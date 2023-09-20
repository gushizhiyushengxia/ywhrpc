package com.ywh.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.ywh.netty.handler.SimpleClientHandler;
import com.ywh.netty.util.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;


public class TCPClient {
    static final Bootstrap b = new Bootstrap();
    static ChannelFuture f=null;
    static {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE,true);
        b.handler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new SimpleClientHandler());
                socketChannel.pipeline().addLast(new StringEncoder());
            }
        });
        String host="localhost";
        int port=8080;
        try{
            f  = b.connect(host,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //1.每一个请求都是同一个连接，并发问题            唯一请求id    请求内容   响应结果
    public static Response send(ClientRequest request){
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }


}
