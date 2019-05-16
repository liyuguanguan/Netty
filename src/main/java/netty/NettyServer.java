package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/9 下午2:22
 */
public class NettyServer {

    public static void main(String[] args) {
        int port = 8321;
       new  NettyStart(port).severStart();
        synchronized (NettyServer.class){
            while (true){
                try {
                    NettyServer.class.wait();
                } catch (Throwable e) {

                }
            }
        }
    }




}

class NettyStart{

    int port;

    public NettyStart(int port) {
        this.port = port;
    }

    public  void severStart(){
        //相当于 大管家 selector()
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工人
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 启动的配置
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 第一个group负责连接，第二个group负责连接之后的I/O处理
        serverBootstrap.group(bossGroup, workerGroup)
                // 建立完连接的通道类型这里是NIO
                .channel(NioServerSocketChannel.class)
                //每一个客户端连接过来之后 给一个监听器让他处理
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //在这个通道上,加一个通道的处理器
                        ch.pipeline().addLast(new Handler());
                    }
                });

        try {
            ChannelFuture f = serverBootstrap.bind(port).sync();
//            f.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
    }
}

class Handler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(buf.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(msg);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("system error");
        cause.printStackTrace();
        ctx.close();
    }
}

class aa extends ChannelOutboundHandlerAdapter{

}
