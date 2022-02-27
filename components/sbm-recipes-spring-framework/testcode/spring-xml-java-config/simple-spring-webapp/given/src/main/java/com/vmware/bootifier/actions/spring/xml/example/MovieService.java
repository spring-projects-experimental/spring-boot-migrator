package org.springframework.sbm.actions.spring.xml.example;

public class MovieService {

    private MovieDao movieDao;

    public Movie addMovie(Movie movie) {
        return movieDao.save(movie);
    }

    public void setMovieDao(MovieDao movieDao) {
        this.movieDao = movieDao;
    }
}
