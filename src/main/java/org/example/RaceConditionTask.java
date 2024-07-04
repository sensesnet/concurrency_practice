package org.example;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RaceConditionTask {
    public static int counter = 0;
    public static final int INCR_AMOUNT_FIRST_TH = 500;
    public static final int INCR_AMOUNT_SECOND_TH = 600;

    public static void main(String[] args) throws InterruptedException {
        final Thread firstThread = createIncrementingCounterThread(INCR_AMOUNT_FIRST_TH);
        final Thread secondThread = createIncrementingCounterThread(INCR_AMOUNT_SECOND_TH);

        firstThread.start();
        secondThread.start();

        firstThread.join();
        secondThread.join();

        System.out.println("counter = " + counter);
    }

    public static Thread createIncrementingCounterThread(int incrementValue) {
        return new Thread(() ->
                IntStream.range(0, incrementValue).forEach(i -> getAnInt()));
    }

    private static synchronized int getAnInt() {
        return counter++;
    }
}
