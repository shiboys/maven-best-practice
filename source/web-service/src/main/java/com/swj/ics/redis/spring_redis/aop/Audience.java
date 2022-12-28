package com.swj.ics.redis.spring_redis.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by swj on 2017/1/5.
 */
public class Audience {
    /**
     * 定义明明的切点
     */
    @Pointcut("execution(**com.swj.ics.redis.spring_redis.aop.IPerformance.perform(..))")
   public void performance(){
   };

    /**
     * 在表演之前
     */
   @Before("performance()")
   public void silenceCellPhone()
   {
        System.out.println("Silencing the cell phone");
   }

   @Before("performance()")
   public void takeSeats()
   {
        System.out.println("taking seats");
   }

   @AfterReturning("performance()")
   public void applause()
   {
       System.out.println("Clap,Clap,Clap");
   }

    /**
     * 表演失败之后要求退费
     */
    @AfterThrowing("performance()")
   public void demandRefund()
   {
       System.out.println("demanding refund!!!");
   }

}
