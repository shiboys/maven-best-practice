package com.swj.ics.netty_study.timeServerNettyEncodeDecode;

import com.swj.ics.netty_study.utils.NettyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by swj on 2018/6/18.
 */
public class ServerClass {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        
        serverBootstrap.group(mainGroup,workerGroup);
        
        serverBootstrap.channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                       // socketChannel.pipeline().addLast(new ServerTimeHandlerNoDecoder());
                      
                        //每次的消息独立体不能大于1024
                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ServerTimeHandlerNoDecoder());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(NettyUtils.PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                mainGroup.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                workerGroup.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


