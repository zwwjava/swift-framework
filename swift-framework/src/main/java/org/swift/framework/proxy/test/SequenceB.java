package org.swift.framework.proxy.test;

import org.swift.framework.proxy.MyThreadLocal;

/**
 * @Description -
 * @Author zww
 * @Date 2018/10/8 11:14
 */
public class SequenceB implements Squence{

    private static MyThreadLocal<Integer> numberMap = new MyThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    public int getNumber() {
        numberMap.set(numberMap.get() + 1);
        return numberMap.get();
    }

    public static void main(String[] args) {
        Squence squence = new SequenceB();
        ClientThread thread1 = new ClientThread(squence);
        ClientThread thread2 = new ClientThread(squence);
        ClientThread thread3 = new ClientThread(squence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
