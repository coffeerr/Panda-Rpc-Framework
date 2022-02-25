package com.panda.rpc.socket;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.enumeration.PackageType;
import com.panda.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/25 2:06 下午
 */

public class ObjectWriter {
    public static final Logger logger = LoggerFactory.getLogger(ObjectWriter.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;


    public static void writeObject(OutputStream out, Object object, CommonSerializer serializer) throws IOException {
        out.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            out.write(intToBytes(PackageType.REQUEST_PACKAGE.getCode()));
        } else {
            out.write(intToBytes(PackageType.RESPONSE_PACKAGE.getCode()));
        }
        out.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        out.write(intToBytes(bytes.length));
        out.write(bytes);
        out.flush();
    }

    private static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[3] = (byte) ((value >> 24) & 0xFF);
        des[2] = (byte) ((value >> 16) & 0xFF);
        des[1] = (byte) ((value >> 8) & 0xFF);
        des[0] = (byte) (value & 0xFF);
        return des;
    }

}
