package com.swj.ics.netty_study.JbossMarshalling;

import com.swj.ics.netty_study.protobuf.SubRequestClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * author shiweijie
 * date 2018/11/6 上午9:39
 */
public class JBossMarshallingClient {
    public void bind(int port,String host) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)

                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(JBossMarshallingFactory.buildMarshallingDecoder());
                        socketChannel.pipeline().addLast(JBossMarshallingFactory.buildMarshallingEncoder());

                        socketChannel.pipeline().addLast(new JBossMarshallingRequestClientHandler());
                    }
                });


        try {
            ChannelFuture channelFuture=  bootstrap.connect(host,port).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
