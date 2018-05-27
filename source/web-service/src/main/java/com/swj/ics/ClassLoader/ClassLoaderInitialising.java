package com.swj.ics.ClassLoader;

import java.util.Random;

/**
 * Created by swj on 2018/5/5.
 */
public class ClassLoaderInitialising {
    
    public static void main(String[] args) {
      //  new Obj();//主动使用方式1 会调用类的静态代码块。
        
        
     //  System.out.println(MyInterface.a);
        //MyInterface.a = 1; 接口的程序不能被写，默认是final的
        //
       // System.out.println(Obj.salary);//对某个类的静态变量进行读写 回导致静态代码块中的代码被执行
        
      //  Obj.printSalary();//调用静态方法，也会初始化类

       /* try {
            Class.forName("com.swj.ics.ClassLoader.Obj");//反射调用类，也会造成类的初始化
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
       
     //  System.out.println(Child.age);//初始化一个子类 会导致父类初始化
        //用子类调用父类的静态变量，父类会被初始化，但是子类不会被初始化，这是类的被动引用一部分
       // System.out.println(Child.salary);
        /**
         * Class Obj initialize..
         100000
         */
        //通过数组引用，不会导致类的初始化
      //  Obj[] objs = new Obj[10];
        
        //编译时常量，也即是引用常量池的常量类型，不会导致初类始化
    //    System.out.println(Obj.x);
        System.out.println(Obj.randomX);//运行时的常量，类仍然会被初始化
    }
}

class Obj {
    
    public static final int x = 100;
    public static final int randomX = new Random().nextInt(100);
    
    public static long salary = 100000L;//对某个类的静态变量进行读写 回导致静态代码块中的代码被执行
    
    static {
        System.out.println("Class Obj initialize..");
    }
    
    public static void printSalary() {
        System.out.println("**************obj salary***********");
    }
}

class Child extends Obj {
    
    public static int age = 35;
    static {
        System.out.println("Child is initialising...");
    }
}

interface MyInterface {
    int a = 10;
}

 //访问某个类或者接口的静态变量，或者对静态变量进行赋值操作
// 对某个类的静态变量进行读写
//对接口中的变量进行读取 
//以上2步操作
