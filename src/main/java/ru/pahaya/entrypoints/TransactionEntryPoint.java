package ru.pahaya.entrypoints;

import com.google.gson.Gson;
import ru.pahaya.ServiceHolder;
import ru.pahaya.services.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/transaction")
public class TransactionEntryPoint {

    private static final TransactionService TRANSACTION_SERVICE = ServiceHolder.getTransactionService();
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
    public String create(String transaction) {

        return "";
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String delete(String accountJson) {
        return "";
    }
}
