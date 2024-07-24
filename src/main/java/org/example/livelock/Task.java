package org.example.livelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class Task implements Runnable {
    private static final String MSG_TEMPLATE_TRY_ACQUIRE_LOCK = "Thread '%s' is trying to acquire lock '%s'.\n";
    private static final String MSG_TEMPLATE_SUCCESS_ACQUIRE_LOCK = "Thread '%s' acquired lock '%s'.\n";
    private static final String MSG_TEMPLATE_RELEASE_LOCK = "Thread '%s' released lock '%s'.\n";

    private static final String FIRST_LOCK_NAME = "firstLock";
    private static final String SECOND_LOCK_NAME = "secondLock";
    private final Lock firstLock;
    private final Lock secondLock;

    public Task(Lock firstLock, Lock secondLock) {
        this.firstLock = firstLock;
        this.secondLock = secondLock;
    }

    @Override
    public void run() {
        final String currentThreadName = currentThread().getName();
        out.printf(MSG_TEMPLATE_TRY_ACQUIRE_LOCK, currentThreadName, FIRST_LOCK_NAME);
        this.firstLock.lock();
        try {
            out.printf(MSG_TEMPLATE_SUCCESS_ACQUIRE_LOCK, currentThreadName, FIRST_LOCK_NAME);
            TimeUnit.MICROSECONDS.sleep(50);
            while (!tryAcquireSecondLock()) {
                TimeUnit.MICROSECONDS.sleep(50);
                this.firstLock.unlock();
                out.printf(MSG_TEMPLATE_RELEASE_LOCK, currentThreadName, FIRST_LOCK_NAME);
                TimeUnit.MICROSECONDS.sleep(50);
                out.printf(MSG_TEMPLATE_TRY_ACQUIRE_LOCK, currentThreadName, FIRST_LOCK_NAME);
                this.firstLock.lock();
                out.printf(MSG_TEMPLATE_SUCCESS_ACQUIRE_LOCK, currentThreadName, FIRST_LOCK_NAME);
                TimeUnit.MICROSECONDS.sleep(50);
            }
            try{
                out.printf(MSG_TEMPLATE_SUCCESS_ACQUIRE_LOCK, currentThreadName, SECOND_LOCK_NAME);
            }
            finally {
                this.secondLock.unlock();
                out.printf(MSG_TEMPLATE_RELEASE_LOCK, currentThreadName, SECOND_LOCK_NAME);
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            this.firstLock.unlock();
            out.printf(MSG_TEMPLATE_RELEASE_LOCK, currentThreadName, FIRST_LOCK_NAME);
        }
    }

    private boolean tryAcquireSecondLock() {
        final String currentThreadName = currentThread().getName();
        out.printf(MSG_TEMPLATE_TRY_ACQUIRE_LOCK, currentThreadName, SECOND_LOCK_NAME);
        return this.secondLock.tryLock();
    }
}
