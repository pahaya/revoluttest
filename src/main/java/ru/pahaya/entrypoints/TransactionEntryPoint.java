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
import java.util.List;
import java.util.Optional;

/**
 * This is transaction entry point
 */
@Path("/transaction")
public class TransactionEntryPoint {

    private static final TransactionService TRANSACTION_SERVICE = ServiceHolder.getTransactionService();
    private static final AccountService ACCOUNT_SERVICE = ServiceHolder.getAccountService();
    private static final Gson gson = new Gson();

    /**
     * Return Transaction
     * @throws BadRequestException if transaction does not exist
     * @param id of transaction
     * @return Transaction from the storage
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") String id) {
        TransactionValidator.validateIsBlank(id);
        Transaction transaction = TRANSACTION_SERVICE.get(id);
        return gson.toJson(transaction);
    }

    /**
     * Execute money transferring from one account to enother
     * @param transaction transaction object
     * @return JSON representation of Result object
     */
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

    /**
     * Executes refund of transaction
     * @param transaction JSON representation of Transaction object
     * @return JSON representation of Result object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String refund(String transaction) {
        Transaction tr = gson.fromJson(transaction, Transaction.class);
        boolean refundTransaction = TRANSACTION_SERVICE.refund(tr.getId());
        return gson.toJson(Result.of(refundTransaction));
    }

    /**
     * Return Transactions related to this id of the client
     * @throws BadRequestException if transaction does not exist
     * @param id of Client
     * @return JSON representation of Transactions
     */
    @GET
    @Path("client/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTransactions(@PathParam("id") String id) {
        List<Transaction> transaction = TRANSACTION_SERVICE.getByClientId(id);
        return gson.toJson(transaction);
    }
}
