package com.panda.rpc.socket;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/25 10:57 上午
 */

public class ObjectReader {
    public static final Logger logger = LoggerFactory.getLogger(ObjectReader.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static Object readObject(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        int magic = bytesToInt(bytes);
        if (magic != MAGIC_NUMBER) {
            logger.info("不识别的协议包");
            throw new RpcException(RpcError.MAGIC_NUMBER_ERROR);
        }
        in.read(bytes);
        int packageType = bytesToInt(bytes);
        Class<?> clazz;
        if (packageType == PackageType.REQUEST_PACKAGE.getCode()) {
            clazz = RpcRequest.class;
        } else if (packageType == PackageType.RESPONSE_PACKAGE.getCode()) {
            clazz = RpcResponse.class;
        } else {
            logger.info("不识别的包类型码");
            throw new RpcException(RpcError.PACKAGE_TYPE_ERROR);
        }
        in.read(bytes);
        int serializeCode = bytesToInt(bytes);
        CommonSerializer commonSerializer;
        if (serializeCode == SerializeCode.JSON.getCode()) {
            commonSerializer = new JsonSerializer();
        } else if (serializeCode == SerializeCode.KRYO.getCode()) {
            commonSerializer = new KryoSerializer();
        } else if (serializeCode == SerializeCode.Hessian.getCode()) {
            commonSerializer = new HessianSerializer();
        } else {
            throw new RpcException(RpcError.SERIALIZER_TYPE_ERROT);
        }
        in.read(bytes);
        int dataLength = bytesToInt(bytes);
        byte[] res = new byte[dataLength];
        in.read(res);
        return commonSerializer.deserialize(res, clazz);

    }


    /**
     * @return [int]
     * @description 字节数组转换为Int
     * @date [2021-03-10 21:57]
     */
    private static int bytesToInt(byte[] src) {
        int value;
        value = (src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8)
                | ((src[2] & 0xFF) << 16)
                | ((src[3] & 0xFF) << 24);
        return value;
    }
}
