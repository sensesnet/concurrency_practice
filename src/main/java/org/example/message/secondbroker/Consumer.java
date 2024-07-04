package org.example.message.secondbroker;

import lombok.SneakyThrows;
import lombok.var;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class Consumer implements Runnable {

    private final MessageBroker broker;

    public Consumer(MessageBroker broker) {
        this.broker = broker;
    }

    @Override
    public void run() {
        while (!currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
                final var message = this.broker.removeMessage();
                final Message consumedMessage = message.orElseThrow(
                        MessageConsumingException::new
                );
                System.out.printf("Message '%s' was consumed!\n", consumedMessage.message);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }
    }
}
