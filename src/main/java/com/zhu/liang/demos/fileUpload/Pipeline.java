package com.zhu.liang.demos.fileUpload;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/11
 * @描述
 */
public class Pipeline extends ChannelInitializer<SocketChannel> {

    private EventExecutorGroup businessEventExecutorGroup = new DefaultEventExecutorGroup(10);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        /**
         * http服务器端对response编码
         */
        pipeline.addLast("encoder", new HttpResponseEncoder());

        /**
         * http服务器端对request解码3.
         */
        pipeline.addLast("decoder", new HttpRequestDecoder());

        /**
         * 合并请求
         */
        pipeline.addLast("aggregator", new HttpObjectAggregator(655300000));

        /**
         * 正常业务逻辑处理
         */
        pipeline.addLast(businessEventExecutorGroup, new FileServerHandle());
    }

}
