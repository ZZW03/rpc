package com.rpc.common.netty.coding;


import com.rpc.common.netty.model.RpcDomain;

import com.rpc.common.netty.model.RpcHeader;
import com.rpc.common.serialize.Serializer;
import com.rpc.common.serialize.SerializerStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@Slf4j
public class EncodeHandle extends MessageToByteEncoder<RpcDomain<Object>> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcDomain<Object> msg, ByteBuf out) throws Exception {
        log.info("Start encoding the data");
        //判断请求头合法性
        if (Objects.isNull(msg)) {
            log.warn("the RpcDto msg is Null!!!");
            return;
        }

        RpcHeader RpcHeader=msg.getHeader();
        out.writeByte(RpcHeader.getVersionId());
        out.writeByte(RpcHeader.getAlgorithmType());
        out.writeByte(RpcHeader.getReqType());
        out.writeLong(RpcHeader.getReqId());
        out.writeInt(RpcHeader.getPort());
        //设定序列化算法
        Serializer serializer= SerializerStrategy.getSerializer(RpcHeader.getAlgorithmType());
        byte[] data=serializer.serialize(msg.getBody());
        //设定数据长度
        out.writeInt(data.length);
        //设定数据
        out.writeBytes(data);
    }

}
