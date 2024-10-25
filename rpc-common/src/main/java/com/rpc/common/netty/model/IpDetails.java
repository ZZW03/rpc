package com.rpc.common.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpDetails {
    String ip;
    int port;
}
