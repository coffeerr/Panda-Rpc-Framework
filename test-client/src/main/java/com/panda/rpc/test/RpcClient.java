package com.panda.rpc.test;

import com.panda.rpc.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/16 9:36 下午
 */

public class RpcClient {
    private final static Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest,String host,int port){
        try(Socket socket = new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        }catch (Exception e){
            logger.error("调用时发生错误" + e);
            return null;
        }
    }
}
