package org.example.antlr.function;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.example.antlr.function.define.DataType;
import org.example.antlr.function.define.FunctionDefine;
import org.example.antlr.function.parser.FunctionParser;
import org.example.antlr.function.suggestion.SuggestionScope;
import org.example.antlr.function.suggestion.TokenStatus;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FormulaToSqlVisitor extends AbstractFormulaVisitor<Object> {

    @Getter
    private String select;
    @Getter
    private DataType type;

    @Getter
    private List<String> groupBy = new LinkedList<>();

    @Getter
    private List<String> physicalFields = new LinkedList<>();

    public FormulaToSqlVisitor(TokenStreamRewriter rewriter, Map<Integer, Long> columnTokenIndexBusinessIdMap) {
        this.rewriter = rewriter;
        this.columnTokenIndexBusinessIdMap = columnTokenIndexBusinessIdMap;
    }

    public String getFormula() {
        return rewriter.getText();
    }

    @Override
    public FormulaToSqlVisitor visitFormula(FunctionParser.FormulaContext ctx) {
        StatementInfo stmt = visitStatement(ctx.statement());
        this.select = stmt.getSelect();
        this.type = stmt.getDataType();
        return this;
    }

    @Override
    public StatementInfo visitStatement(FunctionParser.StatementContext ctx) {
        if (ctx.exception != null) {
            //todo 处理异常情况
            handleException(ctx.exception);
            return null;
        }
        if (ctx.functionStatement() != null) {
            return visitFunctionStatement(ctx.functionStatement());
        } else if (ctx.op != null) {
            return handleOp(ctx);
        } else if (ctx.caseStatement() != null) {
            return visitCaseStatement(ctx.caseStatement());
        } else if (ctx.column() != null) {
            return visitColumn(ctx.column());
        } else if (ctx.constant() != null) {
            return (StatementInfo) visitConstant(ctx.constant());
        } else if (ctx.L_PARENTHESES() != null && ctx.R_PARENTHESES() != null) {
            StatementInfo stmt = visitStatement(ctx.statement(0));
            return new StatementInfo(ctx)
                    .setSelect("( " + stmt.getSelect() + " )")
                    .setDataType(stmt.getDataType())
                    .setStatus(stmt.getStatus())
                    .setScope(stmt.getScope());
        }
        return null;
    }

    @Override
    public StatementInfo visitCaseStatement(FunctionParser.CaseStatementContext ctx) {
        List<StatementInfo> stmts = new LinkedList<>();
        StringBuilder builder = new StringBuilder(" CASE ");
        if (ctx.caseStmt == null) {
            builder.append("\n");
            int whenSize = ctx.WHEN().size();
            for (int i = 0; i < whenSize; i++) {
                StatementInfo stmt = visitStatement(ctx.statement(i));
                stmts.add(stmt);
                builder.append("\t WHEN ").append(visitPredictStatement(ctx.predictStatement(i)).getSelect())
                        .append(" THEN ").append(stmt.getSelect()).append("\n");
            }
        } else {
            builder.append(visitStatement(ctx.caseStmt).getSelect()).append("\n");
            for (int i = 0; i < ctx.WHEN().size(); i++) {
                StatementInfo stmt = visitStatement(ctx.statement(i+1));
                stmts.add(stmt);
                builder.append("\t WHEN ").append(((StatementInfo)visitConstant(ctx.constant(i))).getSelect())
                        .append(" THEN ").append(stmt.getSelect()).append("\n");
            }
        }
        if (ctx.elseStmt != null) {
            StatementInfo stmt = visitStatement(ctx.elseStmt);
            stmts.add(stmt);
            builder.append("\t ELSE ").append(stmt.getSelect()).append("\n");
        }
        builder.append(" END ");
        if (stmts.stream().map(StatementInfo::getDataType).distinct().count()>1) {
            log.warn("CASE表达式多个条件返回的数据类型不一致");
        }
        return new StatementInfo(ctx)
                .setSelect(builder.toString())
                .setScope(SuggestionScope.RESERVED(null))
                .setDataType(stmts.get(0).getDataType())
                .setStatus(TokenStatus.NORMAL);
    }

    @Override
    public StatementInfo visitPredictStatement(FunctionParser.PredictStatementContext ctx) {
        StatementInfo left = visitStatement(ctx.statement(0));
        if (!DataType.BOOL.isCompatible(left.getDataType())) {
            throw new IllegalStateException("WHEN条件应为BOOL类型");
        }
        if (ctx.op != null) {
            StatementInfo right = visitStatement(ctx.statement(1));
            if (!DataType.BOOL.isCompatible(right.getDataType())) {
                throw new IllegalStateException("WHEN条件应为BOOL类型");
            }
            return new StatementInfo(ctx)
                    .setSelect(left.getSelect() + (ctx.op.getType() == FunctionParser.AND ? " AND " : " OR ") + right.getSelect())
                    .setDataType(DataType.BOOL)
                    .setStatus(TokenStatus.NORMAL)
                    .setScope(SuggestionScope.NONE());
        }
        return left;
    }

    @Override
    public StatementInfo visitFunctionStatement(FunctionParser.FunctionStatementContext ctx) {
        return handleFunction(ctx);
    }

    @Override
    public List<StatementInfo> visitFunctionParams(FunctionParser.FunctionParamsContext ctx) {
        if (ctx == null || ctx.statement() == null) {
            return Collections.emptyList();
        }
        List<StatementInfo> params = new ArrayList<>(ctx.statement().size());
        for (FunctionParser.StatementContext statementContext : ctx.statement()) {
            params.add(visitStatement(statementContext));
        }
        return params;
    }

    public StatementInfo visitColumn(FunctionParser.ColumnContext ctx) {
        return new StatementInfo(ctx).setScope(SuggestionScope.COLUMN()).setDataType(DataType.ANY).setSelect(ctx.getText()).setStatus(TokenStatus.NORMAL);
    }

    private StatementInfo handleOp(FunctionParser.StatementContext ctx) {
        StatementInfo result = new StatementInfo(ctx).setStatus(TokenStatus.NORMAL);
        StatementInfo left = visitStatement(ctx.statement(0)), right = visitStatement(ctx.statement(1));
        if (DataType.ANY.equals(left.getDataType())){
            throw new IllegalStateException(left.getText()+"错误");
        }
        if (DataType.ANY.equals(right.getDataType())){
            if ("null".equalsIgnoreCase(right.getSelect()) && ctx.op.getType() != FunctionParser.EQUAL && ctx.op.getType() != FunctionParser.NOT_EQUAL) {
                throw new IllegalStateException("NULL" +"类型只能使用==或!=判断");
            }
        }

        switch (ctx.op.getType()) {
            case FunctionParser.POWER:
                return result
                        .setSelect("POWER ( " + left.getSelect() + "," + right.getSelect() + ") ")
                        .setScope(SuggestionScope.CAL_OPERATOR())
                        .setDataType(DataType.DOUBLE);
            case FunctionParser.EQUAL:
                String op = "null".equalsIgnoreCase(right.getSelect())?" IS ":" = ";
                return result.setSelect(left.getSelect() + op + right.getSelect())
                        .setDataType(DataType.BOOL)
                        .setScope(SuggestionScope.CMP_OPERATOR());
            case FunctionParser.PLUS:
                if (DataType.STRING.isCompatible(left.getDataType()) && DataType.STRING.isCompatible(right.getDataType())) {
                    String expr = left.getSelect() + "," + right.getSelect();
                    boolean isTop = !(ctx.getParent() instanceof FunctionParser.StatementContext
                            && ((FunctionParser.StatementContext) ctx.getParent()).op != null);
                    if (isTop) {
                        return result.setSelect("CONCAT(" + expr + ")")
                                .setDataType(DataType.STRING)
                                .setStatus(TokenStatus.NORMAL)
                                .setScope(SuggestionScope.CAL_OPERATOR());
                    }
                    return result.setSelect(expr).setDataType(DataType.STRING).setScope(SuggestionScope.CAL_OPERATOR());
                } else if (!left.getDataType().isCompatible(right.getDataType())) {
                    throw new IllegalArgumentException("操作参数类型不匹配:" + left.getDataType() + ctx.op.getText() + right.getDataType());
                }
            default:
                if (isCompareOperator(ctx.op.getType())) {
                    if (!left.getDataType().isCompatible(right.getDataType())) {
                        throw new IllegalStateException("比较操作的数据类型不同");
                    }
                    result.setDataType(DataType.BOOL)
                            .setScope(SuggestionScope.CMP_OPERATOR());
                } else if (isLogicOperator(ctx.op.getType())) {
                    if (!DataType.BOOL.isCompatible(left.getDataType()) || !DataType.BOOL.isCompatible(right.getDataType())) {
                        throw new IllegalStateException("逻辑操作只能用于布尔类型方法或字段");
                    }
                    result.setDataType(DataType.BOOL)
                            .setScope(SuggestionScope.LOG_OPERATOR(ctx.op.getText()));
                } else {
                    result.setDataType(DataType.DOUBLE)
                            .setScope(SuggestionScope.CAL_OPERATOR());
                }
                String leftSelect = left.getSelect(), rightSelect = right.getSelect();
                return result.setSelect(leftSelect + " " + ctx.op.getText() + " " + rightSelect);
        }
    }

    private StatementInfo handleFunction(FunctionParser.FunctionStatementContext ctx) {
        StatementInfo result = new StatementInfo(ctx);
        List<StatementInfo> params = visitFunctionParams(ctx.functionParams());
        String funcName = ctx.IDENTITY().getText();
        if (isInnerAggregateFunction(funcName)) {
            //聚合函数
            boolean isRoot = ctx.getParent().getParent() instanceof FunctionParser.FormulaContext;
            if (!isRoot) {
                throw new IllegalStateException("不允许嵌套聚合函数");
            }
            if (params.size() <= 1) {
                throw new IllegalStateException("分组函数至少应包含一个度量字段");
            }
            if (DataType.STRING.isCompatible(params.get(0).getDataType())){
                throw new IllegalStateException("聚合字段不能为String类型");
            }
            this.groupBy = params.stream().skip(1).map(StatementInfo::getSelect).collect(Collectors.toList());
            String aggTypeName = funcName.replace(GROUP_FUNC_PREFIX, "");
            return result.setSelect(aggTypeName + "(" + params.get(0).getSelect() + ")")
                    .setScope(SuggestionScope.FUNCTION())
                    .setStatus(TokenStatus.NORMAL)
                    .setDataType(DataType.DOUBLE);
        }

        FunctionDefine function = figureFunctionDefine(functions.get(funcName), params);
        DataType dataType = function.getType();
        if ( (dataType == null || DataType.UNKNOWN.equals(dataType)) && function.getTypeParamIndex()!=null) {
            dataType = params.get(function.getTypeParamIndex()-1).getDataType();
        }
        FunctionDefine.FunctionSqlMapping mapping = function.getSqlMapping();
        String sqlExpr = mapping.invoke(params.stream().map(StatementInfo::getSelect).toArray(x->new String[params.size()]));
        return result.setSelect(sqlExpr)
                .setStatus(TokenStatus.NORMAL)
                .setDataType(dataType)
                .setScope(SuggestionScope.FUNCTION());

    }

    private FunctionDefine figureFunctionDefine(List<FunctionDefine> functionDefines, List<StatementInfo> paramInfos) {
        List<FunctionDefine> matched = new ArrayList<>(functionDefines.size());
        List<List<String>> errorInfos = new ArrayList<>();
        OUT:
        for (FunctionDefine functionDefine : functionDefines) {
            List<String> errors = new ArrayList<>();
            if (functionDefine.getParams().isEmpty() && !paramInfos.isEmpty()) {
                errors.add("函数"+functionDefine.getName()+"期待"+functionDefine.getParams().size()+"个参数，实际为空");
                errorInfos.add(errors);
                continue;
            }
            Iterator<StatementInfo> paramIter = paramInfos.iterator();
            int i = 0;
            for (FunctionDefine.ParamDefine param : functionDefine.getParams()) {
                i++;
                if (!paramIter.hasNext()) {
                    errors.add("函数"+functionDefine.getName()+"期待"+functionDefine.getParams().size()+"个参数，实际为" + paramInfos.size());
                    errorInfos.add(errors);
                    continue OUT;
                }
                StatementInfo stmt = paramIter.next();
                if (!param.isVararg()) {
                    if (paramInvalid(param.getType(), stmt.getDataType())) {
                        errors.add("函数"+functionDefine.getName()+"第"+i+"个参数期待"+param.getType().name()+"类型，实际为" + stmt.getDataType().name());
                        errorInfos.add(errors);
                        continue OUT;
                    }
                } else {
                    while (paramIter.hasNext()) {
                        i++;
                        if (paramInvalid(param.getType(), paramIter.next().getDataType())){
                            errors.add("函数"+functionDefine.getName()+"第"+i+"个参数期待"+param.getType().name()+"类型，实际为" + stmt.getDataType().name());
                            errorInfos.add(errors);
                            continue OUT;
                        }
                    }
                }
            }
            matched.add(functionDefine);
        }
        if (matched.size()>=1) {
            return matched.get(0);
        }
        if (!errorInfos.isEmpty()) {
            throw new IllegalStateException(errorInfos.get(0).get(0));
        } else {
            throw new IllegalStateException("函数"+functionDefines.get(0).getName()+"不支持");
        }
    }

    private void handleException(RecognitionException e) {
        throw e;
    }
}
