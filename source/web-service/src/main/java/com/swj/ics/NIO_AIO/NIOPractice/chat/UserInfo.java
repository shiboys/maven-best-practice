package com.swj.ics.NIO_AIO.NIOPractice.chat;

import java.util.Objects;

/**
 * author shiweijie
 * date 2018/11/12 下午9:04
 */
public class UserInfo {

    private String ip;
    private int port;
    private String name;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return port == userInfo.port &&
                Objects.equals(ip, userInfo.ip) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, name);
    }

    @Override
    public String toString() {
        return ip + ":" + port + "=" + name;
    }
}
