package netty.ProtocolBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private int counter;
    private byte[] req;
    private int req_times = 100;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RichManProto.RichMan.Builder builder = RichManProto.RichMan.newBuilder();
        builder.setId(1);
        builder.setEmail("richman@google.com");
        builder.setName("richman");
        builder.addCars(RichManProto.RichMan.Car.newBuilder().setType(RichManProto.RichMan.CarType.LAMBORGHINI).setName("兰博基尼"));

        ctx.writeAndFlush(builder.build());
//
//
//        ByteBuf message = null;
//        for (int i=0; i<req_times; i++){
//            message = Unpooled.buffer(req.length);
//            message.writeBytes(req);
//            ctx.writeAndFlush(message);
//        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RichManProto.RichMan body = (RichManProto.RichMan) msg;
        System.out.println(body.getName()+"开"+body.getCarsList().get(0).getName());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
