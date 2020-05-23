package com.suntr.antlr.function.define;

import bsh.EvalError;
import bsh.Interpreter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionDefine {

    //函数名
    private String name;
    //函数返回类型
    private DataType type = DataType.UNKNOWN;
    //函数返回类型由第N(1based)个参数确定
    private Integer typeParamIndex;
    //函数释义
    private String comment;
    //函数参数
    private List<ParamDefine> params = Collections.emptyList();
    //函数转Sql的定义
    private FunctionSqlMapping sqlMapping;
    //函数所属的类别，可为多个类别
    private List<String> sections;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Integer getTypeParamIndex() {
        return typeParamIndex;
    }

    public void setTypeParamIndex(Integer typeParamIndex) {
        this.typeParamIndex = typeParamIndex;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ParamDefine> getParams() {
        return params;
    }

    public void setParams(List<ParamDefine> params) {
        this.params = params;
    }

    public FunctionSqlMapping getSqlMapping() {
        return sqlMapping;
    }

    public void setSqlMapping(FunctionSqlMapping sqlMapping) {
        this.sqlMapping = sqlMapping;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDefine define = (FunctionDefine) o;
        return Objects.equals(name, define.name) &&
                Objects.equals(type, define.type) &&
                Objects.equals(params, define.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, params);
    }

    public static class ParamDefine {
        private boolean vararg = false;
        private String name;
        private DataType type;
        private List<String> options;

        public ParamDefine(boolean vararg, String name, DataType type) {
            this.vararg = vararg;
            this.name = name;
            this.type = type;
        }

        public ParamDefine(boolean vararg, String name, DataType type, List<String> options) {
            this(vararg, name, type);
            this.options = options;
        }

        public boolean isVararg() {
            return vararg;
        }

        public String getName() {
            return name;
        }

        public DataType getType() {
            return type;
        }

        public List<String> getOptions() {
            return options;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParamDefine define = (ParamDefine) o;
            return vararg == define.vararg &&
                    Objects.equals(name, define.name) &&
                    type == define.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vararg, name, type);
        }
    }

    public static class FunctionSqlMapping {

        private final String expression;

        private static final Pattern sqlParamPattern = Pattern.compile("#(\\d+)");
        //可执行的代码片段
        private static final Pattern rawCodePattern = Pattern.compile("\\{(.*)\\}", Pattern.DOTALL);

        private static final Interpreter interpreter = new Interpreter();

        public FunctionSqlMapping(String expression) {
            this.expression = expression;
        }

        public String invoke(String... params) {
            Matcher matcher = sqlParamPattern.matcher(expression) ;
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                int idx = Integer.parseInt(matcher.group(1));
                if (params.length<idx) {
                    throw new IllegalStateException("函数"+expression+"参数个数不符");
                }
                if (idx == 0) {
                    matcher.appendReplacement(stringBuffer, String.join(",", params));
                } else {
                    matcher.appendReplacement(stringBuffer, params[idx-1]);
                }

            }
            matcher.appendTail(stringBuffer);
            matcher = rawCodePattern.matcher(stringBuffer);
            StringBuffer stringBuffer1 = new StringBuffer();
            while (matcher.find()) {
                String code = matcher.group(1);
                try {
                    Object result = interpreter.eval(code);
                    if (result == null) {
                        throw new IllegalStateException("函数"+expression+"返回为空");
                    }
                    matcher.appendReplacement(stringBuffer1, result.toString());

                } catch (EvalError evalError) {
                    throw new IllegalStateException("函数"+expression+"转换函数执行错误");
                }
            }
            matcher.appendTail(stringBuffer1);
            return stringBuffer1.toString();
        }
    }
}
