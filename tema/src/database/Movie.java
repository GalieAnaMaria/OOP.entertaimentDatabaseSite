package database;

import java.util.List;
import java.util.Map;

public class Movie extends Show {
    private Map<String, Double> ratings;

    public Movie() {}

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Double> ratings) {
        this.ratings = ratings;
    }
}