package com.example.jee.app;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

@Path("/")
public class PersonController {

    @POST
    @Path("/json/{name}")
    @Produces("application/json")
    @Consumes("application/json")
    public String getHelloWorldJSON(@PathParam("name") String name) throws Exception {
        System.out.println("name: " + name);
        return "{\"Hello\":\"" + name + "\"";
    }

    @GET
    @Path("/json")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public String getAllPersons(@QueryParam("q") String searchBy, @DefaultValue("0") @QueryParam("page") int page) throws Exception {
        return "{\"message\":\"No person here...\"";
    }


    @POST
    @Path("/xml/{name}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String getHelloWorldXML(@PathParam("name") String name) throws Exception {
        System.out.println("name: " + name);
        return "<xml>Hello "+name+"</xml>";
    }

    private boolean isResponseStatusSuccessful(ContainerResponseContext responseContext) {
        return responseContext.getStatusInfo().getFamily() == SUCCESSFUL;
    }

}
