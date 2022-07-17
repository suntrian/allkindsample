package org.example;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.antlr.dsl.TLLexer;
import org.example.antlr.dsl.TLParser;
import org.example.antlr.dsl.java.EvalVisitor;
import org.example.antlr.dsl.java.Function;
import org.example.antlr.dsl.java.Scope;
import org.example.antlr.dsl.java.SymbolVisitor;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TestAntlrDsl {


    @Test
    public void testDsl2Visitor() throws IOException {
        InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream("dsl/test2.tl");
        TLLexer lexer = new TLLexer(CharStreams.fromStream(fileStream));
        TLParser parser = new TLParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(true);
        ParseTree tree = parser.parse();

        Scope scope = new Scope();
        Map<String, Function> functions = new HashMap<>();
        SymbolVisitor symbolVisitor = new SymbolVisitor(functions);
        symbolVisitor.visit(tree);
        EvalVisitor visitor = new EvalVisitor(scope, functions);
        visitor.visit(tree);
    }

    @Test
    public void testDsl1Visitor() throws IOException {
        InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream("dsl/test.tl");
        TLLexer lexer = new TLLexer(CharStreams.fromStream(fileStream));
        TLParser parser = new TLParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(true);
        ParseTree tree = parser.parse();

        Scope scope = new Scope();
        Map<String, Function> functions = new HashMap<>();
        SymbolVisitor symbolVisitor = new SymbolVisitor(functions);
        symbolVisitor.visit(tree);
        EvalVisitor visitor = new EvalVisitor(scope, functions);
        visitor.visit(tree);
    }
}
