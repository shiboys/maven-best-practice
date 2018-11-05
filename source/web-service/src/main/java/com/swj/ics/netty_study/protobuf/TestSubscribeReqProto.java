package com.swj.ics.netty_study.protobuf;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * author shiweijie
 * date 2018/11/3 下午6:08
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq subscribeReq) {
        return subscribeReq.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] bytesArray)
            throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(bytesArray);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setSubReqId(1);
        builder.setUsername("shiboys");
        builder.setProductName("Netty Study");
        List<String> addressList = new ArrayList<>();
        addressList.add("BeiJing city");
        addressList.add("ChaoYang district");
        addressList.add("ChaoWai street");

        builder.addAllAddressList(addressList);

        return builder.build();
    }

    public static void main(String[] args) {
        SubscribeReqProto.SubscribeReq subscribeReq = createSubscribeReq();
        System.out.println("before encode : " + subscribeReq);

        try {
            SubscribeReqProto.SubscribeReq subscribeReq2 = decode(encode(subscribeReq));
            System.out.printf("after decode :" + subscribeReq2);
            System.out.println("assert are equal :" + subscribeReq.equals(subscribeReq2));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


    }
}
