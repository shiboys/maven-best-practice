package com.swj.ics.activeMq;

import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Created by swj on 2018/7/4.
 */
public class ActiveMqConsumer {
    private ConnectionFactory connectionFactory ;
    private Connection connection;
    private Session session ;
    private ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();
    private AtomicInteger consumerCounter = new AtomicInteger(0);
    public void init() {
        connectionFactory = new ActiveMQConnectionFactory(ActiveMqConfig.USER_NAME,
                ActiveMqConfig.PASSWORD,ActiveMqConfig.BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            //客户端不需要开启事务
            connection.start();
            session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public void consumeMsg(String queueName) {
        try {
            Queue queue = session.createQueue(queueName);
            MessageConsumer messageConsumer = threadLocal.get();
            if (messageConsumer == null) {
                messageConsumer = session.createConsumer(queue);
                threadLocal.set(messageConsumer);
            }
            while (true) {
                Thread.sleep(1000);//每隔1秒消费1条消息
                TextMessage textMessage = (TextMessage)messageConsumer.receive();//阻塞方法
                if(textMessage != null) {
                    textMessage.acknowledge();
                    System.out.println("consumer consume msg :" + textMessage.getText()
                            +",consumer counter is " + consumerCounter.getAndIncrement());
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
