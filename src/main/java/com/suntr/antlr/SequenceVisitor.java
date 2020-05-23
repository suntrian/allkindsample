package com.suntr.antlr;

import com.suntr.antlr.sequence.SequenceBaseVisitor;
import com.suntr.antlr.sequence.SequenceParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SequenceVisitor extends SequenceBaseVisitor<Object> {

    @Override
    public Object visitFile(SequenceParser.FileContext ctx) {
        return super.visitFile(ctx);
    }

    @Override
    public Object visitGroup(SequenceParser.GroupContext ctx) {
        log.info("GROUP:{}", ctx.getText());
        return super.visitGroup(ctx);
    }

    @Override
    public Object visitSequence(SequenceParser.SequenceContext ctx) {
        log.info("Sequence:{}",ctx.getText());
        return super.visitSequence(ctx);
    }
}
