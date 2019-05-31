package netty.ByteToMessageDecoder;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: liyu.guan
 * @Date: 2019/5/31 下午6:42
 */
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new SelfDefineEncodeHandler());
        // 业务处理类
//        ch.pipeline().addLast();
    }
}
