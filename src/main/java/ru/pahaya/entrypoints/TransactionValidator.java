package ru.pahaya.entrypoints;

import javax.ws.rs.BadRequestException;

public class TransactionValidator {

    public static void validateIsBlank(String id) {
        if (id.isBlank()) {
            throw new BadRequestException("Bad user id !");
        }
    }
}
