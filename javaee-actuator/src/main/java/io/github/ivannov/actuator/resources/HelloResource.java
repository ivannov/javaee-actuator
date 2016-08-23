package io.github.ivannov.actuator.resources;

import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloResource {

    @GET
    @Produces("application/json")
    public Response sayHello() {
        return Response.ok(Json.createObjectBuilder().add("hello", "world").build()).build();
    }
}
