package com.seatallocation.exception;

public class ContiguousSeatsUnavailableException extends RuntimeException {

    public ContiguousSeatsUnavailableException(String message) {
        super(message);
    }
}
