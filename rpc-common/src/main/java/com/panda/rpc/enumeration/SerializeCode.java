package com.panda.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializeCode {
    JSON(1);
    private int code;
}
