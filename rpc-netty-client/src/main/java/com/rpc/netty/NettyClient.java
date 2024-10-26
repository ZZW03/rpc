package com.rpc.netty;

import com.rpc.common.netty.enums.AlgorithmTypeEnum;
import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.handle.NettyRpcClientHandler;
import com.rpc.common.netty.coding.DecodeHandle;
import com.rpc.common.netty.coding.EncodeHandle;
import com.rpc.common.netty.model.RpcCommonConstants;
import com.rpc.common.netty.model.RpcDomain;
import com.rpc.common.netty.model.RpcHeader;
import com.rpc.common.util.SnowflakeIdWorker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Setter
public class NettyClient {

    private String ipaddr;

    private int port;

    @Value("${server.port}")
    private int selfPort;

    public NettyClient(String ipaddr, int port) {
        this.ipaddr = ipaddr;
        this.port = port;
    }

    @Getter
    static ChannelFuture myChannel = null;



    @PostConstruct
    public void Start() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new DecodeHandle());
                            ch.pipeline().addLast(new EncodeHandle());
                            ch.pipeline().addLast(new NettyRpcClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(ipaddr, port).sync();
            future.addListener((ChannelFuture channelFuture) -> {
                if (channelFuture.isSuccess()) {
                    log.info("netty client connect success");
                    myChannel = channelFuture;
                    RpcDomain<Object> msg = new RpcDomain<>();
                    RpcHeader rpcHeader = new RpcHeader();
                    rpcHeader.setVersionId(RpcCommonConstants.VERSION_ID);
                    rpcHeader.setAlgorithmType(AlgorithmTypeEnum.JSON.getCode());
                    rpcHeader.setReqId(SnowflakeIdWorker.nextId());
                    rpcHeader.setReqType(ReqTypeEnum.ENROLL.getCode());
                    rpcHeader.setPort(selfPort);
                    msg.setHeader(rpcHeader);
                    myChannel.channel().writeAndFlush(msg);
                } else {
                    log.info("netty client connect failed");
                }
            });

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
