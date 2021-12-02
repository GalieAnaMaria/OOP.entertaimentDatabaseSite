package database;

import java.util.ArrayList;
import java.util.List;

public class Serial extends Show {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Serial() {}

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }
}