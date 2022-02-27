package org.springframework.sbm.actions.spring.xml.example;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MovieDao {

    @PersistenceContext
    private EntityManager em;

    public Movie save(Movie movie) {
        em.persist(movie);
        return movie;
    }
}
