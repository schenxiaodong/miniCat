package com.cxd.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * 线程处理类
 */
public class RequestProcessor extends Thread{
    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            // 封装Request和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();
            if(!url.equals("/favicon.ico")) {
                // 静态资源处理
                if (servletMap.get(url) == null) {
                    response.outputHtml(url);
                } else {
                    // 动态资源处理
                    HttpServlet httpServlet = servletMap.get(url);
                    httpServlet.service(request, response);
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
