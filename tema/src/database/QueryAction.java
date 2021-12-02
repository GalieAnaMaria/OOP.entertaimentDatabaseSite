package database;

import common.Constants;

public class QueryAction {
    public static String executeQuery(Database database, Action command) {
        switch (command.getObjectType()) {
            case Constants.USERS:
                return FilterUsers.sortAfterNrRatings(database, command);
            case Constants.ACTORS:
                return FilterActors.filterActors(database, command);
            case Constants.MOVIES:
                return FilterMovies.filterMovieYearGenre(database, command);
            case Constants.SHOWS:
                return FilterShows.filterSerialYearGenre(database, command);
            default:
                return " ";
        }
    }
}
