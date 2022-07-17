package org.example.nettySample.producer.impl;

import org.example.nettySample.producer.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return msg != null? msg + "-----> I'am fine, thank u ":"And you?";
    }
}
