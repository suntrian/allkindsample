package com.suntr.antlr;

import com.suntr.antlr.expr.ExprBaseVisitor;
import com.suntr.antlr.expr.ExprParser;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Map;

public class ExprVisitor extends ExprBaseVisitor<Integer> {

    private Map<String, Integer> memory;

    @Override
    public Integer visitErrorNode(ErrorNode node) {
        return super.visitErrorNode(node);
    }

    @Override
    public Integer visitProg(ExprParser.ProgContext ctx) {
        return super.visitProg(ctx);
    }

    @Override
    public Integer visitPrintExpr(ExprParser.PrintExprContext ctx) {
        System.out.println(ctx.getText());
        return super.visitPrintExpr(ctx);
    }

    @Override
    public Integer visitAssign(ExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        Integer val = visit(ctx.expr());
        memory.put(id, val );
        return val;
    }

    @Override
    public Integer visitBlank(ExprParser.BlankContext ctx) {
        return super.visitBlank(ctx);
    }

    @Override
    public Integer visitParens(ExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ExprParser.MUL == ctx.op.getType()) {
            return left*right;
        } else {
            return left / right;
        }
    }

    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ExprParser.ADD == ctx.op.getType()) {
            return left + right;
        } else {
            return left - right;
        }
    }

    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        return super.visitId(ctx);
    }

    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.parseInt(ctx.INT().getText());
    }

    @Override
    protected boolean shouldVisitNextChild(RuleNode node, Integer currentResult) {
        return super.shouldVisitNextChild(node, currentResult);
    }

    @Override
    public Integer visitTerminal(TerminalNode node) {
        return super.visitTerminal(node);
    }

    @Override
    protected Integer aggregateResult(Integer aggregate, Integer nextResult) {
        return super.aggregateResult(aggregate, nextResult);
    }
}
