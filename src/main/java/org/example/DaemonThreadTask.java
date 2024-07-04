package org.example;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DaemonThreadTask {
    public static void main(String[] args) throws InterruptedException {
        final Thread daemonThread = new Thread(new TaskDaemon());
        daemonThread.setDaemon(true);
        daemonThread.start();
        System.out.println(daemonThread.isDaemon());
        System.out.println("Main thread finished!");


        final Thread firstDaemonThread = new Thread(() -> {
           printThreadNameAndDaemonStatus(currentThread());
           final Thread thDaemon = new Thread(() -> {
               printThreadNameAndDaemonStatus(currentThread());
           });
           thDaemon.start();
        });
        firstDaemonThread.setDaemon(true);
        firstDaemonThread.start();
        firstDaemonThread.join();
    }

    public static final class TaskDaemon implements Runnable {
        @Override
        public void run() {
            while (true) {
                out.println("I'm working now!");
                try {
                    SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        }
    }

    public static void printThreadNameAndDaemonStatus(final Thread threads) {
        out.printf("%s : %b\n", threads.getName(), threads.isDaemon());
    }

}
