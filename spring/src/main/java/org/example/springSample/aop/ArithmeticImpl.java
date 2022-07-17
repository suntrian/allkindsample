package org.example.springSample.aop;

import org.springframework.stereotype.Component;

@Component("arithmetic")
public class ArithmeticImpl implements Arithmetic {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (b == 0) {
            throw new  IllegalArgumentException("divided by Zero");
        }
        return a / b;
    }
}
