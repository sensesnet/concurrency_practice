package org.example.message.broker;

import lombok.SneakyThrows;
import lombok.var;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class Consumer implements Runnable {

    private final MessageBroker broker;

    public Consumer(MessageBroker broker) {
        this.broker = broker;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!currentThread().isInterrupted()) {
            TimeUnit.SECONDS.sleep(1);
            final var message = this.broker.removeMessage();
            System.out.printf("Message '%s' was consumed!\n", message.get());
        }
    }
}
