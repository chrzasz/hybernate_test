package models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

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

    @Test
    public void shouldRemoveOrphans(){
        Long mi1Id, mc1Id, mc2Id;
        MovieInfo mi1 = new MovieInfo.MovieInfoBuilder()
                .title("Ogniem i mieczem")
                .avgScore(8.5)
                .build();
        MovieCopy mc1 = MovieCopy.builder()
                .movieInfo(mi1)
                .build();
        MovieCopy mc2 = MovieCopy.builder()
                .movieInfo(mi1)
                .build();
        mi1.setCopies(Arrays.asList(mc1, mc2));

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            //cascade persist is on, should save related entities with this one operation
            session.persist(mi1);
            tx.commit();
            //IDs should be set for persisted entities
            mi1Id = mi1.getId();
            assertNotNull(mi1Id);
            mc1Id = mc1.getId();
            assertNotNull(mc1Id);
            mc2Id = mc2.getId();
            assertNotNull(mc2Id);
        }

        //new session
        try(Session session = sessionFactory.openSession()) {
            //load entities from DB by IDs
            mi1 = session.get(MovieInfo.class, mi1Id);
            assertNotNull(mi1);
            mc1 = session.get(MovieCopy.class, mc1Id);
            assertNotNull(mc1);
            mc2 = session.get(MovieCopy.class, mc2Id);
            assertNotNull(mc2);
            //open transaction to modify DB
            Transaction tx = session.beginTransaction();
            session.delete(mi1);    //orphan removal is on, should remove orphans
            tx.commit();
        }

        //start new session to check if entites were removed from DB
        try(Session session = sessionFactory.openSession()) {
            mi1 = session.get(MovieInfo.class, mi1Id);
            assertNull(mi1);
            mc1 = session.get(MovieCopy.class, mc1Id);
            assertNull(mc1);
            mc2 = session.get(MovieCopy.class, mc2Id);
            assertNull(mc2);
        }
    }

    @Test
    public void daysSinceReleaseShouldBeCalculatedPostLoad() {
        Long mId;
        MovieInfo mi1 = new MovieInfo.MovieInfoBuilder()
                .title("Ogniem i mieczem")
                .avgScore(8.5)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .build();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(mi1);
            tx.commit();
            //IDs should be set for persisted entities
            mId = mi1.getId();
            assertNotNull(mId);
            assertNull(mi1.getDaysSinceRelease());
        }
        try(Session session = sessionFactory.openSession()) {
            mi1 = session.get(MovieInfo.class, mId);
            assertNotNull(mi1.getDaysSinceRelease());
            System.out.println("Dni od premiery: " + mi1.getDaysSinceRelease());
        }
    }
}
