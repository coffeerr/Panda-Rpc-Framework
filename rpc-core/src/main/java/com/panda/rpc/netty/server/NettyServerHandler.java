package com.panda.rpc.netty.server;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.registry.ServiceRegistry;
import com.panda.rpc.server.RequestHandler;
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
 * @time: 2022/2/23 9:54 上午
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            String serviceName = msg.getInterfaceName();
            Object service = serviceRegistry.getService(serviceName);
            Object result = requestHandler.handle(msg, service);
            ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(result));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error("" + e);
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }
}
