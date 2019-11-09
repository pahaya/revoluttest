package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.ServiceHolder;
import ru.pahaya.entity.Account;
import ru.pahaya.entity.AccountVO;
import ru.pahaya.services.AccountService;
import ru.pahaya.services.SimpleAccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Entry point for the Account rest service
 */
@Path("/account")
public class AccountEntryPoint {

    private static final AccountService ACCOUNT_SERVICE = ServiceHolder.getAccountService();
    private static final Gson gson = new Gson();

    /**
     * Get information about client with specific id
     * @param id id of the client
     * @return Client in JSON representation
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") String id) {
        AccountValidator.validate(id);
        Optional<Account> account = ACCOUNT_SERVICE.get(id);
        if (account.isPresent()) {
            return gson.toJson(account.get());
        } else {
            return "{}";
        }
    }

    /**
     * Creating account
     * @param accountJson AccountVO object in JSON representation
     * @return created account
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String create(String accountJson) {
        AccountValidator.validate(accountJson);
        AccountValidator.validateMoney(accountJson);
        AccountVO accountFromJson = gson.fromJson(accountJson, AccountVO.class);
        Account accountFromSystem = accountFromJson.getId() != null ?
                ACCOUNT_SERVICE.create(accountFromJson.getId(), accountFromJson.getMoney()) :
                ACCOUNT_SERVICE.create(accountFromJson.getMoney());
        return gson.toJson(accountFromSystem);
    }

    /**
     * Removing account from the system
     * @param accountJson account for deletion
     * @return result of operation
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete(String accountJson) {
        AccountValidator.validateAccountDeleteJson(accountJson);
        AccountVO account = gson.fromJson(accountJson, AccountVO.class);
        boolean result = ACCOUNT_SERVICE.delete(account.getId());
        return "{ \"result\" : " + result + "}";
    }
}
