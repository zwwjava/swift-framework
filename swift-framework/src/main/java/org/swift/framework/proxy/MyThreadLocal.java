package org.swift.framework.proxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description - 自己实现的 ThreadLocal
 * @Author zww
 * @Date 2018/10/8 11:32
 */
public class MyThreadLocal<T> {

    private Map<Thread, T> container = Collections.synchronizedMap(new HashMap<Thread, T>());

    public void set(T value) {
        container.put(Thread.currentThread(), value);
    }

    public T get() {
        Thread thread = Thread.currentThread();
        T value = container.get(thread);
        if (value == null && !container.containsKey(thread)) {
            value = initialValue();
            container.put(thread, value);
        }
        return value;
    }

    public void remove() {
        container.remove(Thread.currentThread());
    }

    protected T initialValue() {
        return null;
    }

}
