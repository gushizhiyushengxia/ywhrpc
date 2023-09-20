package com.ywh.consumer.proxy;


import com.ywh.consumer.annotation.RemoteInvoke;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class InvokeProxy implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Field[] fields= o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RemoteInvoke.class)){
                field.setAccessible(true);

                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {

                        return null;
                    }
                });
            }
        }
        return null;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }
}
