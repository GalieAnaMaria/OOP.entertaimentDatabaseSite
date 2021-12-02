package database;

import java.util.Map;

/**
 *  Information about a season of a serial obtained
 *  from input through the DatabaseServices class
 */
public final class Season {
    /**
     * Number of the season
     */
    private int currentSeason;
    /**
     * Time length of the season
     */
    private int duration;
    /**
     * Map containing information regarding which user gave
     * what grade to a season of a serial
     */
    private Map<String, Double> ratings;
    /**
     * Average grade per season
     */
    private Double average;

    public Season() {
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(final int currentSeason) {
        this.currentSeason = currentSeason;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(final Map<String, Double> ratings) {
        this.ratings = ratings;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(final Double average) {
        this.average = average;
    }
}
