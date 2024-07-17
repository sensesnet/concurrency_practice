package org.example.message.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class ReentrantLockTask {

    public static void main(String[] args) {
        final EvenNumberGenerator generator = new EvenNumberGenerator();

        final Runnable generateTask = () -> IntStream.range(0, 100)
                .forEach(i -> out.println(generator.generate()));

        final Thread threadA = new Thread(generateTask);
        threadA.start();

        final Thread threadB = new Thread(generateTask);
        threadB.start();

        final Thread threadC = new Thread(generateTask);
        threadC.start();

    }

    private static class EvenNumberGenerator {
        private final Lock lock;
        private int previousGenerated;

        public EvenNumberGenerator() {
            this.lock = new ReentrantLock();
            this.previousGenerated = -2;
        }

        public int generate() {
            return this.lock.tryLock() ? onSuccessAcquireLock() : onFailAcquireLock();
        }

        private int onSuccessAcquireLock() {
            try {
                return this.previousGenerated += 2;
            } finally {
                lock.unlock();
            }
        }

        private int onFailAcquireLock() {
            out.printf("Thread '%s' didn't acquire lock.\n", currentThread().getName());
            throw new RuntimeException();
        }
    }
}
