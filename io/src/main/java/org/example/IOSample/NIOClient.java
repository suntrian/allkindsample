package org.example.IOSample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    private final static  String ServerIP =  "127.0.0.1";
    private final static int PORT = 8765;

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ServerIP, PORT);
        SocketChannel socketChannel = null;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            //打开通道
            socketChannel = SocketChannel.open();
            //进行连接
            socketChannel.connect(address);
            //发送数据
            while (true) {
                byte[] msg = new byte[1024];
                System.in.read(msg);
                buffer.put(msg);
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                socketChannel.close();
            }
        }
    }
}
