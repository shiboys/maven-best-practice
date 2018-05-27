package com.swj.ics.proxySamples;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by swj on 2017/1/3.
 * jdk的动态代理的原理也是回调函数，回调invoke
 * CGLib需要指定父类和回调方法。当然cglib也可以和java动态代理一样面向接口，因为本质是继承。
 */
class CGLibClass
{
    public String sayHi()
    {
        return "welcome to view the cglib demo1";
    }
}
class CGLibClassProxy
{
    private  Object object;
    public Object bind(CGLibClass target)
    {
        this.object=target;
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(CGLibClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("i'm in cglib proxy");
                return method.invoke(object,args);
            }
        });
        return enhancer.create();
    }
}

public class CGlibProxy {
    public  static void main(String[] args)
    {
        CGLibClass target=new CGLibClass();
        CGLibClassProxy cglibProxy=new CGLibClassProxy();
        CGLibClass proxy=(CGLibClass)cglibProxy.bind(target);
        String result= proxy.sayHi();
        System.out.println(result);
    }
}
