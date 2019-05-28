package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import netty.util.TimeClientHandler;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/15 下午5:46
 */
public class NettyClient {

    public static void main(String[] args) {
        new NettyClient().clientStart();
    }

    private void clientStart(){
        EventLoopGroup works = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(works)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",8321).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            works.shutdownGracefully();
        }

    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {
    //当客户端和服务端TCP链路建立成功之后,Netty的NIO线程会调用channelAvtive方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i=1;i<50;i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(("query time order"+System.getProperty("line.separator")),
                    CharsetUtil.UTF_8));
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf)msg;
//        System.out.println("客户端开始接收数据:"+byteBuf.toString(CharsetUtil.UTF_8));

        String byteBuf = (String)msg;
        System.out.println("客户端开始接收数据:"+byteBuf);
        System.out.println("dsadsa");
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println(cause.getMessage()+cause);
        ctx.close();
    }

}
