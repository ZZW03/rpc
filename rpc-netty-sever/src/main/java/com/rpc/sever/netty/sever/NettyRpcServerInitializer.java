package com.rpc.sever.netty.sever;

import com.rpc.common.netty.coding.DecodeHandle;
import com.rpc.common.netty.coding.EncodeHandle;
import com.rpc.sever.netty.handle.NettyRpcServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyRpcServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    NettyRpcServerHandler nettyRpcServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new DecodeHandle())
                .addLast(new EncodeHandle())
                .addLast(nettyRpcServerHandler);
    }
}
