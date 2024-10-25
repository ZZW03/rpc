package com.rpc.common.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcHeader implements Serializable {
    /**
     * 版本id
     */
    private byte versionId;

    /**
     * 使用的序列化方式
     */
    private byte algorithmType;

    /**
     * 请求类型
     */
    private byte reqType;

    /**
     * 请求id
     */
    private long reqId;

    /**
     * 端口号
     */
    private int port;

    /**
     * 消息长度
     */
    private int length;
}
