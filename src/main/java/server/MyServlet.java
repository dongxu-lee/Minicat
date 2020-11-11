package server;

import java.io.IOException;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>MyServlet get</h1>";
        try {
            Thread.sleep(1000000);
            response.output(HttpProtocolUtil.getHttpHeader200(content.length()) + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>MyServlet post</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.length()) + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
