package com.ywh.netty.client;

import com.ywh.netty.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;


public class NettyClient {
    public static void main(String[] args) {
        String host="localhost";
        int port =8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
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

           ChannelFuture future = b.connect(host,port).sync();
           future.channel().writeAndFlush("hello server");
           future.channel().writeAndFlush("\r\n");
           future.channel().closeFuture().sync();   //客户端接收数据是异步接收的，应该返回到主线程当中去
           Object result= future.channel().attr( AttributeKey.valueOf("ssssss")).get();
            System.out.println("获取到服务器返回的数据"+result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
