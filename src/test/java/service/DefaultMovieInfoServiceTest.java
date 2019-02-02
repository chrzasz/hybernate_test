package service;

import models.MovieInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DefaultMovieInfoServiceTest {

    SessionFactory sessionFactory;
    MovieInfoService movieInfoService = new DefaultMovieInfoService();

    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    public void shouldPersistGivenMovies() {

        insertMoviesIntoDb();
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findMovieInfo(session, "Ogniem i Mieczem");
            assertNotNull(mi.getId(), "Hibernate should set ID for this instance already");
            assertEquals(mi.getTitle(), "Ogniem i Mieczem");
            assertEquals(mi.getAvgScore(), 8.5, "Score for this movie should be 8.5!");
        }

    }
    //oba testy maja sprawdzac dzialanie metod findOrCreate
    @Test
    public void shouldFindCreatedMovie() {
        insertMoviesIntoDb();
        try(Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, "Ogniem i Mieczem");
            assertNotNull(mi.getAvgScore(), "Score should not be null");
        }
    }

    @Test
    public void shouldCreateNotFoundMovie() {
        try(Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, "Ogniem i Mieczem");
            assertNull(mi.getAvgScore(), "Score should be null");
            assertEquals(mi.getTitle(), "Ogniem i Mieczem");
            assertNotNull(mi.getId(), "ID should already exist");
        }
    }

    @Test

    private MovieInfo createMovieInfo(final String title, final Double avgScore) {
        return MovieInfo.builder()
                .title(title)
                .avgScore(avgScore)
                .build();
    }

    private void insertMoviesIntoDb() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(createMovieInfo("Ogniem i Mieczem", 8.5));
            session.persist(createMovieInfo("Alien vs Predator", 9.5));
            session.persist(createMovieInfo("JAX", 3.5));
            tx.commit();
        }
    }


}
