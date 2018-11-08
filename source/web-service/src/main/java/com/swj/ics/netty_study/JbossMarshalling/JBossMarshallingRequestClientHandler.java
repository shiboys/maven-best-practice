package com.swj.ics.netty_study.JbossMarshalling;

import java.util.ArrayList;
import java.util.List;

import com.swj.ics.netty_study.protobuf.SubscribeReqProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * author shiweijie
 * date 2018/11/5 上午10:39
 */
public class JBossMarshallingRequestClientHandler extends   ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for(int i =0;i < 10 ;i++) {
            ctx.write(getReqById(i));
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive server jboss marshalling message : [" + msg +"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private  SubscribeReq getReqById(int i) {
        SubscribeReq req = new SubscribeReq();
        req.setUserName("shiboys");
        req.setProductName("netty study book");
        req.setSubReqID(i);


        req.setAddress("BeiJing City,ChaoYang District");

        return req;
    }
}

