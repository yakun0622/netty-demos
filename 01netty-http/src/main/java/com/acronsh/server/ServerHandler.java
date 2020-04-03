package com.acronsh.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author wangyakun
 * @date 2020/4/1
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg = " + msg.getClass());
        System.out.println("client address = " + ctx.channel().remoteAddress());
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            System.out.println("request method = " + request.method().name());
            System.out.println("request uri = " + request.uri());

            if ("/favicon.ico".equals(request.uri())){
                System.out.println("不处理favicon.ico");
                return;
            }

            ByteBuf body = Unpooled.copiedBuffer("hello netty world", CharsetUtil.UTF_8);
            // 生成响应对象
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
            // 初始化response的头部
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

//            ctx.write(response);
//            ctx.flush();

            ctx.writeAndFlush(response)
                    // 添加监听器，响应发送完毕后直接关闭
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * channel中的数据在处理过程中出现异常时执行该逻辑
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
