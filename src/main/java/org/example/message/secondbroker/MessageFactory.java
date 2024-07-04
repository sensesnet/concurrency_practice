package org.example.message.secondbroker;

public final class MessageFactory {
    public static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
    public static final String TEMPLATE = "Message#%d";
    private int nextMessageIndex;

    public MessageFactory() {
        this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
    }

    public Message createMessage() {
        return new Message(String.format(TEMPLATE, increment()));
    }

    private synchronized int increment() {
        return nextMessageIndex++;
    }
}
