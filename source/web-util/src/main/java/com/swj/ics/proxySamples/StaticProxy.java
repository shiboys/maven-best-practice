package com.swj.ics.proxySamples;

/**
 * Created by swj on 2017/1/3.
 */
interface Hi_interface{
    void sayHi();
}

    class Hi_interface_class implements Hi_interface
{
    @Override
    public void sayHi() {
        System.out.println(" hi from implement !");
    }
}

class Hi_interface_Proxy implements Hi_interface {
    private Hi_interface hi_interface;
    public Hi_interface_Proxy(Hi_interface hi_interface)
    {
        this.hi_interface=hi_interface;
    }
    @Override
    public void sayHi() {
        System.out.println("I am in proxy");
        this.hi_interface.sayHi();
    }
}

/**
 * 静态代理的弊端：如果需要为多个类进行代理，需要建立多个代理类，维护难度加大。
 */
public class StaticProxy {
    public static void main(String[] args)
    {
        Hi_interface_class im=new Hi_interface_class();
        Hi_interface_Proxy proxy=new Hi_interface_Proxy(im);
        proxy.sayHi();
    }
}
