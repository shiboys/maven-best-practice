package com.swj.ics.netty_study.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by swj on 2018/6/18.
 */
public class NettyUtils {
    
    public static final int PORT = 8888;
    
    public static String getNowTimeStr() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }
}
