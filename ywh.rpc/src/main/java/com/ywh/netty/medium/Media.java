package com.ywh.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.ywh.netty.handler.param.ServerRequest;
import com.ywh.netty.util.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Media {
    public static Map<String,BeanMethod> beanMap;
    static {
         beanMap = new HashMap<String, BeanMethod>();
    }
    private static Media m=null;
    private Media(){}


    public static Media newInstance() {
        if (m==null){
            m=new Media();
        }
        return m;
    }

    //反射处理业务
    public Response  process(ServerRequest request) throws InvocationTargetException, IllegalAccessException {
        String command = request.getCommand();
        BeanMethod beanMethod = beanMap.get(command);
        if (beanMethod==null){
            return null;
        }
        Object bean = beanMethod.getBean();
        Method m = beanMethod.getMethod();
        Class paramType= m.getParameterTypes()[0];
        Object content=request.getContent();
        Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
        Response result=(Response)m.invoke(bean,args);
        request.setId(request.getId());
        return result;
    }
}
