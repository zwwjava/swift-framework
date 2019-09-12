package org.swift.framework.helper.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import org.swift.framework.util.log.SwiftLogger;
import java.lang.reflect.Method;

import java.io.IOException;

/**
 * @Description - mq 助手，对外提供功能
 * @Author zww
 * @Date 2019/9/11
 */
public class RabbitTemplate {

    private static ThreadLocal<Channel> CHANNEL_REGISTER = new ThreadLocal<Channel>();

    /**
     * DIRECT("direct"), FANOUT("fanout"), TOPIC("topic"), HEADERS("headers");
     */
    private static String defaultExchangeType = "direct";

    /**
     * 发送MQ 信息
     * @param exchangeName
     * @param queueName
     * @param object
     */
    public static void sendMq(String exchangeName, String queueName, Object object) {
        try {
            getChannel().basicPublish(exchangeName, queueName, null, JSONObject.toJSONString(object).getBytes("UTF-8"));
        } catch (IOException e) {
            SwiftLogger.error("发送MQ 信息时出现异常");
        }
    }

    /**
     * 监听 MQ 信息，执行代理方法。
     * @param queueName 队列名称
     * @param object 执行的对象
     * @param method 执行对象的方法
     */
    public static void receiveMq(String queueName, Object object, Method method) {
        try {
            Channel channel = getChannel();
            //声明要关注的队列
            channel.queueDeclare(queueName, false, false, false, null);
            SwiftLogger.info("Customer Waiting Received messages");
            //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
            // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    try {
                        method.invoke(object, body);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Customer Received '" + message + "'");
                }
            };
            //自动回复队列应答 -- RabbitMQ中的消息确认机制
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            SwiftLogger.error("接收mq消息 时出现异常");
        }
    }

    /**
     * 队列绑定交换机
     * @param queueName
     * @param exchangeName
     */
    public static void declareBing(String exchangeName, String queueName) {
        declareBing(exchangeName, queueName, queueName);
    }

    /**
     * 队列绑定交换机
     * @param queueName
     * @param exchangeName
     * @param rountingKey
     */
    public static void declareBing(String exchangeName, String queueName, String rountingKey) {
        try {
            getChannel().queueBind(exchangeName, queueName, rountingKey);
        } catch (IOException e) {
            SwiftLogger.error("队列绑定交换机时出现异常");
        }
    }

    /**
     * 定义队列
     * @param queueName
     */
    public static void declareQueue(String queueName) {
        try {
            //第一个参数表示队列名称、第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
            //第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、第四个参数为当所有消费者客户端连接断开时是否自动删除队列、第五个参数为队列的其他参数
            getChannel().queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            SwiftLogger.error("定义交换机时出现异常");
        }
    }

    /**
     * 定义交换机
     * @param exchangeName
     */
    public static void declareExchange(String exchangeName) {
        declareExchange(exchangeName, defaultExchangeType);
    }

    /**
     * 定义交换机
     * @param exchangeName
     */
    public static void declareExchange(String exchangeName, String exchangeType) {
        try {
            getChannel().exchangeDeclare(exchangeName, exchangeType);
        } catch (IOException e) {
            SwiftLogger.error("定义交换机时出现异常");
        }
    }
    /**
     * 获取通道
     * @return
     */
    private static Channel getChannel() {
        Channel channel = CHANNEL_REGISTER.get();
        if (channel == null) {
            try {
                channel = getConnection().createChannel();
                CHANNEL_REGISTER.set(channel);
            } catch (IOException e) {
                SwiftLogger.error("获取通道时出现异常");
            }
        }
        return channel;
    }

    /**
     * 获取 mq 连接
     * @return
     */
    private static Connection getConnection() {
        return MqConfig.getMqConnenction();
    }

    /**
     * 关闭本线程的 队列和连接
     */
    public static void closeMqChannelAndConnection() {
        closeChannel();
        MqConfig.closeConnection();
    }

    /**
     * 关闭 mq 连接
     * @return
     */
    public static void closeChannel() {
        try {
            Channel channel = CHANNEL_REGISTER.get();
            if (channel != null) {
                if (channel.isOpen()) {
                    channel.close();
                }
                CHANNEL_REGISTER.remove();
            }
        } catch (Exception e) {
            SwiftLogger.error("MQ 获取连接异常");
        }
    }

}
