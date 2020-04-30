// Generated from D:/projects/allkinksample/src/main/java/com/suntr/antlr/function\Function.g4 by ANTLR 4.8
package com.suntr.antlr.function.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FunctionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		L_PARENTHESES=1, R_PARENTHESES=2, L_BRACE=3, R_BRACE=4, COMMA=5, COLON=6, 
		MUL=7, DIV=8, PLUS=9, MINUS=10, POWER=11, MOD=12, GREATER=13, GREATER_EQUAL=14, 
		EQUAL=15, LESS=16, LESS_EQUAL=17, NOT_EQUAL=18, AND=19, OR=20, IF=21, 
		CASE=22, WHEN=23, THEN=24, ELSE=25, END=26, BOOL=27, NULL=28, STRING=29, 
		INTEGER=30, FLOAT=31, COLUMN_ID=32, BUSINESS_COLUMN_ID=33, IDENTITY=34, 
		COLUMN_NAME=35, WS=36, LINE_COMMENT=37, BLOCK_COMMENT=38;
	public static final int
		RULE_formula = 0, RULE_statement = 1, RULE_caseStatement = 2, RULE_ifStatement = 3, 
		RULE_predictStatement = 4, RULE_functionStatement = 5, RULE_functionParams = 6, 
		RULE_column = 7, RULE_constant = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"formula", "statement", "caseStatement", "ifStatement", "predictStatement", 
			"functionStatement", "functionParams", "column", "constant"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'{'", "'}'", "','", "':'", "'*'", "'/'", "'+'", 
			"'-'", "'^'", "'%'", "'>'", "'>='", null, "'<'", "'<='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "L_PARENTHESES", "R_PARENTHESES", "L_BRACE", "R_BRACE", "COMMA", 
			"COLON", "MUL", "DIV", "PLUS", "MINUS", "POWER", "MOD", "GREATER", "GREATER_EQUAL", 
			"EQUAL", "LESS", "LESS_EQUAL", "NOT_EQUAL", "AND", "OR", "IF", "CASE", 
			"WHEN", "THEN", "ELSE", "END", "BOOL", "NULL", "STRING", "INTEGER", "FLOAT", 
			"COLUMN_ID", "BUSINESS_COLUMN_ID", "IDENTITY", "COLUMN_NAME", "WS", "LINE_COMMENT", 
			"BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Function.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }




	public FunctionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class FormulaContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode EOF() { return getToken(FunctionParser.EOF, 0); }
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitFormula(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitFormula(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_formula);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			statement(0);
			setState(19);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public Token op;
		public FunctionStatementContext functionStatement() {
			return getRuleContext(FunctionStatementContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public CaseStatementContext caseStatement() {
			return getRuleContext(CaseStatementContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ColumnContext column() {
			return getRuleContext(ColumnContext.class,0);
		}
		public TerminalNode L_PARENTHESES() { return getToken(FunctionParser.L_PARENTHESES, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode R_PARENTHESES() { return getToken(FunctionParser.R_PARENTHESES, 0); }
		public TerminalNode POWER() { return getToken(FunctionParser.POWER, 0); }
		public TerminalNode MOD() { return getToken(FunctionParser.MOD, 0); }
		public TerminalNode MUL() { return getToken(FunctionParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(FunctionParser.DIV, 0); }
		public TerminalNode PLUS() { return getToken(FunctionParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(FunctionParser.MINUS, 0); }
		public TerminalNode GREATER() { return getToken(FunctionParser.GREATER, 0); }
		public TerminalNode GREATER_EQUAL() { return getToken(FunctionParser.GREATER_EQUAL, 0); }
		public TerminalNode LESS() { return getToken(FunctionParser.LESS, 0); }
		public TerminalNode LESS_EQUAL() { return getToken(FunctionParser.LESS_EQUAL, 0); }
		public TerminalNode EQUAL() { return getToken(FunctionParser.EQUAL, 0); }
		public TerminalNode NOT_EQUAL() { return getToken(FunctionParser.NOT_EQUAL, 0); }
		public TerminalNode AND() { return getToken(FunctionParser.AND, 0); }
		public TerminalNode OR() { return getToken(FunctionParser.OR, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		return statement(0);
	}

	private StatementContext statement(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatementContext _localctx = new StatementContext(_ctx, _parentState);
		StatementContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_statement, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(22);
				functionStatement();
				}
				break;
			case 2:
				{
				setState(23);
				if (!(false)) throw new FailedPredicateException(this, "false");
				setState(24);
				ifStatement();
				}
				break;
			case 3:
				{
				setState(25);
				caseStatement();
				}
				break;
			case 4:
				{
				setState(26);
				constant();
				}
				break;
			case 5:
				{
				setState(27);
				column();
				}
				break;
			case 6:
				{
				setState(28);
				match(L_PARENTHESES);
				setState(29);
				statement(0);
				setState(30);
				match(R_PARENTHESES);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(54);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(52);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(34);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(35);
						((StatementContext)_localctx).op = match(POWER);
						setState(36);
						statement(7);
						}
						break;
					case 2:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(37);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(38);
						((StatementContext)_localctx).op = match(MOD);
						setState(39);
						statement(7);
						}
						break;
					case 3:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(40);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(41);
						((StatementContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((StatementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(42);
						statement(6);
						}
						break;
					case 4:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(43);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(44);
						((StatementContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((StatementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(45);
						statement(5);
						}
						break;
					case 5:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(46);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(47);
						((StatementContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GREATER) | (1L << GREATER_EQUAL) | (1L << EQUAL) | (1L << LESS) | (1L << LESS_EQUAL) | (1L << NOT_EQUAL))) != 0)) ) {
							((StatementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(48);
						statement(4);
						}
						break;
					case 6:
						{
						_localctx = new StatementContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_statement);
						setState(49);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(50);
						((StatementContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((StatementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(51);
						statement(3);
						}
						break;
					}
					} 
				}
				setState(56);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CaseStatementContext extends ParserRuleContext {
		public StatementContext thenStmt;
		public StatementContext caseStmt;
		public StatementContext elseStmt;
		public TerminalNode CASE() { return getToken(FunctionParser.CASE, 0); }
		public TerminalNode END() { return getToken(FunctionParser.END, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(FunctionParser.ELSE, 0); }
		public List<TerminalNode> WHEN() { return getTokens(FunctionParser.WHEN); }
		public TerminalNode WHEN(int i) {
			return getToken(FunctionParser.WHEN, i);
		}
		public List<PredictStatementContext> predictStatement() {
			return getRuleContexts(PredictStatementContext.class);
		}
		public PredictStatementContext predictStatement(int i) {
			return getRuleContext(PredictStatementContext.class,i);
		}
		public List<TerminalNode> THEN() { return getTokens(FunctionParser.THEN); }
		public TerminalNode THEN(int i) {
			return getToken(FunctionParser.THEN, i);
		}
		public List<ConstantContext> constant() {
			return getRuleContexts(ConstantContext.class);
		}
		public ConstantContext constant(int i) {
			return getRuleContext(ConstantContext.class,i);
		}
		public CaseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterCaseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitCaseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitCaseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaseStatementContext caseStatement() throws RecognitionException {
		CaseStatementContext _localctx = new CaseStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_caseStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			match(CASE);
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(58);
					match(WHEN);
					setState(59);
					predictStatement();
					setState(60);
					match(THEN);
					setState(61);
					((CaseStatementContext)_localctx).thenStmt = statement(0);
					}
					}
					setState(65); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WHEN );
				}
				break;
			case 2:
				{
				setState(67);
				((CaseStatementContext)_localctx).caseStmt = statement(0);
				setState(73); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(68);
					match(WHEN);
					setState(69);
					constant();
					setState(70);
					match(THEN);
					setState(71);
					((CaseStatementContext)_localctx).thenStmt = statement(0);
					}
					}
					setState(75); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==WHEN );
				}
				break;
			}
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(79);
				match(ELSE);
				setState(80);
				((CaseStatementContext)_localctx).elseStmt = statement(0);
				}
			}

			setState(83);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfStatementContext extends ParserRuleContext {
		public StatementContext thenStmt;
		public StatementContext elseStmt;
		public TerminalNode IF() { return getToken(FunctionParser.IF, 0); }
		public TerminalNode L_PARENTHESES() { return getToken(FunctionParser.L_PARENTHESES, 0); }
		public PredictStatementContext predictStatement() {
			return getRuleContext(PredictStatementContext.class,0);
		}
		public TerminalNode R_PARENTHESES() { return getToken(FunctionParser.R_PARENTHESES, 0); }
		public List<TerminalNode> L_BRACE() { return getTokens(FunctionParser.L_BRACE); }
		public TerminalNode L_BRACE(int i) {
			return getToken(FunctionParser.L_BRACE, i);
		}
		public List<TerminalNode> R_BRACE() { return getTokens(FunctionParser.R_BRACE); }
		public TerminalNode R_BRACE(int i) {
			return getToken(FunctionParser.R_BRACE, i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> ELSE() { return getTokens(FunctionParser.ELSE); }
		public TerminalNode ELSE(int i) {
			return getToken(FunctionParser.ELSE, i);
		}
		public List<IfStatementContext> ifStatement() {
			return getRuleContexts(IfStatementContext.class);
		}
		public IfStatementContext ifStatement(int i) {
			return getRuleContext(IfStatementContext.class,i);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_ifStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(IF);
			setState(86);
			match(L_PARENTHESES);
			setState(87);
			predictStatement();
			setState(88);
			match(R_PARENTHESES);
			setState(89);
			match(L_BRACE);
			setState(90);
			((IfStatementContext)_localctx).thenStmt = statement(0);
			setState(91);
			match(R_BRACE);
			setState(96);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(92);
					match(ELSE);
					setState(93);
					ifStatement();
					}
					} 
				}
				setState(98);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(99);
				match(ELSE);
				setState(100);
				match(L_BRACE);
				setState(101);
				((IfStatementContext)_localctx).elseStmt = statement(0);
				setState(102);
				match(R_BRACE);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredictStatementContext extends ParserRuleContext {
		public Token op;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode AND() { return getToken(FunctionParser.AND, 0); }
		public TerminalNode OR() { return getToken(FunctionParser.OR, 0); }
		public PredictStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predictStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterPredictStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitPredictStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitPredictStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredictStatementContext predictStatement() throws RecognitionException {
		PredictStatementContext _localctx = new PredictStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_predictStatement);
		int _la;
		try {
			setState(111);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				statement(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(107);
				statement(0);
				setState(108);
				((PredictStatementContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==AND || _la==OR) ) {
					((PredictStatementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(109);
				statement(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionStatementContext extends ParserRuleContext {
		public TerminalNode L_PARENTHESES() { return getToken(FunctionParser.L_PARENTHESES, 0); }
		public TerminalNode R_PARENTHESES() { return getToken(FunctionParser.R_PARENTHESES, 0); }
		public TerminalNode IDENTITY() { return getToken(FunctionParser.IDENTITY, 0); }
		public TerminalNode IF() { return getToken(FunctionParser.IF, 0); }
		public FunctionParamsContext functionParams() {
			return getRuleContext(FunctionParamsContext.class,0);
		}
		public FunctionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterFunctionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitFunctionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitFunctionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionStatementContext functionStatement() throws RecognitionException {
		FunctionStatementContext _localctx = new FunctionStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_functionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_la = _input.LA(1);
			if ( !(_la==IF || _la==IDENTITY) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(114);
			match(L_PARENTHESES);
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(115);
				functionParams();
				}
				break;
			}
			setState(118);
			match(R_PARENTHESES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionParamsContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(FunctionParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FunctionParser.COMMA, i);
		}
		public FunctionParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionParams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterFunctionParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitFunctionParams(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitFunctionParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionParamsContext functionParams() throws RecognitionException {
		FunctionParamsContext _localctx = new FunctionParamsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_functionParams);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			statement(0);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(121);
				match(COMMA);
				setState(122);
				statement(0);
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnContext extends ParserRuleContext {
		public ColumnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column; }
	 
		public ColumnContext() { }
		public void copyFrom(ColumnContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IDENTITYContext extends ColumnContext {
		public TerminalNode IDENTITY() { return getToken(FunctionParser.IDENTITY, 0); }
		public IDENTITYContext(ColumnContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterIDENTITY(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitIDENTITY(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitIDENTITY(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class COLUMN_NAMEContext extends ColumnContext {
		public TerminalNode COLUMN_NAME() { return getToken(FunctionParser.COLUMN_NAME, 0); }
		public COLUMN_NAMEContext(ColumnContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterCOLUMN_NAME(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitCOLUMN_NAME(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitCOLUMN_NAME(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class COLUMN_IDContext extends ColumnContext {
		public TerminalNode BUSINESS_COLUMN_ID() { return getToken(FunctionParser.BUSINESS_COLUMN_ID, 0); }
		public TerminalNode COLUMN_ID() { return getToken(FunctionParser.COLUMN_ID, 0); }
		public COLUMN_IDContext(ColumnContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterCOLUMN_ID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitCOLUMN_ID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitCOLUMN_ID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnContext column() throws RecognitionException {
		ColumnContext _localctx = new ColumnContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_column);
		try {
			setState(132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BUSINESS_COLUMN_ID:
				_localctx = new COLUMN_IDContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(128);
				match(BUSINESS_COLUMN_ID);
				}
				break;
			case COLUMN_ID:
				_localctx = new COLUMN_IDContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(129);
				match(COLUMN_ID);
				}
				break;
			case COLUMN_NAME:
				_localctx = new COLUMN_NAMEContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(130);
				match(COLUMN_NAME);
				}
				break;
			case IDENTITY:
				_localctx = new IDENTITYContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(131);
				match(IDENTITY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(FunctionParser.STRING, 0); }
		public TerminalNode INTEGER() { return getToken(FunctionParser.INTEGER, 0); }
		public TerminalNode FLOAT() { return getToken(FunctionParser.FLOAT, 0); }
		public TerminalNode PLUS() { return getToken(FunctionParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(FunctionParser.MINUS, 0); }
		public TerminalNode BOOL() { return getToken(FunctionParser.BOOL, 0); }
		public TerminalNode NULL() { return getToken(FunctionParser.NULL, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FunctionListener ) ((FunctionListener)listener).exitConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FunctionVisitor ) return ((FunctionVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_constant);
		int _la;
		try {
			setState(141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(134);
				match(STRING);
				}
				break;
			case PLUS:
			case MINUS:
			case INTEGER:
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS || _la==MINUS) {
					{
					setState(135);
					_la = _input.LA(1);
					if ( !(_la==PLUS || _la==MINUS) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(138);
				_la = _input.LA(1);
				if ( !(_la==INTEGER || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 3);
				{
				setState(139);
				match(BOOL);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 4);
				{
				setState(140);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return statement_sempred((StatementContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean statement_sempred(StatementContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return false;
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3(\u0092\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3#\n\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\67\n"+
		"\3\f\3\16\3:\13\3\3\4\3\4\3\4\3\4\3\4\3\4\6\4B\n\4\r\4\16\4C\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\6\4L\n\4\r\4\16\4M\5\4P\n\4\3\4\3\4\5\4T\n\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5a\n\5\f\5\16\5d\13\5\3\5\3\5\3\5"+
		"\3\5\3\5\5\5k\n\5\3\6\3\6\3\6\3\6\3\6\5\6r\n\6\3\7\3\7\3\7\5\7w\n\7\3"+
		"\7\3\7\3\b\3\b\3\b\7\b~\n\b\f\b\16\b\u0081\13\b\3\t\3\t\3\t\3\t\5\t\u0087"+
		"\n\t\3\n\3\n\5\n\u008b\n\n\3\n\3\n\3\n\5\n\u0090\n\n\3\n\2\3\4\13\2\4"+
		"\6\b\n\f\16\20\22\2\b\3\2\t\n\3\2\13\f\3\2\17\24\3\2\25\26\4\2\27\27$"+
		"$\3\2 !\2\u00a3\2\24\3\2\2\2\4\"\3\2\2\2\6;\3\2\2\2\bW\3\2\2\2\nq\3\2"+
		"\2\2\fs\3\2\2\2\16z\3\2\2\2\20\u0086\3\2\2\2\22\u008f\3\2\2\2\24\25\5"+
		"\4\3\2\25\26\7\2\2\3\26\3\3\2\2\2\27\30\b\3\1\2\30#\5\f\7\2\31\32\6\3"+
		"\2\2\32#\5\b\5\2\33#\5\6\4\2\34#\5\22\n\2\35#\5\20\t\2\36\37\7\3\2\2\37"+
		" \5\4\3\2 !\7\4\2\2!#\3\2\2\2\"\27\3\2\2\2\"\31\3\2\2\2\"\33\3\2\2\2\""+
		"\34\3\2\2\2\"\35\3\2\2\2\"\36\3\2\2\2#8\3\2\2\2$%\f\t\2\2%&\7\r\2\2&\67"+
		"\5\4\3\t\'(\f\b\2\2()\7\16\2\2)\67\5\4\3\t*+\f\7\2\2+,\t\2\2\2,\67\5\4"+
		"\3\b-.\f\6\2\2./\t\3\2\2/\67\5\4\3\7\60\61\f\5\2\2\61\62\t\4\2\2\62\67"+
		"\5\4\3\6\63\64\f\4\2\2\64\65\t\5\2\2\65\67\5\4\3\5\66$\3\2\2\2\66\'\3"+
		"\2\2\2\66*\3\2\2\2\66-\3\2\2\2\66\60\3\2\2\2\66\63\3\2\2\2\67:\3\2\2\2"+
		"8\66\3\2\2\289\3\2\2\29\5\3\2\2\2:8\3\2\2\2;O\7\30\2\2<=\7\31\2\2=>\5"+
		"\n\6\2>?\7\32\2\2?@\5\4\3\2@B\3\2\2\2A<\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD"+
		"\3\2\2\2DP\3\2\2\2EK\5\4\3\2FG\7\31\2\2GH\5\22\n\2HI\7\32\2\2IJ\5\4\3"+
		"\2JL\3\2\2\2KF\3\2\2\2LM\3\2\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2\2OA\3\2\2"+
		"\2OE\3\2\2\2PS\3\2\2\2QR\7\33\2\2RT\5\4\3\2SQ\3\2\2\2ST\3\2\2\2TU\3\2"+
		"\2\2UV\7\34\2\2V\7\3\2\2\2WX\7\27\2\2XY\7\3\2\2YZ\5\n\6\2Z[\7\4\2\2[\\"+
		"\7\5\2\2\\]\5\4\3\2]b\7\6\2\2^_\7\33\2\2_a\5\b\5\2`^\3\2\2\2ad\3\2\2\2"+
		"b`\3\2\2\2bc\3\2\2\2cj\3\2\2\2db\3\2\2\2ef\7\33\2\2fg\7\5\2\2gh\5\4\3"+
		"\2hi\7\6\2\2ik\3\2\2\2je\3\2\2\2jk\3\2\2\2k\t\3\2\2\2lr\5\4\3\2mn\5\4"+
		"\3\2no\t\5\2\2op\5\4\3\2pr\3\2\2\2ql\3\2\2\2qm\3\2\2\2r\13\3\2\2\2st\t"+
		"\6\2\2tv\7\3\2\2uw\5\16\b\2vu\3\2\2\2vw\3\2\2\2wx\3\2\2\2xy\7\4\2\2y\r"+
		"\3\2\2\2z\177\5\4\3\2{|\7\7\2\2|~\5\4\3\2}{\3\2\2\2~\u0081\3\2\2\2\177"+
		"}\3\2\2\2\177\u0080\3\2\2\2\u0080\17\3\2\2\2\u0081\177\3\2\2\2\u0082\u0087"+
		"\7#\2\2\u0083\u0087\7\"\2\2\u0084\u0087\7%\2\2\u0085\u0087\7$\2\2\u0086"+
		"\u0082\3\2\2\2\u0086\u0083\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0085\3\2"+
		"\2\2\u0087\21\3\2\2\2\u0088\u0090\7\37\2\2\u0089\u008b\t\3\2\2\u008a\u0089"+
		"\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u0090\t\7\2\2\u008d"+
		"\u0090\7\35\2\2\u008e\u0090\7\36\2\2\u008f\u0088\3\2\2\2\u008f\u008a\3"+
		"\2\2\2\u008f\u008d\3\2\2\2\u008f\u008e\3\2\2\2\u0090\23\3\2\2\2\21\"\66"+
		"8CMOSbjqv\177\u0086\u008a\u008f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}