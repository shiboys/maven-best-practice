package com.swj.ics.basicPractice;

/**
 * author shiweijie
 * date 2018/8/7 下午4:20
 */
public class String2Integer {

    public static Integer convertToIntegerFromString(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("str is empty");
        }
        char[] chars = str.toCharArray();
        int result = 0;
        //char c = '0';
       /* for(int i = chars.length - 1;i>=0 ;i--) {
            char c = chars[i];
            if(c >= '0' && c <= '9') {
                int diff = c - '0';
               result = diff * (int)Math.pow(10,chars.length - 1 - i) + result;
            } else {
                throw new RuntimeException(str + "contains invalid number char");
            }
        }*/

       for(char c : chars) {
           if(c >= '0' && c <= '9') {
               int diff = c - '0';
               result = result * 10 + diff;
           } else {
               throw new RuntimeException(str + "contains invalid number char");
           }

       }
        return result;
    }

    public static void main(String[] args) {
        String str="1234";
        Integer val = convertToIntegerFromString(str);
        System.out.println(val );
        System.out.println(val == 1234 );
    }


}
