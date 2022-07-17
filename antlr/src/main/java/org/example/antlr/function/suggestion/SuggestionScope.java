package org.example.antlr.function.suggestion;

import lombok.Getter;

import java.util.Objects;

public class SuggestionScope {

    private static SuggestionScope NONE = new SuggestionScope( Scope.NONE,"");           //不推荐，输入常量或者数字或者完整匹配的时候
    private static SuggestionScope COMMA = new SuggestionScope(Scope.COMMA, ",");          //逗号
    private static SuggestionScope COLUMN = new SuggestionScope(Scope.COLUMN, null);          //空字段
    private static SuggestionScope FUNCTION = new SuggestionScope(Scope.FUNCTION, null);      //空函数

    @Getter
    private final Scope scope;
    @Getter
    private final String text;

    private SuggestionScope(Scope scope, String  text) {
        this.scope = scope;
        this.text = text;
    }

    public static SuggestionScope RESERVED(String text) {
        return new SuggestionScope(Scope.RESERVED, text);
    }

    public static SuggestionScope PARENTHESES(String text) {
        return new SuggestionScope(Scope.PARENTHESES, text);
    }

    public static SuggestionScope BRACE(String text) {
        return new SuggestionScope(Scope.BRACE, text);
    }

    public static SuggestionScope CAL_OPERATOR(String text) {
        return new SuggestionScope(Scope.CAL_OPERATOR, text);
    }

    public static SuggestionScope CAL_OPERATOR() {
        return new SuggestionScope(Scope.CAL_OPERATOR, null);
    }

    public static SuggestionScope CMP_OPERATOR(String text) {
        return new SuggestionScope(Scope.CMP_OPERATOR, text);
    }

    public static SuggestionScope CMP_OPERATOR() {
        return new SuggestionScope(Scope.CMP_OPERATOR, null);
    }

    public static SuggestionScope LOG_OPERATOR(String text) {
        return new SuggestionScope(Scope.LOG_OPERATOR, text);
    }

    public static SuggestionScope FUNCTION(String text) {
        return new SuggestionScope(Scope.FUNCTION, text);
    }

    public static SuggestionScope FUNCTION() {
        return FUNCTION;
    }

    public static SuggestionScope COLUMN(String text) {
        return new SuggestionScope(Scope.COLUMN, text);
    }

    public static SuggestionScope COLUMN() {
        return COLUMN;
    }

    public static SuggestionScope NONE() {
        return NONE;
    }

    public static SuggestionScope COMMA() {
        return COMMA;
    }

    public static SuggestionScope CONSTANT(String constant) {
        return new SuggestionScope(Scope.CONSTANT, constant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuggestionScope that = (SuggestionScope) o;
        return scope == that.scope &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, text);
    }

    @Override
    public String toString() {
        return scope.name() + (text==null?"": "('" + text + "')");
    }

    public enum Scope{
        NONE,           //不推荐，输入常量或者数字或者完整匹配的时候
        RESERVED,         //保留字
        FUNCTION,       //函数
        COLUMN,         //字段
        CONSTANT,       //常量
        CAL_OPERATOR,   //计算运算符
        CMP_OPERATOR,   //比较运算符
        LOG_OPERATOR,   //逻辑运算符
        PARENTHESES,    //括号
        BRACE,        //大括号
        COMMA;          //逗号
    }
}
