package org.example.compareandswap;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Runner {
    public static void main(String[] args) {
        final EventNumberGenerator generator = new EventNumberGenerator();

        final int taskGenerationCounts = 10000;

        final Runnable generatingTask = () -> IntStream.range(0, taskGenerationCounts)
                .forEach(i -> generator.generate());

        final int amountOfGenThreads = 5;
        final Thread[] threads = createThreads(generatingTask, amountOfGenThreads);

        startThreads(threads);
        commonJoin(threads);

        final int expectedGenValue = amountOfGenThreads * taskGenerationCounts * 2; //2 is generation delta
        final int actualGenValue = generator.getValue();

        if (expectedGenValue != actualGenValue) {
            throw new RuntimeException(
                    "The expected %d but actual was %d".formatted(expectedGenValue, actualGenValue));
        }
    }

    private static Thread[] createThreads(final Runnable task,
                                          final int amountOfThreads) {
        return IntStream.range(0, amountOfThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }

    private static void startThreads(final Thread[] task) {
        Arrays.stream(task).forEach(Thread::start);
    }

    private static void commonJoin(final Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void customJoin(final Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
