package com.swj.ics.basicPractice;

/**
 * Created by swj on 2018/3/6.
 */
public class InboxAndUnBox {
    
    public static void main(String[] args) {
        testIntegerWrap1();
    }
    
    static void testIntegerWrap1() {
        Integer a = new Integer(3);
        Integer b = 3;
        int c = 3;
        Integer d = 3;
        /*
        * 结果为 false,true
        * */
        System.out.println(a == b);
        System.out.println(a == c);
        println(b == d); 
    }
    
    
    static void println(Object arg) {
        System.out.println(arg);
    }
    
}
