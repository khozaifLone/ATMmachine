package com.atm.demo.core;

public enum Denomination {

    DENOM_500(500),
    DENOM_100(100);

    private final int value;

    Denomination(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
