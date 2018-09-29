package com.suntr.IOSample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

    final static int PROT = 8765;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PROT);
            System.out.println(" server start .. ");            //进行阻塞
            Socket socket = server.accept();
            // 新建一个线程执行客户端的任务
            new Thread(new ServerHandler(socket)).start();
        } catch (Exception e) {
             		e.printStackTrace();
        } finally {
            if(server != null){
                try {
             			server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server = null;
        }
    }
}

        class ServerHandler implements Runnable {
            private Socket socket;

            public ServerHandler(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {
                BufferedReader in = null;
                PrintWriter out = null;
                try {
                    in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    out = new PrintWriter(this.socket.getOutputStream(), true);
                    String body = null;
                    while (true) {
                        body = in.readLine();
                        if (body == null) break;
                        System.out.println("Server :" + body);
                        out.println("服务器端回送响的应数据.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    socket = null;
                }
            }
        }
