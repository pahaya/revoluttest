package ru.pahaya.entrypoints;

import ru.pahaya.entity.Transaction;

import javax.ws.rs.BadRequestException;

/**
 * Validator for transaction services
 */
class TransactionValidator {

    static void validateIsBlank(String id) {
        if (id.isBlank()) {
            throw new BadRequestException("Bad user id !");
        }
    }

    static void validateTransaction(Transaction tr) {
        if (tr == null) {
            throw new BadRequestException("Transaction is empty !");
        }
        if (tr.getFromAccount() == null || tr.getToAccount() == null) {
            throw new BadRequestException("Some of the accounts is not specified !");
        }
        if (tr.getMoney() == null) {
            throw new BadRequestException("Money is blank !");
        }
    }
}
