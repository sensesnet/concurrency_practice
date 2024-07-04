package org.example;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class SynchronizedBlockTask {
    public static int firstCounter = 0;
    public static int secondCounter = 0;
    public static final int INCR_AMOUNT_FIRST_COUNTER = 500;
    public static final int INCR_AMOUNT_SECOND_COUNTER = 600;
    public static final Object LOCK_FIRST = new Object();
    public static final Object LOCK_SECOND = new Object();

    public static void main(String[] args) throws InterruptedException {
        final Thread firstAThread = createIncrementingCounterThread(
                INCR_AMOUNT_FIRST_COUNTER,
                i -> firstCounter());
        final Thread firstBThread = createIncrementingCounterThread(
                INCR_AMOUNT_FIRST_COUNTER,
                i -> firstCounter());
        final Thread secondAThread = createIncrementingCounterThread(
                INCR_AMOUNT_SECOND_COUNTER,
                i -> secondCounter());
        final Thread secondBThread = createIncrementingCounterThread(
                INCR_AMOUNT_SECOND_COUNTER,
                i -> secondCounter());

        startThreads(firstAThread, firstBThread,
                secondAThread, secondBThread);

        joinThreads(firstAThread, firstBThread,
                secondAThread, secondBThread);


        System.out.println("firstCounter = " + firstCounter);
        System.out.println("secondCounter = " + secondCounter);
    }

    public static Thread createIncrementingCounterThread(final int incrementAmount,
                                                         final IntConsumer consumer) {
        return new Thread(() ->
                IntStream.range(0, incrementAmount).forEach(consumer));
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static void joinThreads(Thread... threads) {
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private static int firstCounter() {
        synchronized (LOCK_FIRST) {
            return firstCounter++;
        }
    }

    private static synchronized int secondCounter() {
        synchronized (LOCK_SECOND) {
            return secondCounter++;
        }
    }
}
