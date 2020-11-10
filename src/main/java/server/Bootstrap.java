package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    public void start() throws IOException {

        /**
         * 1.0版本
         * 访问localhost:8080，返回固定字符串
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====Minicat start on port:" + port);

        while (true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello Minicat!".getBytes());
            socket.close();
        }

    }

    /**
     * Minicat程序启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
