package com.swj.ics.redis.secKill_disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Created by swj on 2018/8/9.
 */

public class DisruptorTest {
    private static volatile AtomicInteger ATOMIC_INTEGER = new AtomicInteger(); 
    //1.定义事件
    public class TradeOrder{
        private String orderId;
        private double amount;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
    //2.定义事件工厂
    public  class TradeOrderEventFactory implements EventFactory<TradeOrder> {
        @Override
        public TradeOrder newInstance() {
            return new TradeOrder();
        }
    }
    //3.定义事件处理类---订单入库操作
    public class TradeDBHandler implements EventHandler<TradeOrder>,WorkHandler<TradeOrder> {

        @Override
        public void onEvent(TradeOrder tradeOrder, long l, boolean b) throws Exception {
            this.onEvent(tradeOrder);
        }

        @Override
        public void onEvent(TradeOrder tradeOrder) throws Exception {
            tradeOrder.setOrderId(String.valueOf(ATOMIC_INTEGER.incrementAndGet()));
            System.out.println("写入DB操作，订单号为："+tradeOrder.getOrderId()+"金额:"+tradeOrder.getAmount());
        }
    }
    //3.定义事件处理类---订单消费操作
    public class TradeOrderVarConsumer implements EventHandler<TradeOrder>{
        @Override
        public void onEvent(TradeOrder event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("进行订单消费，订单金额为"+event.getAmount());
        }
    }
    //3.定义事件处理类---订单金额转换
    public class TradeOrderTrasfer implements EventTranslator<TradeOrder> {
        @Override
        public void translateTo(TradeOrder event, long sequence) {
            event.setAmount(Math.random()*99);
        }
    }
    //3.定义事件处理类---发送订单mq
    public class TradeOrderJMSSender implements EventHandler<TradeOrder>{
        @Override
        public void onEvent(TradeOrder event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("发送订单JMS:"+event.getOrderId()+",金额:"+event.getAmount());
        }
    }
    //7.发布事件
    public class TradeOrderProductor implements Runnable{
        CountDownLatch cdl;
        private final int  count= 1000;
        Disruptor disruptor;
        public  TradeOrderProductor(CountDownLatch cdl,Disruptor disruptor){
            this.disruptor =disruptor;
            this.cdl = cdl;
        }
        @Override
        public void run() {
            TradeOrderTrasfer tof;
            try {
                for (int i = 0; i < count; i++) {
                    tof = new TradeOrderTrasfer();
                    disruptor.publishEvent(tof);
                }
            }finally {
                cdl.countDown();
            }
        }
    }
    //定义RingBuffer的大小
    private static final int BUFFER_SIZE = 1024;

    //4.定义事件处理的线程或者线程池
   // ExecutorService excutorService = Executors.newFixedThreadPool(5);
 

    /** https://blog.csdn.net/jeffsmish/article/details/53572043
     * 6.通过disruptor处理器组装生产者和消费者
     * 菱形结构的处理过程流
     *
     *                                    /--------->TradeOrderVarConsumer--\
     * TradeOrderProductor--->RingBuffer-->                                   ----->TradeOrderJMSSender
     *                                    \--------->TradeDBHandler--------/
     *
     * @throws InterruptedException
     */
    public void processerFlow() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Disruptor<TradeOrder> disruptor = new Disruptor<TradeOrder>(new EventFactory<TradeOrder>(){
            @Override
            public TradeOrder newInstance() {
                return new TradeOrder();
            }
        },BUFFER_SIZE,executorService, ProducerType.SINGLE,/**5.指定等待策略**/new BusySpinWaitStrategy());

        EventHandlerGroup<TradeOrder> eventHandlerGroup = disruptor.handleEventsWith(new TradeOrderVarConsumer(),new TradeDBHandler());
        eventHandlerGroup.then(new TradeOrderJMSSender());
        disruptor.start();//启动disruptor
        CountDownLatch cdl = new CountDownLatch(1);
        executorService.submit(new TradeOrderProductor(cdl,disruptor));
        cdl.await();//用于让任务完全消费掉
        //8.关闭disruptor业务逻辑处理器
        disruptor.shutdown();
        executorService.shutdown();
    }

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        DisruptorTest disruptor = new DisruptorTest();
//        disruptor.BatchEventProcessor();
//          disruptor.workerPool();
        disruptor.processerFlow();
    }
}
