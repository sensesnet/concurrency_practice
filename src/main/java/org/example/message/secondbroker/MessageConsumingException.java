package org.example.message.secondbroker;

public class MessageConsumingException extends RuntimeException {
    public MessageConsumingException() {
    }

    public MessageConsumingException(String message) {
        super(message);
    }

    public MessageConsumingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConsumingException(Throwable cause) {
        super(cause);
    }

    public MessageConsumingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
