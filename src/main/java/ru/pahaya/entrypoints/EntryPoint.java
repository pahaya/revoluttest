package ru.pahaya.entrypoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/entry-point")
public class EntryPoint {

    @GET
    @Path("test/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@PathParam("id") String id) {
        return id;
    }
}
