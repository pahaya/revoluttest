package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.dao.Account;
import ru.pahaya.services.AccountService;
import ru.pahaya.services.SimpleAccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Path("/account")
public class AccountEntryPoint {

    private static final AccountService ACCOUNT_SERVICE = new SimpleAccountService();
    private static final AccountServletValidator ACCOUNT_SERVLET_VALIDATOR = new AccountServletValidator();
    private static final Gson gson = new Gson();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccount(@PathParam("id") String id) {
        AccountServletValidator.validate(id);
        Optional<Account> account = ACCOUNT_SERVICE.get(id);
        if (account.isPresent()) {
            return gson.toJson(account.get());
        } else {
            return "{}";
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAccount(String accountJson) {
        Account account = gson.fromJson(accountJson,Account.class);
        Account accountFromSystem = ACCOUNT_SERVICE.create(account.getId(), account.getMoney());
        return gson.toJson(accountFromSystem);
    }
}
