package com.rpc.configuraion;




import com.register.nacos.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
public class RpcServiceAutoConfiguration  {

    @Autowired
    RegisterService registerService;


    @Bean
    @Lazy
    public RpcServiceAutoProcess rpcServiceAutoProcess(){
        return new RpcServiceAutoProcess(registerService);
    }





}
