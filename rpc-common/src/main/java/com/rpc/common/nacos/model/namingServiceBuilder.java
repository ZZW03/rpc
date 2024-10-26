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
            properties.put("namespace", "5510f6f2-67cb-438a-bca5-06d5d187d6c1");
            try {
                namingService = NacosFactory.createNamingService(properties);
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        }
            return namingService;

    }
}
