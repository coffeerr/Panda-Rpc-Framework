package com.panda.rpc.codec;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.enumeration.PackageType;
import com.panda.rpc.serializer.CommonSerializer;
import com.panda.rpc.serializer.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 2:56 下午
 */

public class CommonEncoder extends MessageToByteEncoder {
    private final Logger logger = LoggerFactory.getLogger(CommonEncoder.class);
    private int MAGIC_NUMBER = 0xCAFEBABE;
    private CommonSerializer commonSerializer;

    public CommonEncoder(CommonSerializer commonSerializer) {
        this.commonSerializer = commonSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(MAGIC_NUMBER);
        if (o instanceof RpcRequest) {
            byteBuf.writeInt(PackageType.REQUEST_PACKAGE.getCode());
        } else {
            byteBuf.writeInt(PackageType.RESPONSE_PACKAGE.getCode());
        }

        int serializeCode = commonSerializer.getCode();
        byteBuf.writeInt(serializeCode);
        byte[] bytes = commonSerializer.serialize(o);
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
