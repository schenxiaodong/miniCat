package com.cxd.server;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 封装Response对象，需要依赖于OutputStream对象
 */
public class Response {
    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @param path 指的就是Request 中的url，随后要根据url赖获取到静态资源的绝对路径，进一步根据绝对路径读取该静态资源文件，最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);

        // 输出静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()) {
            // 读取静态资源文件，输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        } else {
            // 输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }

    /**
     * 使用输出流输出指定字符串
     * @param context
     * @throws IOException
     */
    public void output(String context) throws IOException {
        outputStream.write(context.getBytes(StandardCharsets.UTF_8));
    }
}
