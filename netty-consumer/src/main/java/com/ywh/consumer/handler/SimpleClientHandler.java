package com.ywh.consumer.handler;

import com.alibaba.fastjson.JSONObject;

import com.ywh.consumer.core.DefaultFuture;
import com.ywh.consumer.param.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {   //接收服务器返回来的数据
        if ("ping".equals(msg.toString())){
            ctx.channel().writeAndFlush("ping\r\n");
            return;
        }
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        DefaultFuture.receive(response);
        ctx.channel().attr(AttributeKey.valueOf("ssssss")).set(msg);
//         ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
