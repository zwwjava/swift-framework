package org.swift.framework.helper.mq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.swift.framework.util.PropUtil;
import org.swift.framework.util.log.SwiftLogger;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description - rabbitMq 的配置信息
 * @Author zww
 * @Date 2019/9/11
 */
public class MqConfig {

    private static ThreadLocal<Connection> CONNECTION_REGISTER = new ThreadLocal<Connection>();
    private static ConnectionFactory MQ_CONNECTION_FACTORY;
    private static final String HOST;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final String PORT;

    static {
        Properties conf = PropUtil.loadProps("swift.properties");
        HOST = StringUtils.isNotEmpty(conf.getProperty("swift.rabbitmq.host")) ? conf.getProperty("swift.rabbitmq.host") : "localhost";
        USERNAME = StringUtils.isNotEmpty(conf.getProperty("swift.rabbitmq.username")) ? conf.getProperty("swift.rabbitmq.username") : "guest";
        PASSWORD = StringUtils.isNotEmpty(conf.getProperty("swift.rabbitmq.password")) ? conf.getProperty("swift.rabbitmq.password") : "guest";
        PORT = StringUtils.isNotEmpty(conf.getProperty("swift.rabbitmq.port")) ? conf.getProperty("swift.rabbitmq.port") : "5672";

        Connection connection = null;
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置RabbitMQ相关信息
            factory.setHost(HOST);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);
//            factory.setPort(Integer.parseInt(PORT));
            //创建一个新的连接，测试连接正常
            connection = factory.newConnection();

            MQ_CONNECTION_FACTORY = factory;
        } catch (Exception e) {
            SwiftLogger.error("MQ 初始化连接异常");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获得一个 mq 连接
     * @return
     */
    public static Connection getMqConnenction() {
        Connection connection = CONNECTION_REGISTER.get();
        if (connection == null) {
            try {
                //创建一个新的连接
                connection = MQ_CONNECTION_FACTORY.newConnection();
                CONNECTION_REGISTER.set(connection);
                return connection;
            } catch (Exception e) {
                SwiftLogger.error("MQ 获取连接异常");
            }
        }
        return connection;
    }

    /**
     * 关闭 mq 连接
     * @return
     */
    public static void closeConnection() {
        try {
            Connection connection = CONNECTION_REGISTER.get();
            if (connection != null) {
                if (connection.isOpen()) {
                    connection.close();
                }
                CONNECTION_REGISTER.remove();
            }
        } catch (Exception e) {
            SwiftLogger.error("MQ 获取连接异常");
        }
    }

}
