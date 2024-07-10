package org.example.message.lock;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class ReentrantLockMethodsTask {

    @SneakyThrows
    public static void main(String[] args) {
        final Counter counter = new Counter();
        final int times = 10;

        final Thread threadA = new Thread(createTaskDoingOperationOnCounter(
                counter,
                i -> counter.increment(),
                times
                ));


        final Thread threadB = new Thread(createTaskDoingOperationOnCounter(
                counter,
                i -> counter.decrement(),
                times
        ));

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        out.printf("Counter value '%d'.\n", counter.getPreviousGenerated());
    }

    private static Runnable createTaskDoingOperationOnCounter( final Counter counter,
                                                               final IntConsumer operation,
                                                               final int times){
        return () -> {
          counter.lock();
          try{
              IntStream.range(0,times)
                      .forEach(operation);
          }
          finally {
              counter.unlock();
          }
        };
    }

    private static final class Counter {
        private final Lock lock;
        private int previousGenerated;

        public Counter() {
            this.lock = new ReentrantLock();
            this.previousGenerated = -2;
        }

        public void lock() {
            lock.lock();
            printValue("Thread '%s' locked Counter.\n");
        }

        public void unlock() {
            printValue("Thread '%s' unlocking Counter.\n");
            lock.unlock();
        }

        public void increment() {
            this.previousGenerated++;
            printValue("Thread '%s' incremented Counter.\n");
        }

        public void decrement() {
            this.previousGenerated--;
            printValue("Thread '%s' decremented Counter.\n");
        }

        public int getPreviousGenerated() {
            return previousGenerated;
        }

        public static void printValue(String msg) {
            out.printf(msg, currentThread().getName());
        }
    }
}
