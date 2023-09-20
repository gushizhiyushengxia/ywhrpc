package cpm.ywh.netty.test;

import com.ywh.netty.client.ClientRequest;

import com.ywh.netty.client.TCPClient;
import com.ywh.netty.util.Response;
import com.ywh.user.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestTCP.class)
@ComponentScan("com.ywh")
public class TestTCP {
    @Test
    public void testGetResponse(){
        ClientRequest request = new ClientRequest();
        request.setContent("测试TCP长连接请求");
        Response res = TCPClient.send(request);
        System.out.println(res.getResult());
    }

    @Test
    public void testSaveUsers(){
        ClientRequest request = new ClientRequest();
        ArrayList<User> users = new ArrayList<User>();
        User u = new User();
        u.setId(1);
        u.setName("张三");
        users.add(u);
        request.setCommand("com.ywh.user.controller.UserController.saveUsers");
        request.setContent(users);
        Response resp = TCPClient.send(request);
        System.out.println(resp.getResult());
    }
}
