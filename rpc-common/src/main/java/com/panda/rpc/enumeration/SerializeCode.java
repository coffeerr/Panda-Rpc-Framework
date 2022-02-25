package com.panda.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializeCode {
    KRYO(0),
    JSON(1),
    Hessian(2);
    private int code;
}
