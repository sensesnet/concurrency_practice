package org.example.reentrantreadwritelock;

import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.currentThread;

public abstract class AbstractCounter {
    private long value;

    protected abstract Lock getReadLock();

    protected abstract Lock getWriteLock();

    public OptionalLong getValue() {
        final Lock lock = this.getReadLock();
        lock.lock();
        try {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                return OptionalLong.empty();
            }
            return OptionalLong.of(value);
        } finally {
            lock.unlock();
        }
    }

    public final void increment() {
        final Lock lock = this.getWriteLock();
        lock.lock();
        try {
            this.value++;
        } finally {
            lock.unlock();
        }
    }
}
