package com.suntr.antlr.function.suggestion;

import com.quantchi.intelquery.formula.function.FunctionConfigParser;
import com.suntr.antlr.function.define.FunctionDefine;
import com.suntr.antlr.function.parser.FunctionParser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Accessors(chain = true)
@Data
public class FormulaToken {

    private Long id;
    private Long businessId;        //字段时的业务线ID
    private String text;
    private Integer start;
    private Integer stop;
    private Integer index;
    private TokenStatus status;
    private SuggestionScope scope;
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaToken that = (FormulaToken) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(stop, that.stop) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, stop, index);
    }

    public static FormulaToken from(Token token, Map<Integer, Long> columnTokenIndexBusinessIdMap) {
        FormulaToken formulaToken = new FormulaToken();
        formulaToken.setIndex(token.getTokenIndex());
        formulaToken.setStart(token.getStartIndex());
        formulaToken.setStop(token.getStopIndex()+1);
        formulaToken.setText(token.getText());
        formulaToken.setStatus(TokenStatus.NORMAL);
        switch (token.getType()) {
            case FunctionParser.L_PARENTHESES:
            case FunctionParser.R_PARENTHESES:
                return formulaToken.setScope(SuggestionScope.PARENTHESES(token.getText()));
//            case FunctionParser.L_BRACE:
//            case FunctionParser.R_BRACE:
//                return formulaToken.setScope(SuggestionScope.BRACE(token.getText()));
            case FunctionParser.CASE:
            case FunctionParser.WHEN:
            case FunctionParser.THEN:
            case FunctionParser.ELSE:
            case FunctionParser.END:
                return formulaToken.setScope(SuggestionScope.RESERVED(token.getText()));
            case FunctionParser.COMMA:
                return formulaToken.setScope(SuggestionScope.COMMA());
            case FunctionParser.GREATER:
            case FunctionParser.GREATER_EQUAL:
            case FunctionParser.EQUAL:
            case FunctionParser.LESS:
            case FunctionParser.LESS_EQUAL:
            case FunctionParser.NOT_EQUAL:
                return formulaToken.setScope(SuggestionScope.CMP_OPERATOR(token.getText()));
            case FunctionParser.PLUS:
            case FunctionParser.MINUS:
            case FunctionParser.DIV:
            case FunctionParser.MUL:
            case FunctionParser.POWER:
            case FunctionParser.MOD:
                return formulaToken.setScope(SuggestionScope.CAL_OPERATOR(token.getText()));
            case FunctionParser.AND:
            case FunctionParser.OR:
                return formulaToken.setScope(SuggestionScope.LOG_OPERATOR(token.getText()));
            case FunctionParser.BOOL:
            case FunctionParser.INTEGER:
            case FunctionParser.FLOAT:
            case FunctionParser.STRING:
            case FunctionParser.NULL:
                return formulaToken.setScope(SuggestionScope.NONE());
            case FunctionParser.COLUMN_ID:
                return formulaToken.setScope(SuggestionScope.COLUMN())
                        .setId(Long.parseLong(token.getText().substring(1)))
                        .setBusinessId(columnTokenIndexBusinessIdMap.get(token.getTokenIndex()))
                        ;
            case FunctionParser.BUSINESS_COLUMN_ID:
                return formulaToken.setScope(SuggestionScope.COLUMN())
                        .setId(Long.parseLong(token.getText().substring(1).split(":")[0]))
                        .setBusinessId(columnTokenIndexBusinessIdMap.get(token.getTokenIndex()));
            case FunctionParser.COLUMN_NAME:
            case FunctionParser.IDENTITY:
                String text = token.getText();
                return formulaToken.setComment("").setScope(SuggestionScope.COLUMN(text));
            default:
                return formulaToken.setComment("未处理的Token类型")
                        .setScope(SuggestionScope.NONE());
        }
    }

    public static FormulaToken from(TerminalNode node, Map<Integer, Long> columnTokenIndexBusinessIdMap) {
        if (node instanceof ErrorNode) {
            return null;
        }
        FormulaToken token = from(node.getSymbol(), columnTokenIndexBusinessIdMap);
        if (token.getScope()==null) {
            if (node.getParent() instanceof FunctionParser.FunctionStatementContext) {
                return token.setScope(SuggestionScope.FUNCTION());
            } else {
                return token.setScope(SuggestionScope.COLUMN());
            }
        }
        return token;
    }

    @Override
    public String toString() {
        return text;
    }
}
