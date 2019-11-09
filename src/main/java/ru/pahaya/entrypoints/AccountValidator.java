package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.entity.AccountVO;

import javax.ws.rs.BadRequestException;

/**
 * Validator for the Account Entry point
 */
class AccountValidator {

    private static final Gson gson = new Gson();

    static void validate(String id) {
        if (id.isBlank()) {
            throw new BadRequestException("Bad user id !");
        }
    }

    static void validateAccountDeleteJson(String accountJson) {
        AccountVO account = gson.fromJson(accountJson, AccountVO.class);
        if (account.getId() == null || account.getMoney() == null) {
            throw new BadRequestException("Bad account Json!");
        }
    }

    static void validateMoney(String accountJson) {
        AccountVO account = gson.fromJson(accountJson, AccountVO.class);
        if (account.getMoney() == null) {
            throw new BadRequestException("Bad account Json, does not have money!");
        }
    }
}
