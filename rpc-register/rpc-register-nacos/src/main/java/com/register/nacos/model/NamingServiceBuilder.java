package com.register.nacos.model;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.Properties;

public class NamingServiceBuilder {

    static NamingService namingService = null;

    private static  String serverAddr;

    private static  String namespace;

    public NamingServiceBuilder(String serverAddr, String namespace) {
        this.serverAddr = serverAddr;
        this.namespace = namespace;
    }

    public static NamingService getNamingService()  {
        if (namingService == null) {
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", namespace);
            try {
                namingService = NacosFactory.createNamingService(properties);
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        }
            return namingService;

    }
}
