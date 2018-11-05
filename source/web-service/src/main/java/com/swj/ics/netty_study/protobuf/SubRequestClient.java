package com.swj.ics.netty_study.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * author shiweijie
 * date 2018/11/5 上午9:57
 */
public class SubRequestClient {

    public void bind(int port,String host) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        socketChannel.pipeline().addLast(
                                new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));

                        //解码
                        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        socketChannel.pipeline().addLast(new ProtobufEncoder());

                        socketChannel.pipeline().addLast(new SubRequestClientHandler());
                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new SubRequestClient().bind(8080,"localhost");
    }
}
