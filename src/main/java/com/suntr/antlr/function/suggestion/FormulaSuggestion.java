package com.suntr.antlr.function.suggestion;

import com.google.common.collect.ImmutableSet;
import com.suntr.antlr.function.define.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

@Accessors(chain = true)
@ToString
@Setter
public class FormulaSuggestion {

    private List<TokenSuggestion> suggestions;

    //formula的内部表达式，将字段名称替换为ID
    @Getter
    private String expression;

    private List<FormulaToken> tokens;

    public List<FormulaToken> getTokens() {
        if (tokens==null) { return Collections.emptyList(); }
        return tokens.stream().filter(Objects::nonNull).sorted(Comparator.comparingInt(FormulaToken::getIndex)).collect(Collectors.toList());
    }

    public List<TokenSuggestion> getSuggestions() {
        if (suggestions == null) { return Collections.emptyList(); }
        return suggestions.stream().sorted(Comparator.comparingInt(x->((TokenSuggestion)x).getStatus().getPrivilege()).reversed()).collect(Collectors.toList());
    }

    @Accessors(chain = true)
    @ToString
    @Setter
    public static class TokenSuggestion {

        @Getter
        private TokenStatus status;
        @Getter
        private Integer index;
        @Getter
        private Integer start;
        @Getter
        private Integer stop;
        @Getter
        private String text;

        //光标Token的光标左侧文本
        @Getter
        private String leftPart = "";

        //token说明
        @Getter
        private String comment;

        //推荐的数据范围
        @Getter
        private Set<SuggestionScope> scopes;

        //推荐的数据类型，当且仅当字段和函数时有效
        @Getter
        private Set<DataType> dataTypes = Collections.emptySet();

        public TokenSuggestion setScopes(SuggestionScope... scopes) {
            if (scopes == null || scopes.length == 0) {
                return this;
            }
            this.scopes = ImmutableSet.copyOf(scopes);
            return this;
        }

        public TokenSuggestion setScopes(Collection<SuggestionScope> scope) {
            this.scopes = scope instanceof Set? (Set<SuggestionScope>)scope:  new HashSet<>(scope);
            return this;
        }

        public TokenSuggestion setDataTypes(DataType... dataTypes) {
            if (dataTypes==null || dataTypes.length==0) {
                return this;
            }
            this.dataTypes = ImmutableSet.copyOf(dataTypes);
            return this;
        }

        public TokenSuggestion setDataTypes(Collection<DataType> types) {
            if (types == null || types.isEmpty()) {
                return this;
            }
            this.dataTypes = ImmutableSet.copyOf(types);
            return this;
        }

        //Token.getStopIndex返回的是结束字符的序号，此处需要+1，表示结束时光标的位置
        public TokenSuggestion setStop(Integer stop) {
            this.stop = stop+1;
            return this;
        }
    }
}
