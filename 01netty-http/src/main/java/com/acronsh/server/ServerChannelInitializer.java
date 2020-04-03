package com.acronsh.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author wangyakun
 * @date 2020/4/1
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 从Channel中获取pipline
        ChannelPipeline pipeline = ch.pipeline();
        // HttpServerCodec是什么？？？
        // A combination of HttpRequestDecoder and HttpResponseEncoder
        pipeline.addLast(new HttpServerCodec()).addLast(new ServerHandler());
    }
}
