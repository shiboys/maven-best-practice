package com.swj.ics.proxySamples;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by swj on 2017/1/3.
 */
interface Hi_dynamic_interface
{
    void sayHi();
}

class Hi_dynamic_interface_implement implements Hi_dynamic_interface
{
    @Override
    public void sayHi() {
        System.out.println("hi from impl");
    }
}

class Hi_dynamic_proxy implements InvocationHandler
{
    private  Object target;

    public Object bind(Object obj)
    {
        this.target=obj;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("I'm in proxy");
        Object result=method.invoke(this.target,args);
        return result;
    }
}

/**
 *对比静态代理，可以发现动态代理不需要实现业务接口了，只需要实现代理接口InvocationHandler就可以了。
 * 通过   Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this) 实现的代理类
 * 该类的类加载器和被代理类的类加载器相同，实现的接口与被代理的接口相同。这样就实现了在运行期才决定代理对象是长什么样。
 *
 */
public class DynamicProxy {
    public static void main(String[] args)
    {
        Hi_dynamic_interface_implement im=new Hi_dynamic_interface_implement();
        Hi_dynamic_proxy proxy=new Hi_dynamic_proxy();
        Hi_dynamic_interface hi=(Hi_dynamic_interface)proxy.bind(im);
        hi.sayHi();
    }
    /**
     * 动态代理的弊端：
     * 代理类与委托类都需要实现同一个接口。也就是说只有实现了某个业务接口的类才可以使用Java动态代理机制。
     * 但是事实上，并非所有遇到的类都会给你一个需要实现的接口。因此对于没有接口的实现类，就不能使用该机制。
     * 而cglib则可以实现对类的动态代理。
     */
    /**
     * 动态代理与静态代理的区别：
     * 1、Proxy类的代码被固定下来，不会因为业务的逐渐庞大而庞大。
     * 2、可以实现AOP编程，这是静态代理无法实现的。
     *3、动态代理的优势是实现无侵入式的代码扩展。
     */

}
