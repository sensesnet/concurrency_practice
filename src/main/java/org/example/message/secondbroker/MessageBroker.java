package org.example.message.secondbroker;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.Optional.*;

public final class MessageBroker {
    private final Queue<Message> messageQueue;
    private final int maxStoredMessages;

    public MessageBroker(int maxStoredMessages) {
        this.messageQueue = new ArrayDeque<>(maxStoredMessages);
        this.maxStoredMessages = maxStoredMessages;
    }

    public synchronized void addMessage(final Message message,
                                        MessageProducingTask producingTask) {
        try {
            while (isShouldProduce(producingTask)) {
                super.wait();
            }

            messageQueue.add(message);
            out.printf("Message '%s' was produced with thread '%s'!\n",
                    message,
                    producingTask.getName());
            out.printf("Amount of messages before producing '%d'!\n",
                    messageQueue.size() - 1);
            super.notify();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(final MessageConsumingTask consumingTask) {
        try {
            while (!this.isShouldConsume(consumingTask)) {
                super.wait();
            }
            final Message message = messageQueue.poll();
            out.printf("Message '%s' was consumed with thread '%s'!\n",
                    message,
                    consumingTask.getName());
            out.printf("Amount of messages before consuming '%d'!\n",
                    messageQueue.size() + 1);
            super.notify();
            return ofNullable(message);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            return empty();
        }
    }

    private boolean isShouldConsume(MessageConsumingTask consumerTask) {
        return !messageQueue.isEmpty()
                && messageQueue.size() >= consumerTask.getMinStoredMessages();
    }

    private boolean isShouldProduce(MessageProducingTask producingTask) {
        return this.messageQueue.size() < this.maxStoredMessages
                && this.messageQueue.size() <= producingTask.getMaxAmountOfMessagesToProduce();
    }
}
