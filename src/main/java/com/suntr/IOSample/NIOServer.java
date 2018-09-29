package com.suntr.IOSample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer  extends Thread {

    //多路复用器,管理所有的通道
    private Selector selector;
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);
    public NIOServer(int port) {
        try {
            //打开多路复用器
            this.selector  = Selector.open();
            //打开服务器通道
            ServerSocketChannel channel = ServerSocketChannel.open();
            //设置服务器通道为非阻塞模式
            channel.configureBlocking(false);
            //绑定地址
            channel.bind(new InetSocketAddress(port));
            //把服务器通道注册到多路复用器上，并且监听阻塞事件
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server start at port:" + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //让多路复用器开始监听
                this.selector.select();
                //返回多路复用器已经选择的结果
                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                //遍历
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (key.isValid()) {
                        //如果为阻塞状态
                        if (key.isAcceptable()) {
                            this.accept(key);
                        }
                        if (key.isReadable()) {
                            this.read(key);
                        }
                        if (key.isWritable()) {
                            this.write(key);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            //获取服务信道
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            //执行阻塞方法
            SocketChannel socketChannel = channel.accept();
            //设置阻塞模式
            socketChannel.configureBlocking(false);
            //注册到多路复用器上，并设置读取标识
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) {

    }

    private void read(SelectionKey key) {
        try {
            //清空缓冲区旧数据；
            this.readBuf.clear();
            //获取之前注册的socket通道对象
            SocketChannel socketChannel = (SocketChannel) key.channel();
            //读取数据
            int count = socketChannel.read(readBuf);
            if (count == -1) {
                socketChannel.close();
                key.cancel();
                return;
            }
            //有数据则进行读取，读取之前需要进行复位方法
            this.readBuf.flip();
            //根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] buffer = new byte[readBuf.remaining()];
            //接收缓冲区数据
            this.readBuf.get(buffer);
            //打印结果
            String msg = new String(buffer).trim();
            System.out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread server = new NIOServer(8765);
        server.start();
    }
}
