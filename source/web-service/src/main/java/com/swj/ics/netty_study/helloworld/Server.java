package com.swj.ics.netty_study.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by swj on 2017/8/9.
 */
public class Server {
    public static void main(String[] args) {
        //创建2个线程池
        //1个用来接收客户端请求，
        //另一个用来用来网络通信，处理网络读写
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup gGroup = new NioEventLoopGroup();

        //创建一个辅助工具类，用于服务器通道的一系列配置。
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(pGroup,gGroup) //绑定2个线程池。gGorup:parentGroup,gGroup:childGroup
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG,1024) //设置tcp的缓冲池
        .option(ChannelOption.SO_RCVBUF,32 * 1024)
        .option(ChannelOption.SO_SNDBUF,32 * 1024)
        .option(ChannelOption.SO_KEEPALIVE,true)//保持连接
        .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                //这里配置具体的数据接收方案。
                ch.pipeline().addLast(new ServerHandler());
            }
        });

        //4 进行绑定端口
        try {
           ChannelFuture channelFuture = bootstrap.bind(8888).sync();
            //5等待关闭
            //在Netty中，凡是带Future的都是异步的
            //绑定多个端口，进行多端口监听
           // ChannelFuture channelFuture2 = bootstrap.bind(8080);

            channelFuture.channel().closeFuture().sync();
          //  channelFuture2.channel().closeFuture().sync();
            System.out.println("server is started !");
            pGroup.shutdownGracefully();
            gGroup.shutdownGracefully();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
