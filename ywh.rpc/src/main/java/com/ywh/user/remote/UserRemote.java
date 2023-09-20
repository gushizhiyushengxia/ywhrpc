package com.ywh.user.remote;

import com.ywh.netty.annotation.Remote;
import com.ywh.netty.util.Response;
import com.ywh.netty.util.ResponseUtil;
import com.ywh.user.bean.User;
import com.ywh.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> userlist);
}
