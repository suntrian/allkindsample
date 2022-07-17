package org.example.antlr.function;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.example.antlr.function.parser.FunctionLexer;
import org.example.antlr.function.parser.FunctionParser;
import org.example.antlr.function.suggestion.ParsedFormula;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class FormulaHandler {

    private FormulaHandler() {
    }

    public static ParsedFormula toFormulaColumn(String formula) {
        return toFormulaColumn(formula, Collections.emptyMap());
    }

    public static ParsedFormula toFormulaColumn(String formula, Map<Integer, Long> columnTokenIndexBusinessIdMap) {
        CharStream in = CharStreams.fromString(formula);
        FunctionLexer lexer = new FunctionLexer(in);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokenStream);
        FunctionParser parser = new FunctionParser(tokenStream);
        parser.removeErrorListeners();
        FunctionErrorListener errorListener = new FunctionErrorListener();
        parser.addErrorListener(errorListener);
        FormulaToSqlVisitor visitor = new FormulaToSqlVisitor(rewriter, columnTokenIndexBusinessIdMap).visitFormula(parser.formula());
        if (!errorListener.getSyntaxErrors().isEmpty()) {

        }
        return new ParsedFormula(visitor.getSelect(), visitor.getType(),
                visitor.getPhysicalFields(), visitor.getFormula())
                .setGroupBys(visitor.getGroupBy());
    }

}
