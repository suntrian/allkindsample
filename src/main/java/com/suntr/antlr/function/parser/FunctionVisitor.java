// Generated from D:/projects/allkinksample/src/main/java/com/suntr/antlr/function\Function.g4 by ANTLR 4.8
package com.suntr.antlr.function.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunctionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FunctionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FunctionParser#formula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormula(FunctionParser.FormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(FunctionParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(FunctionParser.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(FunctionParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#predictStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredictStatement(FunctionParser.PredictStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#functionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionStatement(FunctionParser.FunctionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#functionParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionParams(FunctionParser.FunctionParamsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code COLUMN_ID}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOLUMN_ID(FunctionParser.COLUMN_IDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code COLUMN_NAME}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOLUMN_NAME(FunctionParser.COLUMN_NAMEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDENTITY(FunctionParser.IDENTITYContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunctionParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(FunctionParser.ConstantContext ctx);
}