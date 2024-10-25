package com.rpc.common.netty.enums;


import lombok.Getter;

@Getter
public enum AlgorithmTypeEnum {

    // 序列化算法枚举定义
    PROTOBUF((byte)0x01),       // Protobuf序列化
    ARVO((byte)0x02),           // Avro序列化
    JSON((byte)0x03),           // JSON序列化
    JAVA((byte)0x04);           // Java原生序列化

    private final byte code;

    AlgorithmTypeEnum(byte code) {
        this.code = code;
    }

    private static AlgorithmTypeEnum findByCode(byte code) {
        for (AlgorithmTypeEnum type : AlgorithmTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        //没有找到对应的枚举类型就报错
        throw new RuntimeException("No enumeration type was found");
    }
}
