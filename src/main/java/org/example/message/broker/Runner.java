package org.example.message.broker;

import lombok.SneakyThrows;

public class Runner {

    @SneakyThrows
    public static void main(String[] args) {
        final int brokerMaxStoredMsg = 5;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStoredMsg);

        final Thread producerTh = new Thread(new Producer(messageBroker));
        final Thread consumerTh = new Thread( new Consumer(messageBroker));

        producerTh.start();
        consumerTh.start();

        producerTh.join();
        consumerTh.join();
    }
}
