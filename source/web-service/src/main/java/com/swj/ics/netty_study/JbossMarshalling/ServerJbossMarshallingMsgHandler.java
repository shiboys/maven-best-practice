package com.swj.ics.netty_study.JbossMarshalling;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * author shiweijie
 * date 2018/11/5 上午9:37
 */
public class ServerJbossMarshallingMsgHandler extends ChannelHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channel active : " );

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("server channel read : " );

        SubscribeReq subscribeReq = ( SubscribeReq) msg;
        if (subscribeReq == null) {
            ctx.writeAndFlush(responseNull());
            return;
        }

        System.out.println("receive request from client : " + subscribeReq);


        if ("shiboys".equals(subscribeReq.getUserName())) {

            ctx.writeAndFlush(responseWithMessage(subscribeReq.getSubReqID()));
        }    else {
            ctx.writeAndFlush(responseNull());
        }
    }

    private  SubscribeResp responseNull() {
        return getResponseInstance(null);
    }

    private SubscribeResp responseWithMessage(Integer reqId) {
        return getResponseInstance(reqId);
    }

    private SubscribeResp getResponseInstance(Integer reqId) {
        SubscribeResp resp = new SubscribeResp();
        if(reqId == null) {
            resp.setSubReqID(0);
            resp.setDesc(" request object is null or request is illegal");
        } else {
            resp.setSubReqID(reqId.intValue());
            resp.setDesc("netty protobuf usage studying...");
        }

        resp.setRespCode(0);
        return resp ;
    }
}
