package com.swj.ics.rabbit_java_client_demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.UUID;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by swj on 2017/12/25.
 */
public class RabbitMqRPC_Client {
    private static final String IP_ADDRESS = "192.168.0.107";
    private static final int PORT = 5672 ; //rabbitmq 的默认端口    
    private static final String USER_NAME = "root";
    private static final String PWD = "123";
    
    private Connection connection;
    private Channel channel;
    private QueueingConsumer queueingConsumer;
    private String replyQueueName;
    private String requestQueueName="rpc_queue";
    
    public RabbitMqRPC_Client() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PWD);
        Address[] addresses = new Address[] {
                new Address(IP_ADDRESS,PORT)
        };
       
        try {
            connection = connectionFactory.newConnection(addresses);
            channel = connection.createChannel();
            
            replyQueueName = channel.queueDeclare().getQueue();
            System.out.println("replyQueueName is " + replyQueueName);
            queueingConsumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName,false,queueingConsumer);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用远程方法
     * @param inputVal
     * @return
     */
    public String callRemoeRPC_Method(String inputVal) throws IOException, InterruptedException {
        
        String correlationId = UUID.randomUUID().toString();
        
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();
         
        channel.basicPublish("",requestQueueName,properties,inputVal.getBytes());
        Random random = new Random();
        //使用 while(true)循环等待服务端的调用
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                String response = new String(delivery.getBody());
                return response;
            }
        }
    }
    
    public void close() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitMqRPC_Client client = new RabbitMqRPC_Client();
        int fibNum = 30;
        String inputValue = fibNum + "" ;
        System.out.println("远程rpc调用 fib(" + inputValue + ")");
        
        String result = client.callRemoeRPC_Method(inputValue);
        System.out.println("调用结果是："+result);

        BufferedReader bfReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入 fib(x) 函数的参数，exit退出");
        String read = bfReader.readLine();
        while (!read.equalsIgnoreCase("exit")) {
            if (!StringUtils.isNumeric(read)) {
                System.out.println("请输入数字或者exit");
            } else {
                result = client.callRemoeRPC_Method(read);
                System.out.println(String.format("fib(%s) = %s ",read,result));
            }
            read = bfReader.readLine();
        }      
        
        client.close();
    }
}
