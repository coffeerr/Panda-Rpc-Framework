package com.panda.rpc.codec;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.enumeration.PackageType;
import com.panda.rpc.enumeration.RpcError;
import com.panda.rpc.enumeration.SerializeCode;
import com.panda.rpc.exception.RpcException;
import com.panda.rpc.serializer.CommonSerializer;
import com.panda.rpc.serializer.HessianSerializer;
import com.panda.rpc.serializer.JsonSerializer;
import com.panda.rpc.serializer.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 2:56 下午
 */

public class CommonDecoder extends ReplayingDecoder {
    private final Logger logger = LoggerFactory.getLogger(CommonEncoder.class);
    private int MAGIC_NUMBER = 0xCAFEBABE;
    private CommonSerializer commonSerializer;

//    public CommonDecoder(CommonSerializer commonSerializer) {
//        this.commonSerializer = commonSerializer;
//    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magic_num = byteBuf.readInt();
        if (magic_num != MAGIC_NUMBER) {
            // Note 一种不用借助返回值的报错方法
            logger.error("解码时发生错误");
            throw new RpcException(RpcError.MAGIC_NUMBER_ERROR);
        }
        int packageCode = byteBuf.readInt();
        Class<?> packageType;
        if (packageCode == PackageType.REQUEST_PACKAGE.getCode()) {
            packageType = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACKAGE.getCode()) {
            packageType = RpcResponse.class;
        } else {
            logger.error("解码时发生错误");
            throw new RpcException(RpcError.PACKAGE_TYPE_ERROR);
        }
        int serializeCode = byteBuf.readInt();
        if (serializeCode == SerializeCode.JSON.getCode()) {
            commonSerializer = new JsonSerializer();
        } else if (serializeCode == SerializeCode.KRYO.getCode()) {
            commonSerializer = new KryoSerializer();
        } else if (serializeCode == SerializeCode.Hessian.getCode()) {
            commonSerializer = new HessianSerializer();
        } else {
            logger.error("解码时发生错误");
            throw new RpcException(RpcError.SERIALIZER_TYPE_ERROT);
        }
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object deserialize = commonSerializer.deserialize(bytes, packageType);
        list.add(deserialize);
    }
}
