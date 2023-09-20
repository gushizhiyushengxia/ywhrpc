package com.ywh.user;

import com.ywh.consumer.annotation.Remote;
import com.ywh.consumer.param.Response;



import java.util.List;

@Remote
public interface UserRemote {
    public Response saveUser(User user);
    public Response saveUsers(List<User> userlist);
}
