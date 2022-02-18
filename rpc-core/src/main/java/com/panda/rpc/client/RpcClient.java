package com.panda.rpc.client;

import com.panda.rpc.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/18 3:19 下午
 */

public class RpcClient {
    private final static Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendReadRequest(String host, int port, RpcRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.info("客户端发生错误：" + e);
            return null;
        }
    }
}
