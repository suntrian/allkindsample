package com.suntr.antlr.dsl.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.suntr.antlr.dsl.TLBaseVisitor;
import com.suntr.antlr.dsl.TLParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SymbolVisitor extends TLBaseVisitor<TLValue> {

    private Map<String, Function> functions;

    public SymbolVisitor(Map<String, Function> functions) {
        this.functions = functions;
    }

    @Override
    public TLValue visitFunctionDecl(TLParser.FunctionDeclContext ctx) {
        List<TerminalNode> params = ctx.idList() != null ? ctx.idList().Identifier() : new ArrayList<TerminalNode>();
        ParseTree block = ctx.block();
        String id = ctx.Identifier().getText() + params.size();
        functions.put(id, new Function(params, block));
        return TLValue.VOID;
    }
}