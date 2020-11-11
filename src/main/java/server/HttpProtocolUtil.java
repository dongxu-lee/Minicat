package server;

public class HttpProtocolUtil {

    // 为响应码200提供header
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + contentLength + "\n" +
                "\r\n";
    }

    // 为响应码404提供header
    public static String getHttpHeader404() {
        String str404 = "<h1>404 not found</h1>";
        return "HTTP/1.1 404 Not Found \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + str404.getBytes().length + "\n" +
                "\r\n" + str404;
    }

}
