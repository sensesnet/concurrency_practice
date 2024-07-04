package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class WaitAndNotifyTask {
    private static int sentMsg = 0;
    private static int receivedMsg = 0;
    public static final int MAX_QUEUE = 5;
    private static final Object FULL_BUFFER_LOCK = new Object();
    private static final Object EMPTY_BUFFER_LOCK = new Object();
    public static final int PRODUCER_NUMBER_OF_MESSAGES = 500;

    public static void main(String[] args) throws InterruptedException {

        final Runnable producer = () -> {
            while (true) {
                while (MessageBroker.buffer.size() < MAX_QUEUE) {
                    MessageBroker.addToBuffer("Message:" + ++sentMsg);
                    if (sentMsg == PRODUCER_NUMBER_OF_MESSAGES) {
                        Thread.currentThread().interrupt();
                    }
                }
                releaseFullBufferLock();
                try {
                    waitOnEmpty();
                } catch (InterruptedException e) {
                    out.println("Producer was stopped!");
                    break;
                }
            }
        };

        final Runnable consumer = () ->
        {
            while (true) {
                while (!(MessageBroker.buffer.size() == 0)) {
                    MessageBroker.removeFromBuffer(
                            MessageBroker.buffer.stream()
                                    .findAny().get());
                    receivedMsg++;
                    if (receivedMsg == PRODUCER_NUMBER_OF_MESSAGES) {
                        Thread.currentThread().interrupt();
                    }
                }
                releaseEmptyBufferLock();
                try {
                    waitOnFull();
                } catch (InterruptedException e) {
                    out.println("Consumer was stopped!");
                    break;
                }
            }
        };

        final Thread producerThread = new Thread(producer);
        final Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        out.println("Main thread has been stopped!");
    }

    public static final class MessageBroker {
        private static final List<String> buffer = new ArrayList<>();

        private static void addToBuffer(final String message) {
            buffer.add(message);
            out.println("Producer send a message: " + message);
        }

        private static void removeFromBuffer(final String message) {
            buffer.remove(message);
            out.println("Consumer clean up a message: " + message);
        }
    }

    public static void waitOnFull() throws InterruptedException {
        synchronized (FULL_BUFFER_LOCK) {
            FULL_BUFFER_LOCK.wait();
        }
    }

    public static void waitOnEmpty() throws InterruptedException {
        synchronized (EMPTY_BUFFER_LOCK) {
            EMPTY_BUFFER_LOCK.wait();
        }
    }

    public static void releaseEmptyBufferLock() {
        synchronized (EMPTY_BUFFER_LOCK) {
            EMPTY_BUFFER_LOCK.notifyAll();
        }
    }

    public static void releaseFullBufferLock() {
        synchronized (FULL_BUFFER_LOCK) {
            FULL_BUFFER_LOCK.notifyAll();
        }
    }
}
