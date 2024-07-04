package org.example;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class SynchronizedBlockThisTask {
    public static int firstCounter = 0;
    public static int secondCounter = 0;
    public static final int INCR_AMOUNT_FIRST_COUNTER = 500;
    public static final int INCR_AMOUNT_SECOND_COUNTER = 600;
    public static final Object LOCK_FIRST = new Object();
    public static final Object LOCK_SECOND = new Object();

    public static void main(String[] args) throws InterruptedException {
        final Counter firstCounter = new Counter();
        final Counter secondCounter = new Counter();

        final Thread firstAThread = createIncrementingCounterThread(
                INCR_AMOUNT_FIRST_COUNTER,
                i -> firstCounter.increment());
        final Thread firstBThread = createIncrementingCounterThread(
                INCR_AMOUNT_FIRST_COUNTER,
                i -> firstCounter.increment());
        final Thread secondAThread = createIncrementingCounterThread(
                INCR_AMOUNT_SECOND_COUNTER,
                i -> secondCounter.increment());
        final Thread secondBThread = createIncrementingCounterThread(
                INCR_AMOUNT_SECOND_COUNTER,
                i -> secondCounter.increment());

        startThreads(firstAThread, firstBThread,
                secondAThread, secondBThread);

        joinThreads(firstAThread, firstBThread,
                secondAThread, secondBThread);


        System.out.println("firstCounter = " + firstCounter.counter);
        System.out.println("secondCounter = " + secondCounter.counter);
    }

    public static Thread createIncrementingCounterThread(final int incrementAmount,
                                                         final IntConsumer consumer) {
        return new Thread(() ->
                IntStream.range(0, incrementAmount).forEach(consumer));
    }

    public static final class Counter {
        private int counter;

        //        public synchronized void increment() {
//            this.counter++;
//        }
        public void increment() {
            synchronized (this) {
                counter++;
            }
        }
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
