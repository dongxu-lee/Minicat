package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResourceUtil {

    /**
     * 获取静态资源文件的绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        System.out.println("=====>>>绝对路径:" + absolutePath);
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 读取静态资源文件输入流，传递给输出流输出
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        int resourceSize = count;
        // 输出http请求头，然后输出具体内容
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());

        // 读取内容输出
        long written = 0; // 已经读取的内容长度
        int byteSize = 1024; // 计划每次缓冲的长度
        byte[] bytes = new byte[byteSize];

        while (written < resourceSize) {
            if (written + byteSize > resourceSize) { // 剩余内容长度不足1024，设置byteSize为真实长度
                byteSize = (int) (resourceSize - written); // 剩余长度
                bytes = new byte[byteSize];
            }

            inputStream.read(bytes);
            outputStream.write(bytes);

            outputStream.flush();
            written += byteSize;
        }

    }
}
