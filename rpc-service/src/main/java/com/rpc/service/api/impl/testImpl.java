package com.rpc.service.api.impl;

import com.rpc.common.annotation.RpcProvide;
import com.rpc.service.api.test;

@RpcProvide
public class testImpl implements test {

    public testImpl() {
        // 无参构造函数
    }

    @Override
    public String test1() {
        return "test1";
    }

}
