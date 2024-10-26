package com.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration(value = "springRpcProperties")
@ConfigurationProperties(prefix = "zzw.rpc")
public class SpringRpcProperties {

    private Integer nettyPort;

    private String serviceAddress  ;

    private Integer appPort;

}
