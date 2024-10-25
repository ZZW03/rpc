package com.rpc.common.nacos.model;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.Properties;

public class  namingServiceBuilder  {

    static NamingService namingService = null;

    public static NamingService getNamingService()  {
        if (namingService == null) {
            Properties properties = new Properties();
            properties.put("serverAddr", "127.0.0.1:8848");
            properties.put("namespace", "b0f67995-fe9f-4438-a8a9-b1589fa0bb44");
            try {
                namingService = NacosFactory.createNamingService(properties);
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        }
            return namingService;

    }
}
