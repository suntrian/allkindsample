package com.suntr.antlr;

import com.suntr.antlr.expr.ExprLexer;
import com.suntr.antlr.expr.ExprParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) {
        CharStream stream = CharStreams.fromString(" 1 + (2 + 3) * 4+ 6 / 2");
        ExprLexer lexer = new ExprLexer(stream);
        ExprParser parser = new ExprParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.prog();
        ExprVisitor visitor = new ExprVisitor();
        Object res = visitor.visit(parseTree);
        System.out.println(res);
    }
}
