package com.cxd.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CxdServlet extends HttpServlet{
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1> CXDServlet GET </h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length) + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1> CXDServlet POST </h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes(StandardCharsets.UTF_8).length) + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
