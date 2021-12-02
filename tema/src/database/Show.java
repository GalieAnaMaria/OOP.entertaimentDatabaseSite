package database;

import java.util.ArrayList;

/**
 *  Information about a show obtained from
 *  input through the DatabaseServices class
 *  This class will hold most information about
 *  movies and serials included
 */
public abstract class Show {
    /**
     * Title of the show
     */
    private String title;
    /**
     * Year it got released
     */
    private int year;
    /**
     * List of actors names who played in the show
     */
    private ArrayList<String> cast;
    /**
     * List of genres the show is categorized in
     */
    private ArrayList<String> genres;
    /**
     * Average grade of all the ratings received
     */
    private Double average;
    /**
     * Counter of times the show got added into a favorite list
     */
    private int favoriteCounter;
    /**
     * Counter of views (including rewatches by users)
     */
    private int views;
    /**
     * ID of insertion from the input into the database
     */
    private int id;
    /**
     * Time length of the show total
     */
    private int duration;

    public Show() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(final int year) {
        this.year = year;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public void setCast(final ArrayList<String> cast) {
        this.cast = cast;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(final Double average) {
        this.average = average;
    }

    public int getFavoriteCounter() {
        return favoriteCounter;
    }

    public void setFavoriteCounter(final int favoriteCounter) {
        this.favoriteCounter = favoriteCounter;
    }

    public int getViews() {
        return views;
    }

    public void setViews(final int views) {
        this.views = views;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }
}
