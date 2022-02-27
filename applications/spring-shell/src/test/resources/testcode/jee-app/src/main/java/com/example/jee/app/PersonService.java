package com.example.jee.app;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PersonService {

    @PersistenceContext
    private EntityManager entityManager;

    public void addPerson(Person person) throws Exception {
        entityManager.persist(person);
    }

    public void deleteMovie(Person person) throws Exception {
        person = entityManager.find(Person.class, person.getId());
        entityManager.remove(person);
    }

    public List<Person> getPersons() throws Exception {
        Query query = entityManager.createQuery("SELECT m from Person as m");
        return query.getResultList();
    }

}
