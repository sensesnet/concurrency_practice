package org.example.message.secondbroker;

import lombok.SneakyThrows;

public class Runner {

    @SneakyThrows
    public static void main(String[] args) {
        final int brokerMaxStoredMsg = 5;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStoredMsg);
        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducerTh = new Thread(new Producer(messageBroker, messageFactory));
        final Thread secondProducerTh = new Thread(new Producer(messageBroker, messageFactory));
        final Thread thirdProducerTh = new Thread(new Producer(messageBroker, messageFactory));

        final Thread firstConsumerTh = new Thread( new Consumer(messageBroker));
        final Thread secondConsumerTh = new Thread( new Consumer(messageBroker));
        final Thread thirdConsumerTh = new Thread( new Consumer(messageBroker));

        firstProducerTh.start();
        secondProducerTh.start();
        thirdProducerTh.start();

        firstConsumerTh.start();
        secondConsumerTh.start();
        thirdConsumerTh.start();

        firstProducerTh.join();
        secondProducerTh.join();
        thirdProducerTh.join();
        firstConsumerTh.join();
        secondConsumerTh.join();
        thirdConsumerTh.join();
    }
}
