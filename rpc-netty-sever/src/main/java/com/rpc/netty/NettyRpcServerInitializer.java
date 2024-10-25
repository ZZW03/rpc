package com.rpc.netty;

import com.rpc.common.netty.handles.coding.DecodeHandle;
import com.rpc.common.netty.handles.coding.EncodeHandle;
import com.rpc.common.netty.handles.NettyRpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new DecodeHandle())
                .addLast(new EncodeHandle())
                .addLast(new NettyRpcServerHandler());
    }
}
