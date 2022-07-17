package org.example.antlr;

import lombok.extern.slf4j.Slf4j;
import org.example.antlr.sequence.SequenceBaseVisitor;
import org.example.antlr.sequence.SequenceParser;

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
