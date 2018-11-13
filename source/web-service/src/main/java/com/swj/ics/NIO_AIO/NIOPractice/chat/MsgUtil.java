package com.swj.ics.NIO_AIO.NIOPractice.chat;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author shiweijie
 * date 2018/11/12 下午9:50
 */
public class MsgUtil {

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public static final Logger logger = LoggerFactory.getLogger(MsgUtil.class);

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

    public static Message parseMessage(String expression) {
        if (!msgCheck(expression)) {
            logger.error("invalid expression" + expression);
            return null;
        }
        String[] splitArr = expression.split("\\|");
        Message message = new Message();
        message.setFlag(splitArr[0]);

        message.setMsgFrom(getUserInfo(splitArr[1]));
        message.setGroupId(splitArr[2]);

        List<UserInfo> toUserList = new ArrayList<>();
        String[] toArr = splitArr[3].split(",");
        for(String toStr : toArr) {
            toUserList.add(getUserInfo(toStr));
        }

        message.setMsgToList(toUserList);
        message.setContent(splitArr[4]);

        return message;
    }

    public static Message parseMessage(ByteBuffer byteBuffer) {
        CharBuffer charBuffer = CHARSET_UTF8.decode(byteBuffer);
        String msg = new String(charBuffer.array());
        System.out.println("收到消息：" + msg);
        return parseMessage(msg);
    }

    private static boolean msgCheck(String expression) {
        if (expression == null || "".equals(expression.trim())) {
            return false;
        }
        String[] splitArr = expression.split("\\|");
        if(splitArr.length < 5) {
            return false;
        }
        return true;
    }


}
