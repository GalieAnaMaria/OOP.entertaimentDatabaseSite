package database;

import common.Constants;
import database.User;
import java.util.ArrayList;
import java.util.Map;

public class CommandActions {
    public static String executeCommand(Database database, Action command) {
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

    private static String favorite(Database database, Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (user.getHistory().containsKey(command.getTitle())) {
            if (user.getFavorite().contains(command.getTitle())) {
                return "error -> " + command.getTitle() + " is already in favourite list";
            }
            else {
                user.getFavorite().add(command.getTitle());
                Util.increaseCounterShow(database, command);

                return "success -> " + command.getTitle() + " was added as favourite";
            }
        }
        else {
            return "error -> " + command.getTitle() + " is not seen";
        }
    }

    private static String view(Database database, Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (user.getHistory().containsKey(command.getTitle())) {
            user.getHistory().put(command.getTitle(), user.getHistory().get(command.getTitle()) + 1);
            return "success -> " + command.getTitle() +
                    " was viewed with total views of " + user.getHistory().get(command.getTitle());
        }
        else {
            user.getHistory().put(command.getTitle(), 1);
            return "success -> " + command.getTitle() + " was viewed with total views of " + "1";
        }
    }

    private static String rate(Database database, Action command) {
        User user = Search.searchUser(database, command.getUsername());

        if (!user.getHistory().containsKey(command.getTitle())) {
            return "error -> " + command.getTitle() + " is not seen";
        }

        if (command.getSeasonNumber() == 0) {
            Movie movie = Search.searchMovie(database, command.getTitle());

            if (movie.getRatings().containsKey(command.getUsername())) {
                return  "error -> " + command.getTitle() + " has been already rated";
            }
            else {
                movie.getRatings().put(command.getUsername(), command.getGrade());
                user.setNrValidRatings(user.getNrValidRatings() + 1);

                return   "success -> " + command.getTitle() + " was rated with " +
                        command.getGrade() + " by " + command.getUsername();
            }
        }

        if (command.getSeasonNumber() != 0) {
            Serial serial = Search.searchSerial(database, command.getTitle());
            Season season = Search.searchSeason(serial, command.getSeasonNumber());

            if (season.getRatings().containsKey(command.getUsername())) {
                return  "error -> " + command.getTitle() + " has been already rated";
            }
            else {
                season.getRatings().put(command.getUsername(), command.getGrade());
                user.setNrValidRatings(user.getNrValidRatings() + 1);

                return   "success -> " + command.getTitle() + " was rated with " +
                        command.getGrade() + " by " + command.getUsername();
            }
        }

        return "";
    }
}
