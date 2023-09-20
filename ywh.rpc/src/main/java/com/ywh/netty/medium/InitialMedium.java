package com.ywh.netty.medium;

import com.ywh.netty.annotation.RemoteInvoke;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@Controller
public class InitialMedium implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o.getClass().isAnnotationPresent( RemoteInvoke.class)){ //判断类上是否有controller注解
            Method[] methods = o.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String key= o.getClass().getInterfaces()[0].getName()+"."+method.getName();
                Map<String, BeanMethod> beanMap = Media.beanMap;
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(o);
                beanMethod.setMethod(method);
                beanMap.put(key,beanMethod);
            }
        }
        return o;
    }
}
