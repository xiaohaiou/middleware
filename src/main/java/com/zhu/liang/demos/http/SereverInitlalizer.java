package com.zhu.liang.demos.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/2
 * @描述
 */
public class SereverInitlalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //http里用的
        pipeline.addLast(new HttpServerCodec());
        //把前面设置的handler加到最后
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        pipeline.addLast(new HttpServerHandler());
    }
}
