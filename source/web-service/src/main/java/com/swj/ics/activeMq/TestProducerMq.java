package com.swj.ics.activeMq;

/**
 * Created by swj on 2018/7/5.
 */
public class TestProducerMq {

    public static void main(String[] args) {
        ActiveMqProducer producer = new ActiveMqProducer();
        producer.init();
        //等待1秒钟，让producer连接上activemq服务器
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestProducerMq testProducerMq = new TestProducerMq();
        
        //开启5个线程来生产消息
        new Thread(testProducerMq.new ProducerMq(producer)).start();
        new Thread(testProducerMq.new ProducerMq(producer)).start();
        new Thread(testProducerMq.new ProducerMq(producer)).start();
        new Thread(testProducerMq.new ProducerMq(producer)).start();
        new Thread(testProducerMq.new ProducerMq(producer)).start();
    }
    
    
    class ProducerMq implements Runnable {
        
        private ActiveMqProducer producer ;
        
        public ProducerMq(ActiveMqProducer producer) {
            this.producer = producer;
        }
        @Override
        public void run() {
            //每隔10秒发一下条消息
            while (true) {
                producer.sendMessage(ActiveMqConfig.QUEUE_NAME);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
