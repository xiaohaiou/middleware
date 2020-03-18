package com.zhu.liang.demos.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/2
 * @描述
 */
public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup  = new NioEventLoopGroup();//线程，用来接收  事件循环组  死循环
        EventLoopGroup workerGroup = new NioEventLoopGroup();//线程，用来处理  事件循环组  死循环

        try{
            //启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)//添加设置两个线程组
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new SereverInitlalizer());//这里去设置初始化类

            System.out.println("启动netty服务...");
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8899)).sync();//阻塞，等待

            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();//优雅关闭
            workerGroup.shutdownGracefully();
        }

    }

}
