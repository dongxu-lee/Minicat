package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    // 使用输出流输出指定字符串
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    /**
     *
     * @param path url 根据url获取静态资源绝对路径，进一步获取静态资源文件，最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {
        // 获取静态资源文件绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);

        // 输出静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists()) {
            // 读取静态资源文件，输出资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        }else {
            // 输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }


    }

}
