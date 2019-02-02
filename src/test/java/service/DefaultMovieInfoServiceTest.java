package service;

import models.MovieInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DefaultMovieInfoServiceTest {

    private String testTitle = "Ogniem i Mieczem";
    private String testTitle2 = "Alien vs Predator";

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

        insertMoviesIntoDb();
    }

    //run this after every method execution
    @AfterTest
    public void cleanUpDB() {}


    @Test
    public void shouldPersistGivenMovies() {
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findMovieInfo(session, testTitle);
            assertNotNull(mi.getId(), "Hibernate should set ID for this instance already");
            assertEquals(mi.getTitle(), testTitle);
            assertEquals(mi.getAvgScore(), 8.5, "Score for this movie should be 8.5!");
        }
    }

    //oba testy maja sprawdzac dzialanie metod findOrCreate
    @Test
    public void shouldFindCreatedMovie() {
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, testTitle);
            assertNotNull(mi.getAvgScore(), "Score should not be null");
        }
    }

    @Test
    public void shouldCreateNotFoundMovie() {
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, "Dzieci z dworca ZOO");
            assertNull(mi.getAvgScore(), "Score should be null");
            assertEquals(mi.getTitle(), "Dzieci z dworca ZOO");
            assertNotNull(mi.getId(), "ID should already exist");
        }
    }

    @Test
    public void shouldUpdateAvgScoreForMovies() {
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, testTitle);
            assertEquals(mi.getAvgScore(), 8.5);
            movieInfoService.updateMovieInfo(session, testTitle, 8.23);
        }
        try (Session session = sessionFactory.openSession()) {
            MovieInfo mi = movieInfoService.findOrCreateMovieInfo(session, testTitle);
            assertEquals(mi.getAvgScore(), 8.23);
        }
    }

    private MovieInfo createMovieInfo(final String title, final Double avgScore) {
        return MovieInfo.builder()
                .title(title)
                .avgScore(avgScore)
                .build();
    }

    private void insertMoviesIntoDb() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(createMovieInfo(testTitle, 8.5));
            session.persist(createMovieInfo(testTitle2, 9.5));
            session.persist(createMovieInfo("JAX", 3.5));
            tx.commit();
        }
    }


}
