package com.rpc.sever.netty.handle;


import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.sever.netty.holder.SocketHolder;
import com.rpc.common.netty.model.*;
import com.register.nacos.model.RpcServiceInstance;
import com.register.nacos.service.RegisterService;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.rpc.common.netty.enums.ReqTypeEnum.*;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcDomain> {

    static Map<Long, IpDetails> map = new ConcurrentHashMap<>();

    @Autowired
    RegisterService registerService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcDomain msg) throws Exception {
        RpcHeader header = msg.getHeader();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ipPort = remoteAddress.getAddress().getHostAddress() + ":" + header.getPort();
        ReqTypeEnum reqTypeEnum = ReqTypeEnum.getReqTypeByCode(header.getReqType());
        if (reqTypeEnum == REQUEST ){
            log.error("REQUEST {}",msg);
            RpcRequest request = (RpcRequest) msg.getBody();
            List<RpcServiceInstance> allByServiceName = registerService.findAllByServiceName(request.getClassName());
            if(allByServiceName.isEmpty()){
                log.error("找不到这个服务");
            }
            RpcServiceInstance service = allByServiceName.get(0);
            NioSocketChannel nioSocketChannel = SocketHolder.get(service.getIp() + ":" + service.getPort());

            if(allByServiceName.isEmpty()){
                log.error("该服务未连接，请稍后再尝试");
            }
            ChannelFuture channelFuture = nioSocketChannel.writeAndFlush(msg);

            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("Id {} Message Send To {} Success", header.getReqId(), ipPort);
                    map.put(header.getReqId(), new IpDetails(remoteAddress.getAddress().getHostAddress(), header.getPort()));
                } else {
                    log.error("Id {} Message Send To {} Failed", header.getReqId(), ipPort);
                }
            });


        }else if(reqTypeEnum == RESPONSE){

            log.info("Id {} Message Response Received",header.getReqId());
            IpDetails ipDetails = map.get(header.getReqId());
            NioSocketChannel nioSocketChannel = SocketHolder.get(ipDetails.getIp() + ":" + ipDetails.getPort());
            ChannelFuture channelFuture = nioSocketChannel.writeAndFlush(msg);

        }else if (reqTypeEnum == ENROLL){
            SocketHolder.put(ipPort,(NioSocketChannel) ctx.channel());
            log.info("client {} enroll success", ipPort);
        }



    }
}
