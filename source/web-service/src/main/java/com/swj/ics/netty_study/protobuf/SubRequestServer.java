package com.swj.ics.netty_study.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * author shiweijie
 * date 2018/11/5 上午8:59
 */
public class SubRequestServer {

    public void bind(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //启动一个线程辅助类，
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //绑定线程池和设置处理器
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        super.exceptionCaught(ctx, cause);
                    }

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //1、添加ProtoBuf 半包和解码器
                        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        socketChannel.pipeline().addLast(
                                new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));

                        //添加编码器 服务端发送消息给客户端使用
                        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        socketChannel.pipeline().addLast(new ProtobufEncoder());

                        socketChannel.pipeline().addLast(new ServerProtoBufMsgHandler());
                    }
                });

        try {
            //绑定端口，同步等待成功
            ChannelFuture cf= serverBootstrap.bind(port).sync();
            System.out.println("bind the port : " + port +" success !");
            //等待服务端 监听关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        int port = 8080;
        new SubRequestServer().bind(port);
    }
}
