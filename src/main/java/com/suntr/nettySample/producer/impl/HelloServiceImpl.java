package com.suntr.nettySample.producer.impl;

import com.suntr.nettySample.producer.HelloServerHandler;
import com.suntr.nettySample.producer.HelloService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return msg != null? msg + "-----> I'am fine, thank u ":"And you?";
    }
}
