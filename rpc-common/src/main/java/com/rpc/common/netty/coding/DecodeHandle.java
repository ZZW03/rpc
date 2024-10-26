package com.rpc.common.netty.coding;

import com.rpc.common.netty.enums.ReqTypeEnum;
import com.rpc.common.netty.model.*;
import com.rpc.common.serialize.Serializer;
import com.rpc.common.serialize.SerializerStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class DecodeHandle extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("Start decoding the data");

        //判断请求头合法性
        if (Objects.isNull(in)) {
            log.warn("the data is Null!!!");
            return;
        }

        //因为我们的数据至少包含请求头+请求体 因此长度小于此肯定有问题
        if (in.readableBytes() < RpcCommonConstants.HEADER_LENGTH) {
            log.warn("The length of the request header does not meet the requirements!!!");
            return;
        }

        //读取版本号
        byte versionId = in.readByte();
        if (versionId != RpcCommonConstants.VERSION_ID) {
            throw new IllegalArgumentException("Illegal versionId!!!");
        }

        byte algorithmType = in.readByte();
        byte reqType = in.readByte();
        long reqId = in.readLong();
        int port = in.readInt();
        int length = in.readInt();

        if (in.readableBytes() < length) {
            log.info("the readable bytes's length is less!!!");
            return;
        }

        RpcHeader header = new RpcHeader(versionId, algorithmType, reqType, reqId, port,length);

        //获得反序列化器
        Serializer serializer = SerializerStrategy.getSerializer(algorithmType);

        //获得请求类型
        ReqTypeEnum reqTypeEnum = ReqTypeEnum.getReqTypeByCode(reqType);

        //得到实际传输的数据
        byte[] data = new byte[length];
        in.readBytes(data);
        RpcDomain dto = null;

        switch (reqTypeEnum) {
            case REQUEST:
                // 处理 GET 请求逻辑
                log.info("Handling REQUEST request");
                RpcRequest request = serializer.deserialize(data, RpcRequest.class);
                dto = new RpcDomain<RpcRequest>();
                dto.setHeader(header);
                dto.setBody(request);
                //设定到out中 会自动被handler处理
                out.add(dto);
                break;
            case RESPONSE:
                // 处理 RESPONSE 请求逻辑
                log.info("Handling RESPONSE request");
                RpcResponse response = serializer.deserialize(data, RpcResponse.class);
                dto = new RpcDomain<RpcResponse>();
                dto.setHeader(header);
                dto.setBody(response);
                //设定到out中 会自动被handler处理
                out.add(dto);
                break;
            case HEARTBEAT:
                // 处理心跳检测逻辑
                log.info("Handling HEARTBEAT request");
                break;
            case ENROLL:
                //处理注册消息
                log.info("Handling ENROLL request");
                dto = new RpcDomain<RpcResponse>();
                dto.setHeader(header);
                out.add(dto);
                break;
            default:
                log.warn("Unsupported request type: " + reqTypeEnum);
        }
    }
}
