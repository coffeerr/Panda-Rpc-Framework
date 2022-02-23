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
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/22 11:35 上午
 */

public class NettyClient implements RpcClient {


    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new CommonDecoder())
//                                .addLast(new CommonEncoder(new JsonSerializer()))
//                                .addLast(new NettyClientHandler());
                        pipeline.addLast(new CommonDecoder())
                        .addLast(new CommonEncoder(new JsonSerializer()))
                        .addLast(new NettyCLientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            Channel channel = sync.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(sync1 -> {
                    if (sync1.isSuccess()) {
                        System.out.println("客户端发送消息：" + rpcRequest.toString());
                    } else {
                        System.out.println("客户端发送消息时有错误发生：" + sync1.cause());
                    }
                });
                channel.closeFuture().sync();
                //AttributeMap<AttributeKey, AttributeValue>是绑定在Channel上的，可以设置用来获取通道对象
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                //get()阻塞获取value
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }

        } catch (InterruptedException e) {
            logger.info("客户端连接时发生错误：" + e);
        }

        return null;
    }
}
