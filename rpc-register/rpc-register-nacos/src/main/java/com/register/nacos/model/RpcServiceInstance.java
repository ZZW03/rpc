package com.register.nacos.model;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceInstance extends Instance {

    /**
     *服务名称
     */
    private String serviceName;

    /**
     * 服务ip
     */
    private String ip;

    /**
     * 服务端口
     */
    private int port;

    /**
     * 服务的组名
     */
    private String clusterName;

    /**
     * 元信息
     */
    private Map<String,String> metadata = new ConcurrentHashMap<>();


}
