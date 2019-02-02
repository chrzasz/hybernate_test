package service;

import models.MovieInfo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.SessionUtil;

public class DefaultMovieInfoServiceRefactored implements MovieInfoServiceRefactored {


    @Override
    public MovieInfo findMovieInfo(String title) {
        try (Session session = SessionUtil.getSession()) {
            Query<MovieInfo> query = session.createQuery(
                    "from MovieInfo mi where mi.title=:title", MovieInfo.class);
            query.setParameter("title", title);
            return query.uniqueResult();
        }
    }

    @Override
    public MovieInfo findOrCreateMovieInfo(String title) {
        try (Session session = SessionUtil.getSession()) {
            MovieInfo mi = findMovieInfo(title);
            if (mi == null) {
                Transaction tx = session.beginTransaction();
                mi = new MovieInfo();
                mi.setTitle(title);
                session.save(mi);
                tx.commit();
            }
            return mi;
        }

    }

    @Override
    public MovieInfo updateMovieInfo(String title, Double score) {
        try (Session session = SessionUtil.getSession()) {
            MovieInfo mi = findMovieInfo(title);
            if (mi == null) {
                return null;    //see the docs
            }
            Transaction tx = session.beginTransaction();
            mi.setAvgScore(score);
            tx.commit();
            return mi;
        }

    }

    @Override
    public void deleteMovieInfo(String title) {
        MovieInfo mi = findMovieInfo(title);
        if (mi != null) {
            try (Session session = SessionUtil.getSession()) {
                Transaction tx = session.beginTransaction();
                session.delete(mi);
                tx.commit();
            }
        }
    }
}
