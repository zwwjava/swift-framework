package org.swift.framework.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description -
 * @Author zww
 * @Date 2018/10/25 14:13
 */
public class BTraceTest {

    /*
    @OnMethod(clazz="org.swift.framework.test.BTraceTest",method="add",location=@Location(Kind.RETURN))
    public static void func(int a,int b,@Return int result) {
        println("调用堆栈:");
        jstack();
        println(strcat("方法参数A:",str(a)));
        println(strcat("方法参数B:",str(b)));
        println(strcat("方法返回:",str(result)));
        println("end");
    }
     */
    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) throws IOException{
        BTraceTest test = new BTraceTest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 10; i++) {
            reader.readLine();
            int a = (int) Math.round(Math.random() * 1000);
            int b = (int) Math.round(Math.random() * 1000);
            System.out.println(test.add(a, b));
        }
    }

}
