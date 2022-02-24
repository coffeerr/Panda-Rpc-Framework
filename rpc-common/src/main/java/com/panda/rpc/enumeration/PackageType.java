package com.panda.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/2/24 2:02 下午
 */
@Getter
@AllArgsConstructor
public enum PackageType {
    REQUEST_PACKAGE(1), RESPONSE_PACKAGE(2);
    private int code;
}
