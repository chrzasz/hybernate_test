package service;

import models.MovieInfo;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DefaultMovieInfoServiceRefactoredTest {

    private String testTitle = "Ogniem i Mieczem";

    MovieInfoServiceRefactored misr =
            new DefaultMovieInfoServiceRefactored();

    @Test
    public void testFindMovieInfo() {
    }

    @Test
    public void testFindOrCreateMovieInfo() {
    }

    @Test
    public void testUpdateMovieInfo() {
    }

    @Test
    public void testDeleteMovieInfo() {
        MovieInfo mi = misr.findOrCreateMovieInfo(testTitle);
        assertNotNull(mi.getId());
        misr.deleteMovieInfo(testTitle);
        mi = misr.findMovieInfo(testTitle);
        assertNull(mi,"This movie should already be removed from DB");
    }
}