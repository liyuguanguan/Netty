package bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 正常的serversocket绝对不要正要写 这里只是掩饰，正常需要关闭I/O等操作
 * @Author: liyu.guan
 * @Date: 2019/5/7 上午11:45
 */
public class BioServer {

    public static void main(final String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1111);

        while (true) {
            // 阻塞
            Socket socket = serverSocket.accept();
            new Thread(() -> {
//                try {
//                    int len;
//                    byte[] data = new byte[1024];
//                    InputStream inputStream = socket.getInputStream();
//                    // 按字节流方式读取
//                    while ((len = inputStream.read(data)) != -1) {
//                        System.out.println(new String(data, 0, len));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                handle(socket);
            }).start();

        }


    }

    static void handle(Socket socket){


        try {
            byte[] bytes = new byte[1024];
            // 按字节流方式读取
            int len = socket.getInputStream().read(bytes);
            System.out.println(new String(bytes,0,len));
//            socket.getOutputStream().write(bytes,0,len);
            socket.getOutputStream().write("妹爷".getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
