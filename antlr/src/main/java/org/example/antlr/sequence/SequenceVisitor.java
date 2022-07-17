// Generated from D:/projects/allkinksample/src/main/java/com/suntr/antlr/sequence\Sequence.g4 by ANTLR 4.8
package org.example.antlr.sequence;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SequenceParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SequenceVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SequenceParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(SequenceParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link SequenceParser#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(SequenceParser.GroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link SequenceParser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence(SequenceParser.SequenceContext ctx);
}