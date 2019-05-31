package netty.ByteToMessageDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器
 * @Author: liyu.guan
 * @Date: 2019/5/31 下午6:40
 */
public class SelfDefineEncodeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        int beginIndex = in.readerIndex();
        int length = in.readInt();

        if ((in.readableBytes()+1) < length) {
            in.readerIndex(beginIndex);
            return;
        }

        in.readerIndex(beginIndex + 4 + length);

        ByteBuf otherByteBufRef = in.slice(beginIndex, 4 + length);

        otherByteBufRef.retain();

        out.add(otherByteBufRef);
    }
}
