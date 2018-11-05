package com.swj.ics.netty_study.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * author shiweijie
 * date 2018/11/5 上午9:37
 */
public class ServerProtoBufMsgHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq subscribeReq = (SubscribeReqProto.SubscribeReq) msg;
        if (subscribeReq == null) {
            ctx.writeAndFlush(responseNull());
            return;
        }

        System.out.println("receive request from client : " + subscribeReq);


        if ("shiboys".equals(subscribeReq.getUsername())) {

            ctx.writeAndFlush(responseWithMessage(subscribeReq.getSubReqId()));
        } else {
            ctx.writeAndFlush(responseNull());
        }
    }

    private SubscribeRespProto.SubscribeResp responseNull() {
        return getResponseBuilder(null).build();
    }

    private SubscribeRespProto.SubscribeResp responseWithMessage(Integer reqId) {
        return getResponseBuilder(reqId).build();
    }

    private SubscribeRespProto.SubscribeResp.Builder getResponseBuilder(Integer reqId) {
        SubscribeRespProto.SubscribeResp.Builder builder
                = SubscribeRespProto.SubscribeResp.newBuilder();
        if(reqId == null) {
            builder.setSubReqId(0);
            builder.setDesc(" request object is null or request is illegal");
        } else {
            builder.setSubReqId(reqId.intValue());
            builder.setDesc("netty protobuf usage studying...");
        }

        builder.setRespCode(0);
        return builder ;
    }
}
