package com.cxd.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StaticResourceUtil {
    /**
     * 获取静态资源方法的绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 读取静态资源文件输入流，通过输出流输出静态资源
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        // 静态资源长度
        int resourceSize = count;
        // 输出http请求头，然后再输出具体的内容
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes(StandardCharsets.UTF_8));

        // 读取内容输出
        long written = 0;   // 已经读取的内容长度
        int byteSize = 1024;// 计划每次缓冲的长度
        byte[] bytes = new byte[byteSize];

        while (written < resourceSize) {

            if(written + byteSize > resourceSize) {
                // 剩余魏都区大小不足一个1024长度，那就按照真实长度处理
                byteSize = (int)(resourceSize - written); // 剩余的文件内容长度
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written += byteSize;
        }
    }
}
