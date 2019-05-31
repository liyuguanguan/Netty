package netty.FixedLengthFrameDecoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * FixedLengthFrameDecoder采用定长的方式解决TCP粘包
 * StringDecoder的作用就是将收到的对象转换成字符串
 * @Author: liyu.guan
 * @Date: 2019/5/28 上午10:09
 */
public class NettyServer {


    public static void main(String[] args) {
        int port = 8321;
        new NettyStart(port).severStart();
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
        //进行socketChannel的网络读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 启动的配置
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 第一个group负责连接，第二个group负责连接之后的I/O处理
        serverBootstrap.group(bossGroup, workerGroup)
                // 建立完连接的通道类型这里是NIO
                .channel(NioServerSocketChannel.class)
                //ChannelOption的标准配置参考 https://www.cnblogs.com/xiaoyongsz/p/6133266.html?utm_source=itdadao&utm_medium=referral
                // 设置一个队列大小用来初始化服务端可连接队列，服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //允许端口重复
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                //  TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                .option(ChannelOption.TCP_NODELAY, true)
                //缓冲区大小
                .option(ChannelOption.SO_RCVBUF, 65523)
                //缓冲区大小
                .option(ChannelOption.SO_SNDBUF, 65523)
                //Netty4使用对象池，重用缓冲区
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)


                //每一个客户端连接过来之后 给一个监听器让他处理
                .childHandler(new ChannelInitializer<SocketChannel>() {
                                  @Override
                                  protected void initChannel(SocketChannel ch) throws Exception {
                                      ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                                      ch.pipeline().addLast(new StringDecoder());
                                      //在这个通道上,加一个通道的处理器
                                      ch.pipeline().addLast(new TimeServerHandler());
                                  }
                              }
                        // 上面这种写法和这种一样
//                .childHandler(new ChannelInit()
                );

        try {
            //绑定端口,同步等待成功
            ChannelFuture f = serverBootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            //优雅的退出,释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}