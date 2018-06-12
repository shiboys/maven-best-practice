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
        //创建2个NioEventLoopGroup实例
        //NioEventGroup是个线程组，它包含一组NIO线程，专门用来网络事件的处理，实际上它们就是Reactor线程组。
        //这里创建2个的原因是一个用于服务器端接受客户端的连接，另一个用于进行SocketChannel的网络读写。
        //
        NioEventLoopGroup watcherGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //ServerBootstrap对象是Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度。
        ServerBootstrap sbp = new ServerBootstrap();
        //这里调用group方法，将2个NIO线程组当做人参传递到ServerBootStrap中。接着设置channel为NIOServerSocketChannel。它的功能对应于JDK的 NIO中的
        //ServerSocketChannel类。
        sbp.group(watcherGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024) //这里配置TCP参数，此处将backlog设置为1024,关于backlog的参数注释如下：
                .childHandler(new ChildChannelHandler()); //这里绑定IO时间处理类ChildChannelHandler,它的作用类似于Reactor模式中的Handler类，
        // 主要用于处理网络I/O事件，例如记录日志，对消息进行编解码
                /*
                *ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
　　　　服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，
服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小 
                * */
        try {
            //绑定端口，进行监听，等待绑定成功。调用sync同步阻塞方法等待绑定操作的完成。完成之后，Netty会返回一个ChannelFuture,
            //它的功能类似于JDK的java.util.concurrent.Future,主要用于异步操作的通知回调。
            ChannelFuture channelFuture = sbp.bind(port).sync();

            //等待服务端的监听端口关闭，等待服务端链路关闭之后，main函数才会退出
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
