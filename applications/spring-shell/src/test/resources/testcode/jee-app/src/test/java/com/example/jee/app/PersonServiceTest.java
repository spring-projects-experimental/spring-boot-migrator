package com.example.jee.app;

import junit.framework.TestCase;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.List;
import java.util.Properties;

/**
 * @version $Revision: 607077 $ $Date: 2007-12-27 06:55:23 -0800 (Thu, 27 Dec 2007) $
 */
public class PersonServiceTest extends TestCase {

    public void test() throws Exception {
        System.setProperty("hsqldb.reconfig_logging", "false");
        System.setProperty("tomee.jpa.factory.lazy", "true");

        final Properties p = new Properties();
        p.put("movieDatabase", "new://Resource?type=DataSource");
        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.getEnvironment().entrySet().stream().forEach(es -> System.out.println(es.getValue()));
        PersonService personService = (PersonService) context.lookup("java:global/jee-app/PersonService");

        personService.addPerson(new Person("Quentin Tarantino"));
        personService.addPerson(new Person("Joel Coen"));

        List<Person> list = personService.getPersons();
        assertEquals("List.size()", 2, list.size());

        for (Person person : list) {
            personService.deleteMovie(person);
        }

        assertEquals("Movies.getMovies()", 0, personService.getPersons().size());
    }
}
