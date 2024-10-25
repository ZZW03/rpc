package com.rpc.common.netty.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration(value = "springRpcProperties")
@ConfigurationProperties(prefix = "zzw.rpc")
public class SpringRpcProperties {

    private Integer nettyPort = 7777;

    private String serviceAddress = "localhost";

    private Integer appPort = 9999;

}
