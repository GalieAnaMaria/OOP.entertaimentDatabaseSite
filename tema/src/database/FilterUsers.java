package database;

import common.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterUsers {
    public static String sortAfterNrRatings(Database database, Action command) {
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

        if (command.getSortType().equals(Constants.DESC)) {
            Collections.reverse(filteredList);
        }

        filteredList = filteredList.stream()
                .limit(command.getNumber())
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            return "Query result: []";
        }

        listConvertedToNames = filteredList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: [" + listMessage + "]";
    }
}
