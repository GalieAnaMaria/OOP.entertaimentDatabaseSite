package database.commands;

import common.Constants;
import database.Action;
import database.Database;
import database.User;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Class designed to handle filtering command for users
 *  according to the number of activity they had regarding their favourite list
 */
public final class FilterUsers {
    /**
     * For coding style
     */
    private FilterUsers() {
    }

    /**
     *  Method that filters the users from the database according
     *  to the number of shows they inserted in their favourite list
     *  along with an alphabetical order based on their username
     */
    public static String sortAfterNrRatings(final Database database, final Action command) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

        Comparator<User> userRatingComparator
                = Comparator.comparingInt(User::getNrValidRatings);

        Comparator<User> userNameComparator
                = Comparator.comparing(User::getUsername);

        List<User> filteredList = database.getUsers().stream()
                .filter(u -> u.getNrValidRatings() != 0)
                .sorted(userNameComparator)
                .sorted(userRatingComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method reversing the list if requested so specifically
     *  and trims the list however much it has been requested
     */
    private static String finalOrdering(final Action command, List<User> filteredList) {
        if (command.getSortType().equals(Constants.DESC)) {
            Collections.reverse(filteredList);
        }

        filteredList = filteredList.stream()
                .limit(command.getNumber())
                .collect(Collectors.toList());

        return convertListToString(filteredList);
    }

    /**
     *  Method converting the given list into a string message
     *  containing the usernames' of the users in it
     *  If the list is empty it will show a message with no names
     */
    private static String convertListToString(final List<User> filteredList) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

        if (filteredList.isEmpty()) {
            return "Query result: []";
        }

        listConvertedToNames = filteredList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: ["
                + listMessage
                + "]";
    }
}
