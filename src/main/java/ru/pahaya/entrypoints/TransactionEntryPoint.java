package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.ServiceHolder;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.Result;
import ru.pahaya.entity.Transaction;
import ru.pahaya.services.AccountService;
import ru.pahaya.services.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Optional;

@Path("/transaction")
public class TransactionEntryPoint {

    private static final TransactionService TRANSACTION_SERVICE = ServiceHolder.getTransactionService();
    private static final AccountService ACCOUNT_SERVICE = ServiceHolder.getAccountService();
    private static final Gson gson = new Gson();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") String id) {
        return "";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String process(String transaction) {
        Transaction tr = gson.fromJson(transaction, Transaction.class);
        Optional<Account> from = ACCOUNT_SERVICE.get(tr.getFromAccount());
        Optional<Account> to = ACCOUNT_SERVICE.get(tr.getToAccount());
        if (from.isEmpty() || to.isEmpty()) {
            return gson.toJson(Result.of(false));
        }
        BigDecimal money = tr.getMoney();
        boolean result = TRANSACTION_SERVICE.process(from.get(), to.get(), money);
        return gson.toJson(Result.of(result));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String refund(String transaction) {
        Transaction tr = gson.fromJson(transaction, Transaction.class);
        return gson.toJson(Result.of(TRANSACTION_SERVICE.refund(tr.getId())));
    }
}
