package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lidongxu
 * @date 2020/11/10
 * @Description
 */
public class Bootstrap {

    /**定义socket监听的端口号*/
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {

        // 记载解析相关配置 web.xml

        loadServlet();

        /**
         * 1.0版本
         * 访问localhost:8080，返回固定字符串
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port:" + port);

//        while (true) {
//            Socket socket = serverSocket.accept();
//            // 有了socket，接收到请求，获取输出流
//            OutputStream outputStream = socket.getOutputStream();
//            String data = "Hello Minicat!";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            outputStream.write(responseText.getBytes());
//            socket.close();
//        }

        /**
         * 2.0版本
         * 封装Request和Response对象，返回html静态资源文件
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            // 获取输入流
//            InputStream inputStream = socket.getInputStream();
//            Request request = new Request(inputStream);
//            // 拿到url
//            String url = request.getUrl();
//            // 获取输出流
//            OutputStream outputStream = socket.getOutputStream();
//            Response response = new Response(outputStream);
//            response.outputHtml(url);
//
//            socket.close();
//        }

        /**
         * 3.0版本
         * 可以请求动态资源
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            // 获取输入流
//            InputStream inputStream = socket.getInputStream();
//            Request request = new Request(inputStream);
//            // 拿到url
//            String url = request.getUrl();
//            // 获取输出流
//            OutputStream outputStream = socket.getOutputStream();
//            Response response = new Response(outputStream);
//            if (servletMap.get(request.getUrl()) == null) {
//                response.outputHtml(url);
//            }else {
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request, response);
//            }
//
//            socket.close();
//        }

        /**
         * 多线程改造--不使用线程池
         */
//        while (true) {
//            Socket socket = serverSocket.accept();
//            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
//            requestProcessor.start();
//        }

        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler);

        /**
         * 多线程改造--使用线程池
         */
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            //requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }

    }

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 加载解析web.xml，初始化servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();

                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Minicat程序启动入口
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
