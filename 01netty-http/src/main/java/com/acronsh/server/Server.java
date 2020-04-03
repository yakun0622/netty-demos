package com.acronsh.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author wangyakun
 * @date 2020/4/1
 */
public class Server {

    public static void main(String[] args) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    // 指定NIO通信
                    .channel(NioServerSocketChannel.class)
                    // 指定childGroup中的eventLoop所绑定的线程所有处理的处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 从Channel中获取pipline
                            ChannelPipeline pipeline = ch.pipeline();
                            // HttpServerCodec是什么？？？
                            // A combination of HttpRequestDecoder and HttpResponseEncoder
                            pipeline.addLast(new HttpServerCodec()).addLast(new ServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap
                    // 绑定端口号
                    .bind(8888)
                    // 同步执行
                    .sync();
            System.out.println("server start successful, bind port: 8888");
            // 关闭Channel，也同步执行
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
