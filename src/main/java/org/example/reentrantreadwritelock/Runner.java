package org.example.reentrantreadwritelock;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Runner {

    // Amount of reading value: 49623626
    // Amount of reading value: 2358866
    public static void main(String[] args) throws InterruptedException {
//        testCounter(CounterGuardedByLock::new);
        testCounter(CounterGuardedByReadWriteLock::new);
    }

    private static void testCounter(final Supplier<? extends AbstractCounter> counterFactory) throws InterruptedException {
        final int amountOfThreadsGettingValue = 50;
        final AbstractCounter counter = counterFactory.get();

        final ReadingValueTask[] readingValueTasks = createReadingTask(counter, amountOfThreadsGettingValue);
        final Thread[] readingValueThreads = mapToThreads(readingValueTasks);

        final Runnable incrementCounterTask = createIncrementingCounterTask(counter);
        final int amountOfThreadsIncrementingCounter = 2;
        final Thread[] incrementCounterThreads = createThreads(
                incrementCounterTask,
                amountOfThreadsIncrementingCounter);

        startThreads(readingValueThreads);
        startThreads(incrementCounterThreads);

        SECONDS.sleep(1);

        interruptThreads(readingValueThreads);
        interruptThreads(incrementCounterThreads);

        waitingUntilFinishAllThreads(readingValueThreads);

        final long totalAmountOfReads = findTotalAmountOfReads(readingValueTasks);
        System.out.printf("Amount of reading value: %d", totalAmountOfReads);
    }

    private static ReadingValueTask[] createReadingTask(final AbstractCounter counter,
                                                        final int amountOfTask) {
        return IntStream.range(0, amountOfTask)
                .mapToObj(i -> new ReadingValueTask(counter))
                .toArray(ReadingValueTask[]::new);
    }

    private static Thread[] mapToThreads(final ReadingValueTask[] tasks) {
        return stream(tasks)
                .map(Thread::new)
                .toArray(Thread[]::new);
    }

    private static Runnable createIncrementingCounterTask(final AbstractCounter counter) {
        return () -> {
            while (!currentThread().isInterrupted()) {
                incrementCounter(counter);
            }
        };
    }

    private static void incrementCounter(AbstractCounter counter) {
        try {
            counter.increment();
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    private static Thread[] createThreads(final Runnable task, int amountOfThreads) {
        return IntStream.range(0, amountOfThreads)
                .mapToObj(i -> new Thread(task))
                .toArray(Thread[]::new);
    }

    public static void startThreads(Thread[] threads) {
        forEach(threads, Thread::start);
    }

    public static void interruptThreads(Thread[] threads) {
        forEach(threads, Thread::interrupt);
    }

    public static void waitingUntilFinishAllThreads(Thread[] threads) {
        forEach(threads, Runner::waitUntilFinish);
    }

    private static void forEach(final Thread[] threads, final Consumer<Thread> action) {
        stream(threads).forEach(action);
    }

    public static void waitUntilFinish(final Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    private static long findTotalAmountOfReads(final ReadingValueTask[] tasks) {
        return stream(tasks)
                .mapToLong(ReadingValueTask::getAmountOfReads)
                .sum();
    }

    public static final class ReadingValueTask implements Runnable {
        private final AbstractCounter counter;
        private long amountOfReads;

        public ReadingValueTask(AbstractCounter counter) {
            this.counter = counter;
        }

        public long getAmountOfReads() {
            return amountOfReads;
        }

        @Override
        public void run() {
            while (!currentThread().isInterrupted()) {
                this.counter.getValue();
                this.amountOfReads++;
            }
        }
    }
}
