package com.rpc.common.serialize;
import com.rpc.common.netty.enums.AlgorithmTypeEnum;
import java.util.HashMap;
import java.util.Map;

public class SerializerStrategy {


    private static final Map<Byte, Serializer> serializerMap = new HashMap<>();
    // 定义默认序列化器
    private static final Serializer DEFAULT_SERIALIZER = new JavaSerializer();

    static {
        // 注册所有支持的序列化器
        serializerMap.put(AlgorithmTypeEnum.JSON.getCode(), new JsonSerializer());
        serializerMap.put(AlgorithmTypeEnum.JAVA.getCode(), new JavaSerializer());
        // 可以添加更多的序列化器
    }

    /**
     * 获取序列化器
     * @param type 序列化类型
     * @return Serializer 序列化器
     */
    public static Serializer getSerializer(byte type) {
        return serializerMap.getOrDefault(type, DEFAULT_SERIALIZER);
    }
}
