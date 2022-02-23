package com.panda.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageType {
    REQUEST_PACK(1),
    RESPONSE_PACK(0);

    private final int code;
}
