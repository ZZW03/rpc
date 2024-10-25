package com.rpc.common.netty.holder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class FutureHolder {

    static Map<Long, CompletableFuture<Object>> FUTURES = new ConcurrentHashMap<>();

    public static void put(Long id, CompletableFuture<Object> future) {
        FUTURES.put(id, future);
    }

    public static CompletableFuture<Object> get(Long id) {
        return FUTURES.get(id);
    }

    public static CompletableFuture<Object> remove(Long id) {
        return FUTURES.remove(id);
    }


    public static Map<Long, CompletableFuture<Object>> getFUTURES() {
        return FUTURES;
    }

    public static void setFUTURES(Map<Long, CompletableFuture<Object>> FUTURES) {
        FutureHolder.FUTURES = FUTURES;
    }
}
