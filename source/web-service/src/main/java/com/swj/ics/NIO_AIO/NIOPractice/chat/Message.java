package com.swj.ics.NIO_AIO.NIOPractice.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author shiweijie
 * date 2018/11/12 下午9:03
 *
 * 聊天系统消息类：
 * 消息的格式：消息类型|消息来源|消息的群组号|消息的接收人|消息内容。
 * 消息类型：0-上线，1-下线，2-点对点消息，3-组内消息，4-群发消息。
 * 消息来源：地址：端口=用户昵称
 * 消息的群组号：groupID(点对点或者群发) = 0，组内消息groupId > 0
 * 消息的接收人：地址1:端口1=用户昵称1，地址2:端口2=用户昵称2，地址3:端口3=用户昵称3。。。。
 * 消息内容：如果消息过长，则分批次发送。
 *
 */

public class Message {
    private String flag;//消息的类型 0-上线，1-下线，2-点对点消息，3-组内消息，4-群发消息

    private UserInfo msgFrom;//消息来源

    /*
    List<String:ip:port>
     */
    private List<UserInfo> msgToList;//消息的接收人

    //消息群组号
    private String groupId;

    private String content;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public UserInfo getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(UserInfo msgFrom) {
        this.msgFrom = msgFrom;
    }

    public List<UserInfo> getMsgToList() {
        return msgToList;
    }

    public void setMsgToList(List<UserInfo> msgToList) {
        this.msgToList = msgToList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(String flag, UserInfo msgFrom, List<UserInfo> msgToList, String groupId, String content) {
        this.flag = flag;
        this.msgFrom = msgFrom;
        this.msgToList = msgToList;
        this.groupId = groupId;
        this.content = content;
    }

    public Message() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(flag);
        sb.append("|");
        sb.append(msgFrom);
        sb.append("|");
        sb.append(groupId);
        sb.append("|");

        sb.append(msgToList);
        sb.append("|");
        sb.append(content);

        return sb.toString();
    }


}
