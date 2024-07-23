package org.example.volatiletask;

import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class PrintingTask implements Runnable{
    private volatile boolean shouldPrint = true;


    public void setShouldPrint(boolean shouldPrint) {
        this.shouldPrint = shouldPrint;
    }

    @Override
    public void run() {
        while(this.shouldPrint){
            try {
                out.println("In progress.");
                MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
