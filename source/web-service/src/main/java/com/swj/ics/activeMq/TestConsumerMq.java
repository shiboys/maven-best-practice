package com.swj.ics.activeMq;

import javax.jms.MessageConsumer;

/**
 * Created by swj on 2018/7/5.
 */
public class TestConsumerMq {
    public static void main(String[] args) {
        ActiveMqConsumer activeMqConsumer = new ActiveMqConsumer();
        activeMqConsumer.init();
        TestConsumerMq testConsumerMq = new TestConsumerMq();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //启动4个线程消费
        new Thread(testConsumerMq.new ConsumerMq(activeMqConsumer)).start();
        new Thread(testConsumerMq.new ConsumerMq(activeMqConsumer)).start();
        new Thread(testConsumerMq.new ConsumerMq(activeMqConsumer)).start();
        new Thread(testConsumerMq.new ConsumerMq(activeMqConsumer)).start();
    }
    
    class ConsumerMq implements Runnable {
        private ActiveMqConsumer activeMqConsumer ;
        public ConsumerMq(ActiveMqConsumer activeMqConsumer) {
            this.activeMqConsumer = activeMqConsumer;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    activeMqConsumer.consumeMsg(ActiveMqConfig.QUEUE_NAME);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
