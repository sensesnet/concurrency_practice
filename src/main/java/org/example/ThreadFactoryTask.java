package org.example;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

import static java.lang.System.out;

public class ThreadFactoryTask {

    public static void main(String[] args) throws InterruptedException {
        final UncaughtExceptionHandler uncaughtExceptionHandler = (thread, exception) ->
                out.printf("Exception with message '%s' in thread '%s'\n",
                        exception.getMessage(),
                        thread.getName());

        final ThreadFactory factory =
                new DaemonThreadWithUncaughtExHandlerFactory(uncaughtExceptionHandler);
        Thread firstTh = factory.newThread(new Task());
        firstTh.start();
        Thread secondTh = factory.newThread(new Task());
        secondTh.start();

        firstTh.join();
        secondTh.join();

    }
    public static final class Task implements Runnable {
        @Override
        public void run() {
            out.printf("'%s' :: '%b'\n",
                    Thread.currentThread().getName(),
                    Thread.currentThread().isDaemon());
            throw new RuntimeException("I'm in exception!");
        }
    }

    public static final class DaemonThreadWithUncaughtExHandlerFactory implements ThreadFactory {
        private final UncaughtExceptionHandler handler;
        public DaemonThreadWithUncaughtExHandlerFactory(UncaughtExceptionHandler handler) {
            this.handler=handler;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(new Task());
            thread.setUncaughtExceptionHandler(handler);
            thread.setDaemon(true);
            return thread;
        }
    }
}
