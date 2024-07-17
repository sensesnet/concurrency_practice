package org.example.condition;

import lombok.var;

import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        final BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(5);

        final Runnable producingTask = () -> IntStream.iterate(0, i -> i + 1)
                .forEach(i -> {
                    try {
                        boundedBuffer.put(i);
                        SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        currentThread().interrupt();
                    }
                });

        var producingThread = new Thread(producingTask);

        final Runnable consumingTask = () -> {
            try {
                while (!currentThread().isInterrupted()) {
                    boundedBuffer.take();
                    SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        };
        SECONDS.sleep(1);
        var consumingThread = new Thread(consumingTask);

        producingThread.start();
        consumingThread.start();
//        producingThread.join();
//        consumingThread.join();
    }
}
