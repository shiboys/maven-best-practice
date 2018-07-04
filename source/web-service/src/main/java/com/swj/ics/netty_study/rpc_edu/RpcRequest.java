package com.swj.ics.netty_study.rpc_edu;

import java.io.Serializable;

/**
 * Created by swj on 2018/6/10.
 */
public class RpcRequest implements Serializable{
    
    private static final long serialVersionUID = -521763800470377604L;
    private String className;
    private String methodName;
    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
