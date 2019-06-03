package netty.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * 可能你会问为什么在这里使用的是SimpleChannelInboundHandler而不使用ChannelInboundHandlerAdapter?
 * 主要原因是 ChannelInboundHandlerAdapter在处理完消息后需要负责释放资源。在这里将调用ByteBuf.release()来释放资源。
 * SimpleChannelInboundHandler会在 完成channelRead0后释放消息，这是通过Netty处理所有消息的ChannelHandler实现了ReferenceCounted接口达到的。
 * 为什么在服务器中不使用SimpleChannelInboundHandler呢?因为服务器要返回相同的消息给客户端，在服务器执行完成写操作之前不能释放调
 * 用读取到的消息，因为写操作是异步的，一旦写操作完成后，Netty中会自动释放消息
 * @Author: liyu.guan
 * @Date: 2019/6/3 下午4:31
 */
public class StringMessageHandler extends SimpleChannelInboundHandler<String> {



    /**
     * 读消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }

    /**
     * 客户端连接服务器后被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(Unpooled.copiedBuffer(("管管"+System.getProperty("line.separator")).getBytes()));
    }
}
