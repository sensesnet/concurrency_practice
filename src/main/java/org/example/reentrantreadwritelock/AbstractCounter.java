package org.example.reentrantreadwritelock;

import java.util.concurrent.locks.Lock;

public abstract class AbstractCounter {
    private long value;

    protected abstract Lock getReadLock();

    protected abstract Lock getWriteLock();

    public long getValue() {
        final Lock lock = this.getReadLock();
        lock.lock();
        try {
            return value;
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
