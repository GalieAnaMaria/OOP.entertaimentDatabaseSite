package database;

import java.util.ArrayList;
import java.util.Map;

/**
 *  Information about a user of a serial obtained
 *  from input through the DatabaseServices class
 *  This object represents the user requesting
 *  the commands which will have to be executed in their favor
 */
public final class User {
    /**
     * Username for user
     */
    private String username;
    /**
     * Type of subscription (Basic or Premium)
     */
    private String subscriptionType;
    /**
     * History of user containing the name of the show
     * and the number of views (rewatches)
     */
    private Map<String, Integer> history;
    /**
     * List of shows added to favorite
     */
    private ArrayList<String> favorite;
    /**
     * Number of valid and executed ratings
     */
    private int nrValidRatings;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(final String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(final ArrayList<String> favorite) {
        this.favorite = favorite;
    }

    public int getNrValidRatings() {
        return nrValidRatings;
    }

    public void setNrValidRatings(final int nrValidRatings) {
        this.nrValidRatings = nrValidRatings;
    }
}
