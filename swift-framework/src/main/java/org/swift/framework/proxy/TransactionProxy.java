package org.swift.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.framework.annotation.Transaction;
import org.swift.framework.helper.DataBaseHelper;

import java.lang.reflect.Method;

/**
 * @Description - 事物操作的代理
 * @Author zww
 * @Date 2018/10/8 14:40
 */
public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    //保证同一线程中事物控制相关逻辑只会执行一次
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DataBaseHelper.beginTransaction();
                logger.info("开启事物");
                result = proxyChain.doProxyChain();
                DataBaseHelper.commitTransaction();
                logger.info("提交事物");
            } catch (Exception e) {
                 DataBaseHelper.rollbackTransaction();
                 logger.info("事物回滚");
                 throw e;
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }

}
