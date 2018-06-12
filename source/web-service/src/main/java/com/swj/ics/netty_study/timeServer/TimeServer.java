package com.swj.ics.netty_study.timeServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * author shiweijie
 * date 2018/6/12 下午7:32
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8888;

        startServer(port);
    }

    static void startServer(int port) {
        NioEventLoopGroup watcherGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap sbp = new ServerBootstrap();
        sbp.group(watcherGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChildChannelHandler());

        try {
            //绑定端口，等待绑定成功。
            ChannelFuture channelFuture = sbp.bind(port).sync();

            //等待服务端的监听端口关闭
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally { //优雅推出，释放线程池资源。
            watcherGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static String getLocalTimeStr() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }
}
