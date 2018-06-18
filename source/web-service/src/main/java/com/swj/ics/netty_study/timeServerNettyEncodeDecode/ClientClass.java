package com.swj.ics.netty_study.timeServerNettyEncodeDecode;

import com.swj.ics.netty_study.utils.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by swj on 2018/6/18.
 */
public class ClientClass {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                       // socketChannel.pipeline().addLast(new ClientTimeHandlerNoDecoder()); 
                        //StringDecoder+LineBasedFrameDecoder 就是按行切换的文本解码器，它被用来设计支持TCP拆包粘包
                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ClientTimeHandlerNoDecoder());
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", NettyUtils.PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                //优雅退出，释放线程池
                group.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
