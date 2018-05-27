package com.swj.ics.Algorithum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swj on 2018/3/21.
 */
public class MyMath {
    
    public static void main(String[] args) {
        //printAllZhiShu(101,200);
        //shuiXianHua();
        
        for (int i=90 ; i <= 100;i++) {
            System.out.print(i + "=");
            printSeperateZhiShu(i,false);
            System.out.println();
        }
    }
    
    static void printAllZhiShu(int start,int end) {
        boolean isZhiShu = false;
        List<Integer> list = new ArrayList<>((end - start) / 2 );
        for (int i = start;i < end; i++) {
            isZhiShu = isZhiShu(i);
            if (isZhiShu) {
                list.add(i);
            }
        }
        System.out.println(String.format("%d 到 %d 之间的质数个数为 %d",start,end,list.size() ));
        System.out.println("所有的质数如下："+ list);
    }
    
    static boolean isZhiShu(int num) {
        if(num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        
        int middle = num;
        int sqrtNum = (int) Math.sqrt(num);
        boolean isZhiShu = true;
        for (int i= 2;i <= sqrtNum;i++) {
            if (num % i ==0) {
                isZhiShu = false;
                break;
            }
        }
        return isZhiShu;
        
    }

    /**
     * 水仙花 153 = 1^3 + 5^3 + 3^3 .100-999之前的水仙花书面
     */
    static void shuiXianHua() {
        int p,t,s = 0;
        List<Integer> correctList = new ArrayList<>(10);
        for (int i=100;i < 1000 ;i++) {
            p = i /100;
            t = i % 100 /10;
            s = i % 10 ;
            
            if ((p * p * p + t*t*t + s*s*s) == i) {
                correctList.add(i);
            }
        }
        System.out.println("list is " + correctList);
    }
    
    //分级质因数 90 = 2 *3 *3 *5
    static void printSeperateZhiShu(int num,Boolean isFinish) {
        if (isFinish ) {
            return ;
        }
        if (num < 2) {
            return ;
        }
       // 
        for (int i= 2; i<= num/2;i++ ) { 
            if (num % i == 0) { //能整除
                //继续分
                System.out.print(i + "*");
                printSeperateZhiShu(num / i,isFinish);
            }
        }
        if (isFinish ) {
            return ;
        }
        //质数
        System.out.print(num);
        isFinish = true;
    }
}

