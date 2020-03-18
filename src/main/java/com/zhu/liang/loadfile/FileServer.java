package com.zhu.liang.loadfile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/12
 * @描述
 */
@Configuration
public class FileServer {

    private Logger logger = LoggerFactory.getLogger(FileServer.class);

    @Autowired
    FilePipeline filePipeline;

    @Autowired
    NettyFileProperties nettyFileProperties;

    @Bean("starFileServer")
    public String start() {
        Thread thread =  new Thread(() -> {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyFileProperties.getBossThreads());
            NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyFileProperties.getWorkThreads());
            try {
                logger.info("start netty [FileServer] server ,port: " + nettyFileProperties.getPort());
                ServerBootstrap boot = new ServerBootstrap();
                boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(filePipeline);
                Channel ch = null;
                //是否绑定IP
                if(StringUtils.isNotEmpty(nettyFileProperties.getBindIp())){
                    ch = boot.bind(nettyFileProperties.getBindIp(),nettyFileProperties.getPort()).sync().channel();
                }else{
                    ch = boot.bind(nettyFileProperties.getPort()).sync().channel();
                }
                ch.closeFuture().sync();
            } catch (InterruptedException e) {
                logger.error("启动NettyServer错误", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        thread.setName("File_Server");
        thread.start();
        return "file start";
    }

}
