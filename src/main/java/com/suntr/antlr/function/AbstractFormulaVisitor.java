package com.suntr.antlr.function;

import com.suntr.antlr.function.define.DataType;
import com.suntr.antlr.function.define.FunctionConfigParser;
import com.suntr.antlr.function.define.FunctionDefine;
import com.suntr.antlr.function.parser.FunctionBaseVisitor;
import com.suntr.antlr.function.parser.FunctionParser;
import com.suntr.antlr.function.suggestion.SuggestionScope;
import com.suntr.antlr.function.suggestion.TokenStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractFormulaVisitor<T> extends FunctionBaseVisitor<T> {

    protected final Map<String, List<FunctionDefine>> functions;
    protected final boolean strictParamCheck = true;
    protected Map<Integer, Long> columnTokenIndexBusinessIdMap;
    protected TokenStreamRewriter rewriter;

    public AbstractFormulaVisitor() {
        this.functions = FunctionConfigParser.mapFunctionsByName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T visitConstant(FunctionParser.ConstantContext ctx) {
        DataType type = null;
        if (ctx.BOOL() != null) {
            type = DataType.BOOL;
        } else if (ctx.STRING() != null) {
            type = DataType.STRING;
        } else if (ctx.INTEGER() != null) {
            type = DataType.INTEGER;
        } else if (ctx.FLOAT() != null) {
            type = DataType.DOUBLE;
        } else if (ctx.NULL() != null) {
            type = DataType.ANY;
        }
        return (T)new StatementInfo(ctx)
                .setSelect(ctx.getText())
                .setStatus(TokenStatus.NORMAL)
                .setScope(SuggestionScope.CONSTANT(ctx.getText()))
                .setDataType(type);
    }

    protected String getColumn(FunctionParser.ColumnContext ctx) {
        return ctx.getText();
    }

    private String getColumnName(String identity) {
        if (identity.startsWith("`") && identity.endsWith("`")){
            return identity.substring(1, identity.length()-1);
        }
        return identity;
    }

    /**
     * 根据ctx中的参数，获取匹配的函数定义，
     * 当ctx中的参数不完整时，也进行推荐
     * @param ctx FunctionStatementContext
     * @return 匹配的函数定义，函数存在重载方法时，可能存在多个
     */
    protected List<FunctionDefine> guessFunctionDefine(FunctionParser.FunctionStatementContext ctx, boolean allowIncompleteParams) {
        String funcName = ctx.IDENTITY().getText();
        List<FunctionDefine> functionDefines = getFunctions(funcName);
        if (functionDefines == null) {
            throw new IllegalStateException("不支持的函数:"+funcName);
        }
        return guessFunctionDefine(ctx.functionParams(), functionDefines, allowIncompleteParams);
    }

    protected List<FunctionDefine> guessFunctionDefine(FunctionParser.FunctionParamsContext ctx, List<FunctionDefine> functionDefines, boolean allowIncompleteParams) {
        List<FunctionDefine> matched = new ArrayList<>(functionDefines.size());
        int[] firstMissMatch = new int[functionDefines.size()];
        Map<Integer, DataType> missMatchRealType = new HashMap<>();
        boolean paramSizeMissMatch = true;
        OUT:
        for (int funcDefIndex = 0; funcDefIndex < functionDefines.size(); funcDefIndex++) {
            FunctionDefine define = functionDefines.get(funcDefIndex);
            boolean hasVararg = !define.getParams().isEmpty() && define.getParams().get(define.getParams().size() - 1).isVararg();
            if (!allowIncompleteParams) {
                if ((!hasVararg && define.getParams().size() != (ctx.statement()==null?0:ctx.statement().size())) || (hasVararg && ctx.statement().size() < define.getParams().size())) {
                    //不存在可变变量且参数数量不一致， 或者存在vararg且实参数量比定义少
                    continue;
                }
            }
            paramSizeMissMatch = false;
            FunctionDefine.ParamDefine param = null;

            for (int paramIndex = 0; paramIndex < ctx.statement().size(); paramIndex++) {
                if (!hasVararg && paramIndex >= define.getParams().size()) {
                    //无可变变量时，参数数量只能少不能多，
                    continue OUT;
                }
                param = paramIndex < define.getParams().size() ? define.getParams().get(paramIndex) : param;
                if (param == null) {
                    //不会到这里的
                    throw new IllegalStateException("这是个彩蛋");
                }
                DataType expectType = param.getType();
                DataType realType = getStatementType(ctx.statement(paramIndex));
                if (!expectType.isCompatible(realType)) {
                    firstMissMatch[funcDefIndex] = paramIndex+1;
                    missMatchRealType.put(funcDefIndex, realType);
                    continue OUT;
                }
            }
            matched.add(define);
        }
        if (paramSizeMissMatch) {
            throw new IllegalStateException("TYPE MISMATCH");
        }

        Integer maxParamIndex, funcDefIndex;
        if (matched.isEmpty() && (maxParamIndex = Arrays.stream(firstMissMatch).max().orElse(0))>0) {
            funcDefIndex = Stream.iterate(0, x->x+1).limit(functionDefines.size()).max(Comparator.nullsLast(Comparator.comparing(x->firstMissMatch[x]))).orElse(0);
            throw new IllegalStateException("TYPE MISMATCH");
        }

        return matched;
    }

    protected List<FunctionDefine> getFunctions(String functionName) {
        return functions.get(functionName);
    }

    /**
     * 获取statement的数据类型
     * @param context
     * @return
     */
    protected DataType getStatementType(FunctionParser.StatementContext context) {
        if (context == null) {
            return DataType.UNKNOWN;
        } else if (context.column()!=null) {
            //字段类型
            return DataType.ANY;
        } else if (context.functionStatement()!=null) {
            String funcName = context.functionStatement().IDENTITY().getText();
            if (isInnerAggregateFunction(funcName)){
                return DataType.DOUBLE;
            }
            if (isInnerDateConvertFunction(funcName)) {
                return DataType.INTEGER;
            }
            List<FunctionDefine> defines = functions.get(funcName);
            if (defines.size() ==1 || defines.stream().map(FunctionDefine::getType).distinct().count() == 1L) {
                return defines.get(0).getType();
            }
            //存在多个类型的的重载方法, 继续判断字段的类型，决定使用哪个重载
            defines = guessFunctionDefine(context.functionStatement(), true);
            if (defines.size()==1 || defines.stream().map(FunctionDefine::getType).distinct().count() == 1L) {
                return defines.get(0).getType();
            }
            //仍然存在多个类型的重载方法，则简单返回第一个吧。此处可以和上面的判断合并，主要是为了判断逻辑清晰，方便调试。
            return defines.get(0).getType();
        } else if (context.caseStatement() !=null ) {
            //if时，返回第一个if的statement的类型。todo 暂不考虑多个if的statement返回类型不一致的问题
            if (context.caseStatement().thenStmt != null && !context.caseStatement().thenStmt.isEmpty()) {
                return getStatementType(context.caseStatement().thenStmt.statement(0));
            } else if (context.caseStatement().elseStmt != null) {
                return getStatementType(context.caseStatement().elseStmt);
            } else {
                return DataType.UNKNOWN;
            }
        } else if (context.constant() != null ) {
            if (context.constant().BOOL() != null) {
                return DataType.BOOL;
            } else if (context.constant().STRING() != null) {
                return DataType.STRING;
            } else if (context.constant().INTEGER() != null) {
                return DataType.INTEGER;
            } else if (context.constant().FLOAT() != null) {
                return DataType.DOUBLE;
            } else if (context.constant().NULL()!= null) {
                return DataType.ANY;
            }
        } else if (context.op !=null ) {
            if ( isCompareOperator(context.op.getType()) ) {
                return DataType.BOOL;
            }
            if (context.op.getType() != FunctionParser.PLUS) {
                //不是加法时，肯定是数字类型
                //todo 进一步判断是Integer还是Double
                return DataType.DOUBLE;
            }
            //加法时 判断第一个statement的类型，todo 暂不考虑两个statement类型不一致的问题
            return getStatementType(context.statement(0));
        } else if (context.L_PARENTHESES()!=null && context.R_PARENTHESES() != null) {
            return getStatementType(context.statement(0));
        }
        return DataType.STRING;
    }

    protected boolean paramInvalid(DataType expectType, DataType realType) {
        if (realType == null) {
            return true;
        }
        if (DataType.ANY.equals(expectType) || DataType.ANY.equals(realType)){
            return false;
        }
        if (this.strictParamCheck) {
            return !expectType.equals(realType);
        } else {
            return !expectType.isCompatible(realType);
        }
    }

    protected boolean paramInvalid(Collection<DataType> expectedTypes, DataType realType) {
        if (expectedTypes.contains(DataType.ANY) || DataType.ANY.equals(realType)) {
            return false;
        }
        if (this.strictParamCheck) {
            return !expectedTypes.contains(realType);
        } else {
            return expectedTypes.stream().noneMatch(x->x.isCompatible(realType));
        }
    }

    /**
     * 是否比较运算符
     * @param opType
     * @return
     */
    static boolean isCompareOperator(int opType) {
        return FunctionParser.GREATER == opType
                || FunctionParser.GREATER_EQUAL == opType
                || FunctionParser.LESS == opType
                || FunctionParser.LESS_EQUAL == opType
                || FunctionParser.EQUAL == opType
                || FunctionParser.NOT_EQUAL == opType;
    }

    static boolean isLogicOperator(int opType) {
        return FunctionParser.AND == opType
                || FunctionParser.OR == opType;
    }

    protected static final String GROUP_FUNC_PREFIX = "GROUP_";
    protected static final String DATE_CONVERT_FUNC = "YEAR,QUARTER,MONTH,WEEK,DAY";

    //是否聚合函数，的内部表示
    static boolean isInnerAggregateFunction(String funcName) {
        return funcName.toUpperCase().startsWith(GROUP_FUNC_PREFIX);
    }

    //是否日期转换函数的内部表示
    static boolean isInnerDateConvertFunction(String funcName) {
        return DATE_CONVERT_FUNC.contains(funcName.toUpperCase());
    }

    @RequiredArgsConstructor
    @Accessors(chain = true)
    @Data
    public static class StatementInfo {

        //原始文本
        private final String text;

        private Object extra;

        private final int startIndex;

        private final int stopIndex;

        private final int tokenIndex;

        //转SQL后
        private String select;

        private DataType dataType;

        private TokenStatus status = TokenStatus.NORMAL;

        private SuggestionScope scope ;

        public StatementInfo(ParserRuleContext context) {
            this(context.getText(), context.getStart().getStartIndex(), context.getStop().getStopIndex()+1, context.getStart().getTokenIndex());
        }
    }
}
