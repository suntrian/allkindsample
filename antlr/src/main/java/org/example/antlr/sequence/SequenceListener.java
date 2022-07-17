// Generated from D:/projects/allkinksample/src/main/java/com/suntr/antlr/sequence\Sequence.g4 by ANTLR 4.8
package org.example.antlr.sequence;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SequenceParser}.
 */
public interface SequenceListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SequenceParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(SequenceParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SequenceParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(SequenceParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SequenceParser#group}.
	 * @param ctx the parse tree
	 */
	void enterGroup(SequenceParser.GroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link SequenceParser#group}.
	 * @param ctx the parse tree
	 */
	void exitGroup(SequenceParser.GroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link SequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void enterSequence(SequenceParser.SequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SequenceParser#sequence}.
	 * @param ctx the parse tree
	 */
	void exitSequence(SequenceParser.SequenceContext ctx);
}