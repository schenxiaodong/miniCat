package com.cxd.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * miniCat 的主类
 */
public class Bootstrap2 {
    /**
     * 定义socket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * mimiCat的启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap2 bootstrap = new Bootstrap2();
        try {
            // 启动miniCat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() throws IOException {
        /**
         * 完成miniCat 2.0 版本
         * 需求：封装Request和Response对象
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>miniCat start on port: " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            // 封装Request和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());
            socket.close();
        }
    }

//    public void start() throws IOException {
//        /**
//         * 完成miniCat 2.0 版本
//         * 需求：从输入流中获取请求信息
//         */
//        ServerSocket serverSocket = new ServerSocket(port);
//        System.out.println("========>>miniCat start on port: " + port);
//
//        while (true) {
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//            // 从输入流中获取请求信息
//            int count = 0;
//            while (count == 0) {
//                count = inputStream.available();
//            }
//            byte[] bytes = new byte[count];
//            inputStream.read(bytes);
//            System.out.println("请求信息====>>" + new String(bytes));
//        }
//    }
}
