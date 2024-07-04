package org.example.message.secondbroker;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class Producer implements Runnable {

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    public Producer(MessageBroker messageBroker, MessageFactory messageFactory) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
    }

    @SneakyThrows
    @Override
    public void run() {
        while(!currentThread().isInterrupted()){
            final Message producedMessage = this.messageFactory.createMessage();
            TimeUnit.SECONDS.sleep(1);
            this.messageBroker.addMessage(producedMessage);
            System.out.printf("Message '%s' was produced!\n", producedMessage);
        }
    }
}
