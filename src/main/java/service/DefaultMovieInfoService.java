package service;

import models.MovieInfo;
import org.hibernate.Session;

import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DefaultMovieInfoService implements MovieInfoService {

    @Override
    public MovieInfo findMovieInfo(Session session, String title) {
        Query<MovieInfo> query = session.createQuery(
                "from MovieInfo mi where mi.title=:title", MovieInfo.class);
        query.setParameter("title", title);
        return query.uniqueResult();
    }

    @Override
    public MovieInfo findOrCreateMovieInfo(Session session, String title) {
        MovieInfo mi = findMovieInfo(session, title);
        if (mi == null) {
            mi = new MovieInfo();
            mi.setTitle(title);
            session.save(mi);
        }
        return mi;
    }

    @Override
    public MovieInfo updateMovieInfo(Session session, String title, Double score) {
        MovieInfo mi = findMovieInfo(session, title);
        if(mi == null) {
            return null;    //see the docs
        }
        Transaction tx = session.beginTransaction();
        mi.setAvgScore(score);
        tx.commit();
        return mi;
    }
}
