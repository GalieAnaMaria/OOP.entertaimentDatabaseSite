package database;

import java.util.ArrayList;

/**
 *  Information about a serial obtained from
 *  input through the DatabaseServices class
 */
public final class Serial extends Show {
    /**
     * Number of current seasons for each serial
     */
    private int numberOfSeasons;
    /**
     * List of season objects containing the season information
     */
    private ArrayList<Season> seasons;

    public Serial() {
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(final int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(final ArrayList<Season> seasons) {
        this.seasons = seasons;
    }
}
