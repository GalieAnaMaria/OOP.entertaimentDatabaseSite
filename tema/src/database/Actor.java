package database;

import actor.ActorsAwards;
import java.util.ArrayList;
import java.util.Map;

/**
 *  Information about an actor obtained from
 *  input through the DatabaseServices class
 */
public final class Actor {
    /**
     * Actor name
     */
    private String name;
    /**
     * Text description for the actor's career
     */
    private String careerDescription;
    /**
     * Movies in which the actor was featured
     */
    private ArrayList<String> filmography;
    /**
     * Awards won by actor
     */
    private Map<ActorsAwards, Integer> awards;
    /**
     * The rating average of the actor's filmography
     */
    private double averageFilmography;
    /**
     * The numbers of awards won (total or per genre)
     */
    private int awardsNumber;

    public Actor() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public void setAwards(final Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public double getAverageFilmography() {
        return averageFilmography;
    }

    public void setAverageFilmography(final double averageFilmography) {
        this.averageFilmography = averageFilmography;
    }

    public int getAwardsNumber() {
        return awardsNumber;
    }

    public void setAwardsNumber(final int awardsNumber) {
        this.awardsNumber = awardsNumber;
    }
}
