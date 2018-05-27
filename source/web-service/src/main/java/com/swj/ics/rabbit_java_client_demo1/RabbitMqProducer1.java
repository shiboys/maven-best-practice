package com.swj.ics.rabbit_java_client_demo1;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by swj on 2017/12/15.
 */
public class RabbitMqProducer1 {
     private static  final String EXCHANGE_NAME = "exchange_demo";
     private static final String QUEUE_NAME = "queue_demo";
     private static final String ROUTE_KEY = "routekey_demo";
     private static final String IP_ADDRESS = "192.168.0.107";
     private static final int PORT = 5672 ; //rabbitmq 的默认端口    
     private static final String USER_NAME = "root";
     private static final String PWD = "123";
    
    public static void  main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PWD);

        try {
            //创建连接
            Connection connection = connectionFactory.newConnection();
            //创建一个channel
            Channel channel = connection.createChannel();
            //创建个Type ="direct"，持久化的，非自动删除删除的交换器
            channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
            //创建一个 持久化，非排他的，非自动删除的队列
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            
            //将交换器 和 队列 通过路由绑定
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTE_KEY);
            //发送一条持久化消息：hello,world
            String msg = "Hello World!";
            //发布一条持久化的消息
            channel.basicPublish(EXCHANGE_NAME,
                    ROUTE_KEY, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    msg.getBytes());
            
            //关闭资源
            channel.close();
            connection.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        finally {
            //关闭资源
        }
    }
    
    
    // bitmq 
     
}
