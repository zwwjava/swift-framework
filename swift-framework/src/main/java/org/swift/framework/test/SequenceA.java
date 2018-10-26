package org.swift.framework.test;

import org.swift.framework.proxy.test.ClientThread;

/**
 * @Description -
 * @Author zww
 * @Date 2018/10/8 11:08
 */
public class SequenceA implements Squence {
    private static int number = 0;

    @Override
    public int getNumber() {
        number++;
        return number;
    }

    public static void main(String[] args) {
        Squence squence = new SequenceA();
        ClientThread thread1 = new ClientThread(squence);
        ClientThread thread2 = new ClientThread(squence);
        ClientThread thread3 = new ClientThread(squence);

        thread1.start();
        thread2.start();
        thread3.start();
    }

}
