package com.rpc.common.serialize;

import com.alibaba.fastjson.JSON;
import com.rpc.common.netty.enums.AlgorithmTypeEnum;


public class JsonSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return JSON.parseObject(new String(bytes),clazz);
    }

    @Override
    public byte getSerializerType() {
        return AlgorithmTypeEnum.JSON.getCode();
    }
}
