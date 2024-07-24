package org.example.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

    public static void main(String[] args) {
        final Lock firstGivenLock = new ReentrantLock();
        final Lock secondGivenLock = new ReentrantLock();

        final Thread firstThread = new Thread(new Task(firstGivenLock, secondGivenLock));
//        final Thread secondThread = new Thread(new Task(secondGivenLock, firstGivenLock)); //deadlock
        final Thread secondThread = new Thread(new Task(firstGivenLock, secondGivenLock)); //solution

        firstThread.start();
        secondThread.start();
    }
}
