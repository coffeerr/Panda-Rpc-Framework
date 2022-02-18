package com.panda.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/18 3:31 下午
 */

public class RpcServer {
    private final static Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private static ExecutorService threadPool;

    public RpcServer() {
        int corePoolSize = 5;
        int maximunPoolSize = 10;
        int keepAliveTime = 60;
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);
        threadPool = new ThreadPoolExecutor(corePoolSize, maximunPoolSize, keepAliveTime, TimeUnit.SECONDS
                , blockingQueue, threadFactory);
    }

    public void register(int port, Object service) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动。。。");
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                logger.info("接收到客户端访问：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket,service));
            }
        } catch (Exception e) {
            logger.info("服务端发生错误：" + e);
        }
    }
}
