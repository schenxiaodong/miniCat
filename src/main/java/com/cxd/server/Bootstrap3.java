package com.cxd.server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * miniCat 的主类
 */
public class Bootstrap3 {
    /**
     * 定义socket监听的端口号
     */
    private int port = 8080;

    // 用于下面存储url-pattern以及其对应的servlet-class的实例化对象
    private Map<String, HttpServlet> servletMap = new HashMap<>();

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
        Bootstrap3 bootstrap = new Bootstrap3();
        try {
            // 启动miniCat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void start() throws Exception {
        /**
         * 在2.0版本的时候，我们只支持静态资源访问，所以直接调用了outputHtml方法
         * 在3.0版本，支持了动态资源。
         * 所以我们要进行判断：在request中得到url，根据url从map中取，如果取不到，就把它当作一个静态资源进行访问
         */
        // 加载解析相关的配u之，web.xml，把配置的servlet存入servletMap中
        loadServlet();

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("========>>miniCat start on port: " + port);

        /**
         * 可以请求动态资源
         */
        while (true) {
            Socket socket = serverSocket.accept();
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
        }
    }

    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            // 根元素
            Element rootElement = document.getRootElement();
            /**
             * 1. 找到所有的servlet标签，找到servlet-name和servlet-class
             * 2. 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
             */
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                /**
                 * 1. 找到所有的servlet标签，找到servlet-name和servlet-class
                 */
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

                /**
                 * 2. 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
                 */
                //Xpath表达式：从/web-app/servlet-mapping下查询，查询出servlet-name=servletName的元素
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
