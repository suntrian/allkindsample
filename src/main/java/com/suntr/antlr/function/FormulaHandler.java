package com.suntr.antlr.function;

import com.suntr.antlr.function.parser.FunctionLexer;
import com.suntr.antlr.function.parser.FunctionParser;
import com.suntr.antlr.function.suggestion.ParsedFormula;
import com.suntr.antlr.function.suggestion.SyntaxError;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
