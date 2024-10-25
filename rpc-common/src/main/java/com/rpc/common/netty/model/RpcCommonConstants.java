package com.rpc.common.netty.model;


public interface RpcCommonConstants {

    //当前RPC版本 先从1开始
    byte VERSION_ID = 0x01;

    //请求头长度
    int HEADER_LENGTH = 19;

    /**
     * nacos 默认分组名称
     */
    String DEFAULT_GROUP = "DEFAULT_GROUP";

    /**
     * nacos默认名称中心
     */
    String namespace = "public";

    String NACOS_NAMING_CLASS = "com.alibaba.nacos.api.naming.NamingFactory";

    String NACOS_REGISTER_CLASS = "blossom.project.rpc.nacos.NacosRegisterService";

    String ZK_DISCOVERY_CLASS = "org.apache.curator.x.discovery.ServiceDiscovery";

    String ZK_REGISTER_CLASS = "blossom.project.rpc.zookeeper.ZookeeperAutoConfiguration";

    String REGISTER_ADDRESS = "blossom.rpc.registerAddress";
}
