package org.example;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.lang.Thread.*;

public class PriorityThreadTask {
    public static final String MESSAGE_TEMPLATE_THREAD_NAME = "%s : %d\n";

    public static void main(String[] args) {
        Thread.currentThread().setPriority(MIN_PRIORITY);
        final Thread th = new Thread(() -> printThreadName(currentThread()));
        th.start();
        printThreadName(Thread.currentThread());

        Thread threadMin = new Thread(new TaskMin());
        threadMin.setPriority(MAX_PRIORITY);
        threadMin.start();
    }

    public static void printThreadName(final Thread thread) {
        out.printf(MESSAGE_TEMPLATE_THREAD_NAME, thread.getName(), thread.getPriority());
    }

    public static final class TaskMin implements Runnable {
        @Override
        public void run() {
            IntStream.range(0, 100).forEach(out::println);
            out.println("The thread max finished!");
        }
    }
}
