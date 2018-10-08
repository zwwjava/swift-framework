package org.swift.framework.proxy.test;

/**
 * @Description -
 * @Author zww
 * @Date 2018/10/8 11:05
 */
public class ClientThread extends Thread {
    private Squence squence;

    public ClientThread(Squence squence) {
        this.squence = squence;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + squence.getNumber());
        }
    }
}
