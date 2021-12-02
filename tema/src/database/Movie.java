package database;

import java.util.Map;

/**
 *  Information about a movie obtained from
 *  input through the DatabaseServices class
 */
public final class Movie extends Show {
    /**
     * Map containing information regarding which user gave
     * what grade to a movie
     */
    private Map<String, Double> ratings;

    public Movie() {
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(final Map<String, Double> ratings) {
        this.ratings = ratings;
    }
}
