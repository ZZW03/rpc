package com.rpc.common.netty.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcDomain<T> implements Serializable {

    RpcHeader Header;

    T Body;
}
