package org.example.nettySample2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    //channelActive()   客户端以服务端建立连接后，服务端执行的方法
    // 客户端和服务端建立连接后，发送十次消息给服务端
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf buffer = Unpooled.copiedBuffer("发送自客户端：", StandardCharsets.UTF_8);
            ctx.writeAndFlush(buffer);
        }
    }


    // 接收到服务端发送过来的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        String message = new String(buffer, StandardCharsets.UTF_8);

        System.out.println("客户端接收到消息内容：" + message);
        System.out.println("客户端接受到的消息数量：" + (++this.count));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}