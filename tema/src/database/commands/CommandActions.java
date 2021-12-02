package database.commands;

import common.Constants;
import database.Action;
import database.Database;
import database.Movie;
import database.Season;
import database.Serial;
import database.User;

/**
 * Class with fixed functionality of executing
 * action types representing a concrete command
 * (favourite-ing a show, adding a show to 'viewed', rating a show)
 */
public final class CommandActions {
    /**
     * For coding style
     */
    private CommandActions() {
    }

    /**
     * Method redirecting the command depending on the command's type requested
     * to the afferent methods designed to handle the action
     * If the action is undefined the user will only get a blank message
     */
    public static String executeCommand(final Database database, final Action command) {
        switch (command.getType()) {
            case Constants.FAVORITE:
                return favorite(database, command);
            case Constants.VIEW:
                return view(database, command);
            case Constants.RATING:
                return rate(database, command);
            default:
                return "";
        }
    }

    /**
     *  Method adding a wanted show by a user to their favorite list
     *  If the show is nonexistent in the user's history or already in their favorite list
     *  this action will not be executed, and it will throw an error
     */
    private static String favorite(final Database database, final Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (user.getHistory().containsKey(command.getTitle())) {
            if (user.getFavorite().contains(command.getTitle())) {
                return "error -> "
                        + command.getTitle()
                        + " is already in favourite list";
            } else {
                user.getFavorite().add(command.getTitle());
                Util.increaseCounterShow(database, command);

                return "success -> "
                        + command.getTitle()
                        + " was added as favourite";
            }
        } else {
            return "error -> "
                    + command.getTitle()
                    + " is not seen";
        }
    }

    /**
     * Method adding a show to the user's view list
     * If the show was seen already and the user rewatched it, the show's
     * view count, respectively the view field in the user's history, will be increased
     */
    private static String view(final Database database, final Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (user.getHistory().containsKey(command.getTitle())) {
            user.getHistory().put(command.getTitle(),
                    user.getHistory().get(command.getTitle()) + 1);
            return "success -> "
                    + command.getTitle()
                    + " was viewed with total views of "
                    + user.getHistory().get(command.getTitle());
        } else {
            user.getHistory().put(command.getTitle(), 1);
            return "success -> "
                    + command.getTitle()
                    + " was viewed with total views of " + "1";
        }
    }

    /**
     * Method rating each show with specific criteria depending on the shows
     * type (movie or serial)
     *  If the show is nonexistent in the user's history or has been already rated
     *  this action will not be executed, and it will throw an error
     */
    private static String rate(final Database database, final Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (!user.getHistory().containsKey(command.getTitle())) {
            return "error -> "
                    + command.getTitle()
                    + " is not seen";
        }

        if (command.getSeasonNumber() == 0) {
            Movie movie = Search.searchMovie(database, command.getTitle());

            if (movie.getRatings().containsKey(command.getUsername())) {
                return "error -> "
                        + command.getTitle()
                        + " has been already rated";
            } else {
                movie.getRatings().put(command.getUsername(), command.getGrade());
                user.setNrValidRatings(user.getNrValidRatings() + 1);

                return "success -> "
                        + command.getTitle()
                        + " was rated with "
                        + command.getGrade()
                        + " by "
                        + command.getUsername();
            }
        }

        if (command.getSeasonNumber() != 0) {
            Serial serial = Search.searchSerial(database, command.getTitle());
            Season season = Search.searchSeason(serial, command.getSeasonNumber());

            if (season.getRatings().containsKey(command.getUsername())) {
                return "error -> "
                        + command.getTitle()
                        + " has been already rated";
            } else {
                season.getRatings().put(command.getUsername(), command.getGrade());
                user.setNrValidRatings(user.getNrValidRatings() + 1);

                return "success -> "
                        + command.getTitle()
                        + " was rated with "
                        + command.getGrade()
                        + " by "
                        + command.getUsername();
            }
        }

        return "";
    }
}
