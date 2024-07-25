package org.example.message.secondbroker;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class MessageConsumingTask implements Runnable {

    private final MessageBroker broker;
    private final int minStoredMessages;
    private final String name;

    public MessageConsumingTask(MessageBroker broker, int minStoredMessages, String name) {
        this.broker = broker;
        this.minStoredMessages = minStoredMessages;
        this.name = name;
    }

    @Override
    public void run() {
        while (!currentThread().isInterrupted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
                final Optional<Message> message = this.broker.consume(this);
                message.orElseThrow(
                        MessageConsumingException::new
                );
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }
    }

    public int getMinStoredMessages() {
        return minStoredMessages;
    }

    public String getName() {
        return name;
    }
}
