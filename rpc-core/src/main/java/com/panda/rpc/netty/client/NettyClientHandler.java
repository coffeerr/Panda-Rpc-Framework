package com.panda.rpc.netty.client;

import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.serializer.CommonSerializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 3:58 下午
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        try {
            logger.info("客户端读取到消息：" + rpcResponse);
            AttributeKey<RpcResponse> attributeKey = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(attributeKey).set(rpcResponse);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(rpcResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
