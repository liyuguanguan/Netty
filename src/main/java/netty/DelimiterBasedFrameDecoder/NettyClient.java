package netty.DelimiterBasedFrameDecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/28 上午10:09
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
                .attr(AttributeKey.newInstance("haha"),"DSA")
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ByteBuf byteBuf = Unpooled.copiedBuffer("$".getBytes());
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(4000,byteBuf));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new TimeClientHandler());
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
