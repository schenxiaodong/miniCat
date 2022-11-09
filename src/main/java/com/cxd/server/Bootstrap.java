package com.cxd.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * miniCat 的主类
 */
public class Bootstrap {
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
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动miniCat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        /**
         * 完成miniCat 1.0 版本
         * 需求：浏览器请求http://localhost:8080，返回一个固定的字符串到页面“Hello miniCat”
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>miniCat start on port: " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输入流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello miniCat！";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes(StandardCharsets.UTF_8).length) + data;
            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }
}
