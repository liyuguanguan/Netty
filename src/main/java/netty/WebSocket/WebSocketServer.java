package netty.WebSocket;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: liyu.guan
 * @Date: 2019/6/3 下午6:04
 */
public class WebSocketServer {


    public static void run(){
        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //将请求和应答消息编码或者解码为Http消息
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                //将Http消息的多个部分组个成一条完整的Http消息
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                //来向客户端发送HTML5文件,它主要用户支持浏览器和服务端进行WEBSOCKET通信
                ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                ch.pipeline().addLast("handler", new WebSocketServerHandler());
            }
        });


        try {
            //绑定端口,同步等待成功
            ChannelFuture f = serverBootstrap.bind(8112).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        run();
    }
}
