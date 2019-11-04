package ru.pahaya.entrypoints;

import javax.ws.rs.BadRequestException;

class AccountServletValidator {

    static void validate(String id) {
        if (id == null) {
            throw new BadRequestException("Bad user id !");
        }
    }
}
