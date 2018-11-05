package com.swj.ics.netty_study.protobuf;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * author shiweijie
 * date 2018/11/5 上午10:39
 */
public class SubRequestClientHandler extends   ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for(int i =0;i < 10 ;i++) {
            ctx.writeAndFlush(getReqById(i));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive server message : [" + msg +"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq getReqById(int i) {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setUsername("shiboys");
        builder.setProductName("netty study book");
        builder.setSubReqId(i);
        List<String> addressList = new ArrayList<>();

        addressList.add("BeiJing City");
        addressList.add("ChaoYang District");

        builder.addAllAddressList(addressList);

        return builder.build();
    }
}

