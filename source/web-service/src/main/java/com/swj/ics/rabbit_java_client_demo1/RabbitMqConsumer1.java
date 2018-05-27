package com.swj.ics.rabbit_java_client_demo1;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by swj on 2017/12/15.
 */
public class RabbitMqConsumer1 {
    private static  final String EXCHANGE_NAME = "exchange_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.0.107";
    private static final int PORT = 5672 ; //rabbitmq 的默认端口    
    private static final String USER_NAME = "root";
    private static final String PWD = "123";
    
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[] {
              new Address(IP_ADDRESS,PORT)  
        };

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PWD);
        //这里就是消费者和生产者的使用的参数不同了
        Connection connection = connectionFactory.newConnection(addresses);
        Channel channel = connection.createChannel();
        //设置客户端最多未被 ack 的消息个数
        channel.basicQos(64);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, 
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("recv message:" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //这段话我还不太理解 
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        
        channel.basicConsume(QUEUE_NAME,consumer);
        
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
    
}
