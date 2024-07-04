package org.example.message.secondbroker;

import lombok.SneakyThrows;
import lombok.var;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.lang.Thread.currentThread;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public final class MessageBroker {
    private final Queue<Message> messageQueue;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messageQueue = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public synchronized void addMessage(final Message message) {
        try {
            while (messageQueue.size() >= maxStoredMessages) {
                super.wait();
            }
            messageQueue.add(message);
            super.notify();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> removeMessage() {
        try {
            while (this.messageQueue.isEmpty()) {
                super.wait();
            }
            final var message = messageQueue.poll();
            super.notify();
            return of(message);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            return empty();
        }
    }
}
