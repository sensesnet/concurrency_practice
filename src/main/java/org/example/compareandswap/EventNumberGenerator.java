package org.example.compareandswap;

import java.util.concurrent.atomic.AtomicInteger;

public class EventNumberGenerator {
    public static final int GENERATOR_DELTA = 2;
    private final AtomicInteger value = new AtomicInteger();

    public int generate() {
        return this.value.getAndAdd(GENERATOR_DELTA);
    }

    public int getValue() {
        return value.intValue();
    }
}
