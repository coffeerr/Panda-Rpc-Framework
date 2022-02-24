package com.panda.rpc.netty.server;

import com.panda.rpc.RequestHandler;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.registry.DefaultServiceRegistry;
import com.panda.rpc.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 3:40 下午
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        try {
            logger.info("服务端读取到消息：" + rpcRequest);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object handle = requestHandler.handle(rpcRequest, service);
            ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(handle));

            //添加一个监听器到channelfuture来检测是否所有的数据包都发出，然后关闭通道
            channelFuture.addListener(ChannelFutureListener.CLOSE);
            // Node 从InBound里读取的ByteBuf要手动释放，还有自己创建的ByteBuf要自己负责释放。
            // 这两处要调用这个release方法。
        } finally {
            ReferenceCountUtil.release(rpcRequest);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
