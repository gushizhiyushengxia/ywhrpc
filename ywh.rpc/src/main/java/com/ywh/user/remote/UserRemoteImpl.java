package com.ywh.user.remote;

import com.ywh.netty.util.Response;
import com.ywh.netty.util.ResponseUtil;
import com.ywh.user.bean.User;
import com.ywh.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;


public class UserRemoteImpl implements UserRemote{
    @Resource
    private UserService service;

    public Response saveUser(User user){
        service.save(user);
        Response response = ResponseUtil.createSuccessResult(user);

        return response;
    }

    public Response saveUsers(List<User> userlist){
        service.saveUsers(userlist);
        Response response = ResponseUtil.createSuccessResult(userlist);

        return response;
    }

}
