package service;

import models.MovieInfo;
import org.hibernate.Session;

public interface MovieInfoServiceRefactored {
    MovieInfo findMovieInfo(String title);
    MovieInfo findOrCreateMovieInfo(String title);
    MovieInfo updateMovieInfo(String title, Double score);
    void deleteMovieInfo(String title);
}

