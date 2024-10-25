package com.rpc.common.netty.enums;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum ReqTypeEnum {


    ENROLL((byte)0x00),
    REQUEST((byte)0x01),
    RESPONSE((byte)0x02),


    //心跳类型请求
    HEARTBEAT((byte)0x09);

    private final byte code;

    ReqTypeEnum(byte code){
        this.code=code;
    }


    /**
     * 根据code值返回相应的枚举值
     * @param code 要查询的code值
     * @return 对应的ReqTypeEnum枚举值，如果没有找到则返回null
     */
    public static ReqTypeEnum getReqTypeByCode(byte code) {
        return Arrays.stream(ReqTypeEnum.values())
                .filter(type -> type.code == code)
                .findFirst()
                .orElse(null);
    }
}
