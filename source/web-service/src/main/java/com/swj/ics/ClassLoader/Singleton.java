package com.swj.ics.ClassLoader;

/**
 * Created by swj on 2018/5/5.
 */
public class Singleton {
    //instance如果在这里，输出为x=0,y=1,因为 instance初始化之后，x=1,y=1
    //可以，下面的赋值语句将x 设置为0，因此出现x=0,y=1;
    private static Singleton instance = new Singleton();
    
    public static int x = 0;
    public static int y;

    private Singleton() {
        x++;
        y++;
    }
// instance初始化在这里的的话，输出x1=,y=1
   // private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Singleton singleton = getInstance();
        System.out.println("x=" + singleton.x + ",y=" + singleton.y);
    }
}
