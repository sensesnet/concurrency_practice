package org.example.message.broker;

import lombok.SneakyThrows;
import lombok.var;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.util.Optional.of;

public final class MessageBroker {
    private final Queue<Message> messageQueue;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messageQueue = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    @SneakyThrows
    public synchronized void addMessage(final Message message) {
        while(messageQueue.size()>=maxStoredMessages){
            super.wait();
        }
        messageQueue.add(message);
        super.notify();
    }

    @SneakyThrows
    public synchronized Optional<Message> removeMessage() {
//        deadlock example
//        while (this.messageQueue.isEmpty()){
//
//        }
        while(this.messageQueue.isEmpty()){
            super.wait();
        }
        final var message =  messageQueue.poll();
        super.notify();
        return of(message);
    }
}
