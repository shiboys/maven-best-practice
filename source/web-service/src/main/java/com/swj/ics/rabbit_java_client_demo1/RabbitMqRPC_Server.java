package com.swj.ics.rabbit_java_client_demo1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

/**
 * Created by swj on 2017/12/25.
 */
public class RabbitMqRPC_Server {
    
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private static final String ROUTE_KEY = "routekey_demo";
    private static final String IP_ADDRESS = "192.168.0.107";
    private static final int PORT = 5672 ; //rabbitmq 的默认端口    
    private static final String USER_NAME = "root";
    private static final String PWD = "123";
    
    public static Connection getConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PWD);
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        try {
            return connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);//设置未消费的消息最多只有一条
        System.out.println(" [x] awaiting rpc requests");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String requestNum = new String(body,"UTF-8");
                Integer facNum = Integer.valueOf(requestNum);
                System.out.println("[.] fib("+facNum+")");
                
                AMQP.BasicProperties.Builder propertiesBuilder = new AMQP.BasicProperties.Builder();
                propertiesBuilder.correlationId(properties.getCorrelationId());
                
                //propertiesBuilder.deliveryMode(2);
                
                AMQP.BasicProperties replyProperties = propertiesBuilder.build();
                
                
                String resultNum = fib(facNum)+"";
                
                channel.basicPublish("",properties.getReplyTo(),
                        replyProperties,resultNum.getBytes("UTF-8"));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        
        channel.basicConsume(RPC_QUEUE_NAME,false,consumer);
    }

    private static Integer fib(Integer facNum) {
        if (facNum <= 0) {
            return 0;
        }
        else if (facNum == 1) {
            return 1;
        }
        return fib( facNum -1) + fib(facNum - 2);
    }
    
    private static void msgTransactionModeDemo(Channel channel,String exchangeName) throws IOException {
        channel.txSelect();//开启事务
        channel.basicPublish(exchangeName,ROUTE_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,
                "transaction message".getBytes());
        channel.txCommit();//如果事务提交成功，则消息一定到达了RabbitMq中。
        //channel.txRollback(); 如果消息在提交的过程中发生了异常，我们可以在catch中进行捕获，
        //调用rollback来实现事务回滚。
        
    }

    /**
     * 普通的publisher confirm机制。
     * 普通的publisher confirm并不比 事务模式性能高很多，原因在于
     * 该种模式每发一条消息就调用channel.waitForConfirms方法，之后等待服务确认，
     * 这实际上是一种串行同步等待的方式。事务机制和它一样，发送消息之后等待确认，之后再发消息。
     * 不同的是。publisher confirm是发送一条消息，需要交互的命令是2条：Basic.Publish和Basic.Ack
     * 事务机制是3条：Basic.Publish,Tx.commit/.CommitOk 或者 Tx.Rollback/.RollbackOk
     *改进方法是如下的批量发消息，然后确认机制
     */
    private static void basicConfirmSelect(Channel channel) throws IOException {
        channel.confirmSelect(); //将信道设置为publisher confirm模式
        channel.basicPublish("exchange","routekey",
                null,"publisher confirm test".getBytes());
        try {
            if (!channel.waitForConfirms()) { //waitFormConfirm返回的条件是客户端收到了Basic.Ack
                //或者Basic.Nack或者被中断。
                System.out.println("send message failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void basicConfirmSelectBatch(Channel channel) throws IOException {
        channel.confirmSelect();
        int msgCount = 0;
        List<String> msgList = new ArrayList<>();
        while (true) {
            String msg = " batch confirm test "+msgCount;
            channel.basicPublish("exchange","routingkey",null,msg.getBytes());
            msgList.add(msg);
            if (++msgCount >= 1000) {
                try {
                    if (channel.waitForConfirms()) { //等待rabbitMq的确认返回
                        //清空消息
                        msgList.clear();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //将缓存中的消息重新发送。
                }
            }
        }
    } 
    
    //todo：展示异步confirm消息。

}
