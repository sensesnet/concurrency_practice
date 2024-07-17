package org.example.condition;

import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.Arrays.stream;

public final class BoundedBuffer<T> {

    private final T[] elements;
    private final Lock lock;
    private final Condition condition;
    private int size;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(final int capacity) {
        this.elements = (T[]) new Object[capacity];
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public boolean isEmpty() {
        this.lock.lock();
        try {
            return this.size == 0;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isFull() {
        this.lock.lock();
        try {
            return this.size == this.elements.length;
        } finally {
            this.lock.unlock();
        }
    }

    @SneakyThrows
    public void put(final T element) {
        this.lock.lock();
        try {
            while (this.isFull()) {
                this.condition.await();
            }
            this.elements[this.size] = element;
            this.size++;
            out.printf("%s was put in buffer. Results buffer: %s%n", element, this);
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public T take() {
        this.lock.lock();
        try {
            while (this.isEmpty()) {
                this.condition.await();
            }
            final T result = this.elements[this.size - 1];
            this.elements[this.size - 1] = null;
            this.size--;
            this.condition.signalAll();
            out.printf("%s was taken from buffer. Results buffer: %s%n", result, this);
            return result;
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public String toString() {
        this.lock.lock();
        try {
            return "{"
                    + stream(this.elements, 0, this.size)
                    .map(Objects::toString)
                    .collect(Collectors.joining(","))
                    + "}";
        } finally {
            this.lock.unlock();
        }
    }
}
