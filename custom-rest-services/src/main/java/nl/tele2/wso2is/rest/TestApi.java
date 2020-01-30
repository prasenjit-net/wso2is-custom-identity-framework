package nl.tele2.wso2is.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/me")
@Consumes({"application/json"})
@Produces({"application/json"})
public class TestApi {
    @GET
    public Response getTest() {
        return Response.ok("Hello World").build();
    }
}
