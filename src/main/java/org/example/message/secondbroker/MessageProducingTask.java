package org.example.message.secondbroker;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class MessageProducingTask implements Runnable {

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private final int maxAmountOfMessagesToProduce;
    private final String name;

    public MessageProducingTask(MessageBroker messageBroker, MessageFactory messageFactory, int maxAmountOfMessagesToProduce, String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.maxAmountOfMessagesToProduce = maxAmountOfMessagesToProduce;
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        while(!currentThread().isInterrupted()){
            final Message producedMessage = this.messageFactory.createMessage();
            TimeUnit.SECONDS.sleep(1);
            this.messageBroker.addMessage(producedMessage, this);
        }
    }

    public int getMaxAmountOfMessagesToProduce() {
        return maxAmountOfMessagesToProduce;
    }

    public String getName() {
        return name;
    }
}
