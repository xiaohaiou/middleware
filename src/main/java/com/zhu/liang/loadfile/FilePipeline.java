package com.zhu.liang.loadfile;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @创建人 ZHULIANG
 * @创建人时间 2020/3/12
 * @描述
 */
@Component
public class FilePipeline extends ChannelInitializer<SocketChannel> {

    @Autowired
    FileServerHandler fleServerHandler;

    /**
     *  channelHandler 的执行顺序是根据，被添加的顺序执行的。
     *      channelInboundHandler 是入站事件
     *      channelOutboundHandler 是出站事件
     *
     * @param socketChannel
     */
    @Override
    protected void initChannel(SocketChannel socketChannel){
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("http-decoder", new HttpRequestDecoder());
        p.addLast("http-aggregator", new HttpObjectAggregator(65536));
        p.addLast("http-encoder", new HttpResponseEncoder());
        p.addLast("http-chunked", new ChunkedWriteHandler());
        //引入文件处理的handler
        p.addLast("fileServerHandler",fleServerHandler);
    }
}
