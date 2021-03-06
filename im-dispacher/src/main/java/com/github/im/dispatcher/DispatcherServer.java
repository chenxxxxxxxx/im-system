package com.github.im.dispatcher;

import com.github.im.core.ImConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 请求分发系统
 * 负责分发请求给底层其他各种业务系统进行业务处理
 * @author wangsz
 * @create 2020-03-29
 **/
public class DispatcherServer {

    public static final int port = 8090;

    public static void main(String[] args) throws Exception {
        EventLoopGroup connectionGroup = new NioEventLoopGroup();
        EventLoopGroup ioThreadGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(connectionGroup, ioThreadGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ImConstants.MAX_FRAME_LENGTH,
                                    0, 4, 0, 4));
                            ch.pipeline().addLast(new DispatcherHandler());
                        }
                    });
            ChannelFuture channelFuture = server.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            connectionGroup.shutdownGracefully();
            ioThreadGroup.shutdownGracefully();
        }
    }
}
