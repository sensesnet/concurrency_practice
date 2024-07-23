package org.example.volatiletask;

import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        final PrintingTask printingTask = new PrintingTask();
        final Thread printThread = new Thread(printingTask);

        printThread.start();
        SECONDS.sleep(5);

        printingTask.setShouldPrint(false);
        out.println("Printing should be stopped.");
    }
}
