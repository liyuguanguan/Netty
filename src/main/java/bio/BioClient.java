package bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/7 下午12:08
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8321);
        socket.getOutputStream().write((new Date()+"妹爷").getBytes());
        socket.getOutputStream().flush();
        byte[] bytes = new byte[1024];
        int aa = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes,0,aa));
        socket.close();
    }
}
