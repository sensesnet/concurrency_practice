package org.example.message.secondbroker;

import lombok.SneakyThrows;

import java.util.Arrays;

public class Runner {

    @SneakyThrows
    public static void main(String[] args) {
        final int brokerMaxStoredMsg = 15;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStoredMsg);
        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducerTh = new Thread(
                new MessageProducingTask(
                        messageBroker, messageFactory,
                        15, "PRODUCER_1"));
        final Thread secondProducerTh = new Thread(
                new MessageProducingTask(
                        messageBroker, messageFactory,
                        10, "PRODUCER_2"));
        final Thread thirdProducerTh = new Thread(
                new MessageProducingTask(
                        messageBroker, messageFactory,
                        5, "PRODUCER_3"));

        final Thread firstConsumerTh = new Thread(
                new MessageConsumingTask(
                        messageBroker,
                        0,"CONSUMER_1"));
        final Thread secondConsumerTh = new Thread(
                new MessageConsumingTask(messageBroker,
                        6,"CONSUMER_2"));
        final Thread thirdConsumerTh = new Thread(
                new MessageConsumingTask(messageBroker,
                        11,"CONSUMER_3"));

        firstProducerTh.start();
        secondProducerTh.start();
        thirdProducerTh.start();

        firstConsumerTh.start();
        secondConsumerTh.start();
        thirdConsumerTh.start();

        startThreads(firstProducerTh, secondProducerTh, thirdProducerTh,
                firstConsumerTh, secondConsumerTh, thirdConsumerTh);

        firstProducerTh.join();
        secondProducerTh.join();
        thirdProducerTh.join();
        firstConsumerTh.join();
        secondConsumerTh.join();
        thirdConsumerTh.join();
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}
