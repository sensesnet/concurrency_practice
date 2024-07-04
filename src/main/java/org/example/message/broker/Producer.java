package org.example.message.broker;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class Producer implements Runnable {

    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;

    public Producer(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        this.messageFactory = new MessageFactory();
    }

    public static final class MessageFactory {
        public static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
        public static final String TEMPLATE = "Message#%d";
        int nextMessageIndex;

        public MessageFactory() {
            this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
        }

        public Message createMessage() {
            return new Message(String.format(TEMPLATE, nextMessageIndex++));
        }
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
