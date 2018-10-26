package org.swift.framework.test;

/**
 * @Description -
 * @Author zww
 * @Date 2018/10/25 15:15
 */
public class ThreadTest extends Thread{

    public static void main(String[] args) {

        ThreadTest mt1 = new ThreadTest("Thread a");
        ThreadTest mt2 = new ThreadTest("Thread b");

        mt1.setName("Test-Thread-1 ");
        mt2.setName("Test-Thread-2 ");

        mt1.start();
        mt2.start();
    }

    public ThreadTest(String name) {
    }

    public void run() {
        while (true) {
        }
    }

}
