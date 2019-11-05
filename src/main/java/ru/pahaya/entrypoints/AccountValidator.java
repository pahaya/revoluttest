package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.entity.Account;

import javax.ws.rs.BadRequestException;

class AccountValidator {

    private static final Gson gson = new Gson();

    static void validate(String id) {
        if (id == null) {
            throw new BadRequestException("Bad user id !");
        }
    }

    static void validateAccountJson(String accountJson) {
        if (accountJson == null) {
            throw new BadRequestException("Bad account Json!");
        }
    }

    public static void validateAccountDeleteJson(String accountJson) {
        Account account = gson.fromJson(accountJson, Account.class);
        if (account.getId() == null || account.getMoney() == null) {
            throw new BadRequestException("Bad account Json!");
        }
    }
}
