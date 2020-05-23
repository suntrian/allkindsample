package com.suntr.antlr.function.suggestion;

import com.suntr.antlr.function.define.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ToString
@Getter
@Accessors(chain = true)
public class ParsedFormula implements Serializable {

    private static final long serialVersionUID = -2850470622126426456L;
    //转换为SQL函数的select
    private final String select;

    private final DataType type;

    private final List<String> physicalFields;

    //聚合函数的聚合SQL
    @Setter
    private List<String> groupBys = Collections.emptyList();

    //字段转换为ID内部表示的原始formula;
    private final String formula;

    public ParsedFormula(String select, DataType type, List<String> physicalFields, String formula) {
        this.select = select;
        this.type = type;
        this.physicalFields = physicalFields;
        this.formula = formula;
    }
}
