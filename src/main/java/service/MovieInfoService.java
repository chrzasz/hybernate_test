package service;

import models.MovieInfo;
import org.hibernate.Session;

public interface MovieInfoService {
    MovieInfo findMovieInfo(Session session, String title);
    MovieInfo findOrCreateMovieInfo(Session session, String title);
    /**
     * Updates info on movie with given title.
     * If such title does not exist, returns null.
     * @param session
     * @param title
     * @param score - new score
     * @return
     */
    MovieInfo updateMovieInfo(Session session, String title, Double score);
}
