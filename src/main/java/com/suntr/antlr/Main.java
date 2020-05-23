package com.suntr.antlr;

import com.suntr.antlr.expr.ExprLexer;
import com.suntr.antlr.expr.ExprParser;
import com.suntr.antlr.sequence.SequenceLexer;
import com.suntr.antlr.sequence.SequenceParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

@Slf4j
public class Main {

    public static void main(String[] args) {
        visitSequence("1 2 3 4 5 6 3 8 9 10");
        visitSequence("2 9 10 3 1 2 3");
    }


    public static void visitExpr(String expr){
        CharStream stream = CharStreams.fromString(expr);
        log.info("EXPR:{}", expr);
        ExprLexer lexer = new ExprLexer(stream);
        ExprParser parser = new ExprParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.prog();
        ExprVisitor visitor = new ExprVisitor();
        Object res = visitor.visit(parseTree);
        System.out.println(res);
    }

    public static void visitSequence(String seq) {
        CharStream stream = CharStreams.fromString(seq);
        log.info("INPUT:{}", seq);
        SequenceLexer lexer = new SequenceLexer(stream);
        SequenceParser parser = new SequenceParser(new CommonTokenStream(lexer));
        ParseTree parseTree = parser.file();
        SequenceVisitor visitor = new SequenceVisitor();
        Object res = visitor.visit(parseTree);
        System.out.println(res);
    }


}
