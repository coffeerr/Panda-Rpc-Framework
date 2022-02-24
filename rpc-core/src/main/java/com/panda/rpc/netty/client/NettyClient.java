package com.panda.rpc.netty.client;

import com.panda.rpc.RpcClient;
import com.panda.rpc.codec.CommonDecoder;
import com.panda.rpc.codec.CommonEncoder;
import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.serializer.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 3:34 下午
 */

public class NettyClient implements RpcClient {
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new JsonSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            Channel channel = sync.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        logger.info("客户端发送信息成功");
                    } else {
                        logger.info("客户端发送信息失败");
                    }
                });
                channel.closeFuture().sync();
            }
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = sync.channel().attr(key).get();
            return rpcResponse.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;


    }
}
