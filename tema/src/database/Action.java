package database;

import java.util.ArrayList;
import java.util.List;

/**
 *  Information about an action obtained from
 *  input through the DatabaseServices class
 *  The object type that will hold the commands which will be executed
 */
public final class Action {
    /**
     * ID of the action
     */
    private int actionId;
    /**
     * Command type at request by user
     */
    private String actionType;
    /**
     *  Type of command
     */
    private String type;
    /**
     * Username for whom the action is executed for
     */
    private String username;
    /**
     * Object type on which the action will be applied on
     */
    private String objectType;
    /**
     * Sorting ascending or descending
     */
    private String sortType;
    /**
     * Criteria used for ordering in queries
     */
    private String criteria;
    /**
     * Tittle of the show on which the command to be executed on
     */
    private String title;
    /**
     * Genre for recommendations
     */
    private String genre;
    /**
     * First number of research results
     */
    private int number;
    /**
     * Grade wished to be given to a show
     */
    private double grade;
    /**
     * Number of the season wished to be added to the favorite list of the user
     */
    private int seasonNumber;
    /**
     * Filters which will be chosen for searching shows
     */
    private List<List<String>> filters = new ArrayList<>();

    public Action() {
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(final int actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(final String sortType) {
        this.sortType = sortType;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(final String criteria) {
        this.criteria = criteria;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(final double grade) {
        this.grade = grade;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(final int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public List<List<String>> getFilters() {
        return filters;
    }

    public void setFilters(final List<List<String>> filters) {
        this.filters = filters;
    }
}
