package com.suntr.antlr.function.suggestion;

import lombok.Getter;

public enum  TokenStatus {

    //正常
    NORMAL(1),
    //错误
    ERROR(5),
    //未识别
    UNRECGONIZED(4),
    //期望的
    EXPECTED(3),
    //提示
    INFO(2),
    //无
    NONE(0);

    @Getter
    private int privilege;

    TokenStatus(int privilege) {
        this.privilege = privilege;
    }
}
