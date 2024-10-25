package com.rpc.common.netty.holder;

import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketHolder {
    @Getter
    static Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    public static void put(String name, NioSocketChannel channel) {
         CHANNELS.put(name, channel);
    }

    public static NioSocketChannel get(String name) {
        return CHANNELS.get(name);
    }

    public static void remove(String name) {
        CHANNELS.remove(name);
    }

}
