package com.panda.rpc.codec;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.enumeration.PackageType;
import com.panda.rpc.enumeration.RpcError;
import com.panda.rpc.exception.RpcException;
import com.panda.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/22 4:12 下午
 */

public class CommonDecoder extends ReplayingDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包");
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int packageTypeCode = in.readInt();
        Class<?> packageClass;
        if (packageTypeCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageTypeCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的协议包");
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int serializeTypeCode = in.readInt();
        CommonSerializer commonSerializer = CommonSerializer.getByCode(serializeTypeCode);
        if (commonSerializer == null) {
            logger.error("不识别的序列化器");
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = commonSerializer.deserialize(bytes, packageClass);
        list.add(obj);
    }
}
