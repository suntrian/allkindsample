// Generated from D:/projects/allkinksample/src/main/java/com/suntr/antlr/function\Function.g4 by ANTLR 4.8
package com.suntr.antlr.function.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FunctionParser}.
 */
public interface FunctionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FunctionParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(FunctionParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(FunctionParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(FunctionParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(FunctionParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(FunctionParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(FunctionParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(FunctionParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(FunctionParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#predictStatement}.
	 * @param ctx the parse tree
	 */
	void enterPredictStatement(FunctionParser.PredictStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#predictStatement}.
	 * @param ctx the parse tree
	 */
	void exitPredictStatement(FunctionParser.PredictStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#functionStatement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionStatement(FunctionParser.FunctionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#functionStatement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionStatement(FunctionParser.FunctionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#functionParams}.
	 * @param ctx the parse tree
	 */
	void enterFunctionParams(FunctionParser.FunctionParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#functionParams}.
	 * @param ctx the parse tree
	 */
	void exitFunctionParams(FunctionParser.FunctionParamsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code COLUMN_ID}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void enterCOLUMN_ID(FunctionParser.COLUMN_IDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code COLUMN_ID}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void exitCOLUMN_ID(FunctionParser.COLUMN_IDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code COLUMN_NAME}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void enterCOLUMN_NAME(FunctionParser.COLUMN_NAMEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code COLUMN_NAME}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void exitCOLUMN_NAME(FunctionParser.COLUMN_NAMEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void enterIDENTITY(FunctionParser.IDENTITYContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDENTITY}
	 * labeled alternative in {@link FunctionParser#column}.
	 * @param ctx the parse tree
	 */
	void exitIDENTITY(FunctionParser.IDENTITYContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunctionParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(FunctionParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunctionParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(FunctionParser.ConstantContext ctx);
}