package database.commands;

import database.Database;
import database.Movie;
import database.Season;
import database.Serial;
import database.User;

/**
 *  Class designed to handle searches for a specific
 *  type of object throughout the database
 */
public final class Search {
    /**
     * For coding style
     */
    private Search() {
    }

    /**
     *  Method that searches for a user by username in the database
     */
    public static User searchUser(final Database database, final String username) {
        User user = database.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        return user;
    }

    /**
     *  Method that searches for a movie by title in the database
     */
    public static Movie searchMovie(final Database database, final String title) {
        Movie movie = null;
        for (Movie m : database.getMovies()) {
            if (m.getTitle().equals(title)) {
                movie = m;
            }
        }
        return movie;
    }

    /**
     *  Method that searches for a serial by title in the database
     */
    public static Serial searchSerial(final Database database, final String title) {
        Serial serial = null;
        for (Serial s : database.getSerials()) {
            if (s.getTitle().equals(title)) {
                serial = s;
            }
        }
        return serial;
    }

    /**
     *  Method that searches for a season by number of the season for a serial from the database
     */
    public static Season searchSeason(final Serial serial, final int seasonNumber) {
        Season season = null;
        for (Season s : serial.getSeasons()) {
            if (s.getCurrentSeason() == seasonNumber) {
                season = s;
            }
        }
        return season;
    }
}
