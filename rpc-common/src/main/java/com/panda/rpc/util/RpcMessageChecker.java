package com.panda.rpc.util;

import com.panda.rpc.entity.RpcRequest;
import com.panda.rpc.entity.RpcResponse;
import com.panda.rpc.enumeration.ResponseCode;
import com.panda.rpc.enumeration.RpcError;
import com.panda.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/28 5:00 下午
 */

public class RpcMessageChecker {
    public static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);
    private  static String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker() {
    }

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            logger.info("调用服务发生错误");
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if (rpcRequest != null && !rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            logger.info("调用服务发生错误");
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH);
        }
        if(rpcResponse.getStatusCode() != null && !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.info("调用服务发生错误");
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }

}
