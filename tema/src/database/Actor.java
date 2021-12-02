package database;

import actor.ActorsAwards;
import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double averageFilmography;
    private int awardsNumber;

    public Actor() {}

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

    public void setAwards(Map<ActorsAwards, Integer> awards) {
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

    public void setAverageFilmography(double averageFilmography) {
        this.averageFilmography = averageFilmography;
    }

    public int getAwardsNumber() {
        return awardsNumber;
    }

    public void setAwardsNumber(int awardsNumber) {
        this.awardsNumber = awardsNumber;
    }
}