package com.rpc.client;

import com.rpc.annotation.RpcConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class SpringRpcClientAutoProxy implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext context;
    private ClassLoader classLoader;
    private Integer port;
    private Environment environment = null;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
    }

    // 保存代理bean的定义信息
    private final Map<String, BeanDefinition> rpcBeanDefinitionCache = new ConcurrentHashMap<>();

    //将所有带注解的类 生成代理类 注册成bean
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.port = Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port")));
        Stream.of(beanFactory.getBeanDefinitionNames())
                .map(beanFactory::getBeanDefinition)
                .map(BeanDefinition::getBeanClassName)
                .filter(Objects::nonNull)
                .map(className -> ClassUtils.resolveClassName(className, this.classLoader))
                .forEach(clazz -> ReflectionUtils.doWithFields(clazz, this::resolveRpcAutowiredProxy));

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        rpcBeanDefinitionCache.forEach((beanName, beanDefinition) -> {
            if (!context.containsBean(beanName)) {
                registry.registerBeanDefinition(beanName, beanDefinition);
                log.info("registe bean {} successfully...", beanName);
            } else {
                log.warn("Spring Context has contain bean {}", beanName);
            }
        });
    }

    private void resolveRpcAutowiredProxy(Field field) {
        Optional.ofNullable(AnnotationUtils
                        .getAnnotation(field, RpcConsumer.class))
                .ifPresent(rpcAutowiredProxy -> {
                    BeanDefinitionBuilder builder =
                            BeanDefinitionBuilder.genericBeanDefinition(RpcClientProxy.class);
                    builder.setInitMethodName("generateProxy");
                    builder.addPropertyValue("port",port);
                    builder.addPropertyValue("interfaceClass", field.getType());
                    BeanDefinition beanDefinition = builder.getBeanDefinition();
                    rpcBeanDefinitionCache.put(field.getType().getName(), beanDefinition);
                });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.environment = ((ConfigurableApplicationContext) applicationContext).getEnvironment();
        }
    }

}
