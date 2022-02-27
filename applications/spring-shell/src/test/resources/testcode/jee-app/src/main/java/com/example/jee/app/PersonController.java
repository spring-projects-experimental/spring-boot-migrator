package com.example.jee.app;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;
import com.example.jee.app.ejb.local.ABusinessInterface;

/**
 * A simple REST service which is able to say hello to someone using HelloService Please take a look at the web.xml.orig where JAX-RS
 * is enabled And notice the @PathParam which expects the URL to contain /json/David or /xml/Mary
 *
 * @author bsutter@redhat.com
 */

@Path("/")
public class PersonController {
    @Inject
    private PersonService personService;

    @Inject
    private ABusinessInterface a;


    @GET
    @Path("/a")
    @Produces("text/plain")
    public String getAnA() {
        return a.businessMethod();
    }

    @POST
    @Path("/json/{name}")
    @Produces("application/json")
    @Consumes("application/json")
    public String getHelloWorldJSON(@PathParam("name") String name) throws Exception {
        System.out.println("name: " + name);

        /*
        Movie movie = new Movie();
        movie.setDirector("Robert Zemeckis");
        movie.setTitle("Forest Gump");
        movie.setYear(1994);
        movies.addMovie(movie);
        Response response = Response.status(Response.Status.OK).entity(movie).build();
        Object entity = response.getEntity();
        System.out.println(response.readEntity(String.class).toString());
        */
        Person person = new Person(name);
        personService.addPerson(person);
        return person.getId() + ":" + person.getName(); // .readEntity(String.class);
        // return "{\"result\":\"" + movies.createHelloMessage(name) + "\"}";
    }

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllPersons() throws Exception {
        /*
        Movie movie = new Movie();
        movie.setDirector("Robert Zemeckis");
        movie.setTitle("Forest Gump");
        movie.setYear(1994);
        movies.addMovie(movie);
        Response response = Response.status(Response.Status.OK).entity(movie).build();
        Object entity = response.getEntity();
        System.out.println(response.readEntity(String.class).toString());
        */
        String personsFound = personService.getPersons().stream().map(Person::getName).collect(Collectors.joining(", "));
        return personsFound;
//        List<String> personsFound = personService.getPersons().stream().map(Person::getName).collect(Collectors.toList());
//        Response response = Response.status(Response.Status.OK).entity(personsFound).build();
//        return response;
//        Person person = new Person(name);
//        personService.addPerson(person);
//        return  person.getName(); // .readEntity(String.class);
        // return "{\"result\":\"" + movies.createHelloMessage(name) + "\"}";
    }


    @POST
    @Path("/xml/{name}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String getHelloWorldXML(@PathParam("name") String name) throws Exception {
        System.out.println("name: " + name);
        /*
        Movie movie = new Movie();
        movie.setDirector("Robert Zemeckis");
        movie.setTitle("Forest Gump");
        movie.setYear(1994);
        movies.addMovie(movie);
        Response response = Response.status(Response.Status.OK).entity(movie).build();
        Object entity = response.getEntity();
        System.out.println(response.readEntity(String.class).toString());
        */
        Person person = new Person(name);
        personService.addPerson(person);
        return person.getName(); // .readEntity(String.class);
        // return "{\"result\":\"" + movies.createHelloMessage(name) + "\"}";
    }

}