package com.ywh.netty.init;

import com.ywh.netty.constant.Constants;
import com.ywh.netty.factory.ZookeeperFactory;
import com.ywh.netty.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class NettyInital implements ApplicationListener<ContextRefreshedEvent> {
    public  void start() {
        //NioEventLoopGroup实际上是一个线程池，用于接收客户端的连接
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        //NioEventLoopGroup实际上是一个线程池，用于负责网络的读写
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup,childGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)  //允许最多128个线程等待
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .channel(NioServerSocketChannel.class)   //NioSocketChannel作为通道的实现
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(6535, Delimiters.lineDelimiter()[0]));
                            socketChannel.pipeline().addLast(new StringDecoder());//字符串解码器
//						    socketChannel.pipeline().addLast(new IdleStateHandler(20, 15, 10, TimeUnit.SECONDS));
                            socketChannel.pipeline().addLast(new SimpleServerHandler());
                            socketChannel.pipeline().addLast(new StringEncoder());//字符串编码器
                        }
                    });
            ChannelFuture future = null;
            future = bootstrap.bind(8080).sync();
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress netAddress=InetAddress.getLocalHost();
            client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.SERVER_PATH+netAddress);

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.start();
    }
}
