package com.cxd.server;

import java.nio.charset.StandardCharsets;

/**
 * http协议工具类，主要提供响应头信息，这里我没只提供200和404的情况
 */
public class HttpProtocolUtil {
    /**
     * 为响应码200提供请求头信息
     */
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-type: text/html \n" +
                "Content-Length: " + contentLength + "\n" +
                "\r\n";
    }

    /**
     * 为响应码404提供请求头信息
     */
    public static String getHttpHeader404() {
        String str404 = "<h4>404 not found<h4>";
        return "HTTP/1.1 200 OK \n" +
                "Content-type: text/html \n" +
                "Content-Length: " + str404.getBytes(StandardCharsets.UTF_8).length + "\n" +
                "\r\n" + str404;
    }
}
