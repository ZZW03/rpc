package com.rpc;


import com.rpc.netty.NettySever;

public class NettyStart {
    public static void main(String[] args) {
        NettySever nettySever = new NettySever();
        nettySever.start();
    }
}
