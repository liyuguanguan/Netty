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
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

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
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",8321).sync();
//            channelFuture.channel().closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            works.shutdownGracefully();
//        }

    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("虑；就打死了；快放假快乐大；实际付款方法发大水大书法家搜附近ad搜if阿萨德大师傅",
                CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("客户端开始接收数据:"+byteBuf.toString(CharsetUtil.UTF_8));
        ReferenceCountUtil.release(msg);
    }

}
