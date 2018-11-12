package com.swj.ics.NIO_AIO.NIOPractice.chat;

/**
 * author shiweijie
 * date 2018/11/12 下午9:50
 */
public class MsgUtil {

    public static UserInfo getUserInfo(String exp) {

        String[] splitArr = exp.split("=");

        String[] addressInfoArr = splitArr[0].split(":");

        UserInfo userInfo = new UserInfo() {{
            setIp(addressInfoArr[0]);
            setName(splitArr[1]);
            setPort(Integer.parseInt(addressInfoArr[1]));
        }};

        return userInfo;
    }


}
