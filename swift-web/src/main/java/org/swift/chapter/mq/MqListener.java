package org.swift.chapter.mq;

import com.alibaba.fastjson.JSON;
import org.swift.chapter.model.Customer;
import org.swift.framework.annotation.RabbitEnable;
import org.swift.framework.annotation.RabbitListener;
import org.swift.framework.util.log.SwiftLogger;

/**
 * @Description - MQ信息的监听类，需要使用 @RabbitEnable 注解
 * @Author zww
 * @Date 2019/9/12
 */
@RabbitEnable
public class MqListener {

    /**
     * 监听MQ 信息，配置队列名称
     * @param bytes 参数只允许是 byte[]
     */
    @RabbitListener(queue = "nihao", exchange = "nihao")
    public void receiverMqOne(byte[] bytes) {
        SwiftLogger.info("receiverMqOne : 接收到一条mq 信息");
        if (bytes != null && bytes.length > 0) {
            Customer customer = JSON.parseObject(bytes, Customer.class);
            if (customer != null) {
                SwiftLogger.info("receiverMqOne : 信息内容：" + customer.getName());
                System.out.println(customer.getName());
            }
        }
    }

}
