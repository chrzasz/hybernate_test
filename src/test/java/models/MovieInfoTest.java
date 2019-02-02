package models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class MovieInfoTest {

    SessionFactory sessionFactory;

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
    public void shouldPersistGivenMovieInfo() {
        MovieInfo mi1 = new MovieInfo.MovieInfoBuilder()
                .title("Ogniem i Mieczem")
                .avgScore(4.5)
                .build();

        MovieInfo mi2 = new MovieInfo.MovieInfoBuilder()
                .title("Alien vs Predator")
                .avgScore(3.3)
                .build();

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(mi1);
            session.persist(mi2);
            tx.commit();
        }
    }
}
