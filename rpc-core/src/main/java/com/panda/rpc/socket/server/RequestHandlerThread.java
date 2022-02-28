package com.panda.rpc.socket.server;

import com.panda.rpc.RequestHandler;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.registry.ServiceRegistry;
import com.panda.rpc.serializer.KryoSerializer;
import com.panda.rpc.socket.ObjectReader;
import com.panda.rpc.socket.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-02-18 11:38]
 * @description IO传输模式|处理客户端RpcRequest的工作线程
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(objectInputStream);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            ObjectWriter.writeObject(objectOutputStream, RpcResponse.success(result,rpcRequest.getRequestId()), new KryoSerializer());
        } catch (IOException e) {
            logger.info("调用或发送时发生错误：" + e);
        }
    }
}
