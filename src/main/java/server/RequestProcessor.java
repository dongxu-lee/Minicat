package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor () {}

    public RequestProcessor (Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            // 拿到url
            String url = request.getUrl();
            // 获取输出流
            OutputStream outputStream = socket.getOutputStream();
            Response response = new Response(outputStream);
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(url);
            }else {
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }

            socket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
