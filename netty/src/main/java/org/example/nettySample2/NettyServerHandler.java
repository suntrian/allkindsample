package org.example.nettySample2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

//  服务器端只有这一个处理器
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        byte[] buffer = new byte[msg.readableBytes()]; //  长度为刻度的字节数
        msg.readBytes(buffer);

        String message = new String(buffer, StandardCharsets.UTF_8);//    转换为字符串  指定字符集

        System.out.println("服务端接收到的消息内容：" + message);
        System.out.println("服务端接收到的消息数cc量" + (++this.count));   //  计算收到的消息数量

        ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("utf-8"));
        ctx.writeAndFlush(responseByteBuf); //   发送给客户端
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
