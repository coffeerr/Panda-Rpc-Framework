package com.panda.rpc.transport.netty.server;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.handler.RequestHandler;
import com.panda.rpc.factory.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-02-22 16:24]
 * @description Netty中处理从客户端传来的RpcRequest
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        requestHandler = new RequestHandler();
        //引入异步业务线程池，避免长时间的耗时业务阻塞netty本身的worker工作线程，耽误了同一个Selector中其他任务的执行
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() -> {
            try {
                logger.info("服务端接收到请求：{}", msg);
                Object response = requestHandler.handle(msg);

                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    //注意这里的通道是workGroup中的，而NettyServer中创建的是bossGroup的，不要混淆
                    ctx.writeAndFlush(response);
                } else {
                    logger.error("通道不可写");
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (ctx instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) ctx).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("长时间未收到心跳包，请求结束");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
