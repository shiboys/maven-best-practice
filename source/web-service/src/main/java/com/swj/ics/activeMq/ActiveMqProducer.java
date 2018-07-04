package com.swj.ics.activeMq;

import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Created by swj on 2018/7/4.
 */
public class ActiveMqProducer {
    //连接工厂
    private ConnectionFactory connectionFactory;
    //连接对象
    private Connection connection;
    //事务、回话
    private Session session;
    
    ThreadLocal<MessageProducer> messageProducerThreadLocal;
    AtomicInteger atomicCounter = new AtomicInteger(0);
    public void init() {
        connectionFactory = new ActiveMQConnectionFactory(ActiveMqConfig.USER_NAME,
                ActiveMqConfig.PASSWORD,ActiveMqConfig.BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            //创建一个带事务的回话
            session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void sendMessage(String queueName) {
        try {
             Queue queue = session.createQueue(queueName);
             MessageProducer messageProducer = null;
             if (messageProducerThreadLocal.get() != null) {
                 messageProducer = messageProducerThreadLocal.get();
             } else {
                 messageProducer = session.createProducer(queue);
                 messageProducerThreadLocal.set(messageProducer);
             }
             while (true) {
                 Thread.sleep(1000);//每一秒产生一条消息
                 String msg = "this is the msg from producer-" + Thread.currentThread().getName() +
                         ",counter is " + atomicCounter.getAndIncrement();
                 TextMessage textMessage = session.createTextMessage(msg);
                 messageProducer.send(textMessage);
                 session.commit();
             }
             
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
