package com.n26;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton {
    /**
     * The Java volatile keyword guarantees visibility of changes to variables across threads.
     * If the counter variable is not declared volatile there is no guarantee about when the value of the
     * counter variable is written from the CPU cache back to main memory. This means,
     * that the counter variable value in the CPU cache may not be the same as in main memory.
     * By declaring the counter variable volatile all writes to the counter variable will be written back to main
     * memory immediately. Also, all reads of the counter variable will be read directly from main memory.
     */
    private volatile static Singleton instance = null;

    private Singleton() {
        if (instance != null) {
            throw new RuntimeException("Cannot create, please use getInstance()!");
        }
        System.out.println("Creating singleton ...");
    }

    /**
     * synchronized on method level makes getInstance single-point-of-access
     * all calls to getInstance is going to be synchronized
     */
    public synchronized static Singleton getInstance() {
//      double checked locking
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}


class TestSingleton {
    public static void main(String[] args) throws Exception {
//        Singleton s1 = Singleton.getInstance();
//        Singleton s2 = Singleton.getInstance();
//        printSingleton(s1);
//        printSingleton(s2);

//        Reflection: it creates new instance
//        System.out.println("########## Using reflection #########");
//        Class clazz = Class.forName("com.n26.Singleton");
//        Constructor<Singleton> ctor = clazz.getDeclaredConstructor();
//        ctor.setAccessible(true);
//        Singleton s3 = ctor.newInstance();
//        printSingleton(s3);

//        Thread.sleep(100);

//        multi thread
        System.out.println("#### Using multi-threads ######");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(TestSingleton::useSingleton);
        executorService.submit(TestSingleton::useSingleton);
        executorService.shutdown();
    }

    static void useSingleton() {
        Singleton s1 = Singleton.getInstance();
        printSingleton(s1);
    }

    static void printSingleton(Singleton s) {
        System.out.println(s.hashCode());
    }
}