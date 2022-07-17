package org.example.antlr.function;

import lombok.Getter;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.example.antlr.function.parser.FunctionParser;
import org.example.antlr.function.suggestion.SyntaxError;

import java.util.ArrayList;
import java.util.List;

public class FunctionErrorListener extends BaseErrorListener {

    @Getter
    private final List<SyntaxError> syntaxErrors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        syntaxErrors.add(new SyntaxError(recognizer, (Token) offendingSymbol, line, charPositionInLine, msg, e==null?((FunctionParser) recognizer).getExpectedTokens(): e.getExpectedTokens(),  e));
    }

}
