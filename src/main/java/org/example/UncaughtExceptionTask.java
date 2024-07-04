package org.example;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class UncaughtExceptionTask {

    public static void main(String[] args) {
        final UncaughtExceptionHandler uncaughtExceptionHandler = (thread, exception) ->
                out.printf("Exception with message '%s' in thread '%s\n",
                        exception.getMessage(),
                        thread.getName());
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);


        final Thread th = new Thread(new Task());
//        th.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        th.start();

        final Thread thOneMore = new Thread(new Task());
//        thOneMore.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thOneMore.start();
    }
    public static final class Task implements Runnable {
        @Override
        public void run() {
            throw new RuntimeException("I'm in exception!");
        }
    }
}
