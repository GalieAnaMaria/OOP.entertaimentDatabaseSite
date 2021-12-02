package database.commands;

import common.Constants;
import database.Action;
import database.Database;
import database.Show;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with fixed functionality of executing
 * action types representing a concrete filtered search
 * in the database for the specified object types
 */
public final class QueryAction {
    /**
     * For coding style
     */
    private QueryAction() {
    }

    /**
     * Method redirecting the command, depending on the object type requested
     * for the filtered search, to the afferent methods designed to handle the action
     * If the object is undefined the user will only get a blank message
     */
    public static String executeQuery(final Database database, final Action command) {
        switch (command.getObjectType()) {
            case Constants.USERS:
                return FilterUsers.sortAfterNrRatings(database, command);
            case Constants.ACTORS:
                return FilterActors.filterActors(database, command);
            case Constants.MOVIES:
                List<Show> moviesAsShowList = new ArrayList<>();
                moviesAsShowList.addAll(database.getMovies());
                return FilterShows.filter(database, command, moviesAsShowList);
            case Constants.SHOWS:
                List<Show> serialsAsShowList = new ArrayList<>();
                serialsAsShowList.addAll(database.getSerials());
                return FilterShows.filter(database, command, serialsAsShowList);
            default:
                return " ";
        }
    }
}
