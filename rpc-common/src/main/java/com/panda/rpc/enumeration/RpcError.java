package com.panda.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-02-07 18:48]
 * @description Rpc调用过程中出现的错误
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    MAGIC_NUMBER_ERROR("不识别的协议包"),
    PACKAGE_TYPE_ERROR("不识别的包类型码"),
    SERIALIZER_TYPE_ERROT("不识别的序列化器码"),
    RESPONSE_NOT_MATCH("不匹配的回应码"),
    SERIALIZER_NOT_FOUND("未知序列化器"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接Nacos有错误发生"),
    REGISTER_SERVICE_FAILED("注册服务时有错误发生");


    private final String message;
}
