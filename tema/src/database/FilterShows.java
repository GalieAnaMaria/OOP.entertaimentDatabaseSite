package database;

import common.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterShows {
    public static String filterSerialYearGenre(Database database, Action command) {
        Comparator<Serial> serialNameComparator
                = Comparator.comparing(Serial::getTitle);

        List<Serial> filteredList = filterYearGenreName(database, command,
                serialNameComparator);

        switch (command.getCriteria()) {
            case Constants.FAVORITE:
                return filterByFavorite(database, command, filteredList);
            case Constants.RATINGS:
                return filterByRating(database, command, filteredList);
            case Constants.LONGEST:
                return filterByDuration(database, command, filteredList);
            case Constants.MOST_VIEWED:
                return filterByViews(database, command, filteredList);
            default:
                return "";
        }
    }

    private static String convert(Action command, List<Serial> filteredList) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

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
                .map(Serial::getTitle)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: [" + listMessage + "]";
    }

    private static List<Serial> filterYearGenreName(Database database, Action command,
                                                   Comparator<Serial> serialNameComparator) {
        if (command.getFilters().get(1).get(0) == null && command.getFilters().get(0).get(0) != null) {
            List<Serial> filteredList = database.getSerials().stream()
                    .filter(s -> String.valueOf(s.getYear()).equals(command.getFilters().get(0).get(0)))
                    .sorted(serialNameComparator)
                    .collect(Collectors.toList());

            return filteredList;
        } else if (command.getFilters().get(0).get(0) == null && command.getFilters().get(1).get(0) != null) {
            List<Serial> filteredList = database.getSerials().stream()
                    .filter(s -> s.getGenres().contains(command.getFilters().get(1).get(0)))
                    .sorted(serialNameComparator)
                    .collect(Collectors.toList());

            return filteredList;
        } else if (command.getFilters().get(1).get(0) != null && command.getFilters().get(0).get(0) != null) {
            List<Serial> filteredList = database.getSerials().stream()
                    .filter(s -> String.valueOf(s.getYear()).equals(command.getFilters().get(0).get(0)))
                    .filter(s -> s.getGenres().contains(command.getFilters().get(1).get(0)))
                    .sorted(serialNameComparator)
                    .collect(Collectors.toList());

            return filteredList;
        }
        else {
            List<Serial> filteredList = database.getSerials().stream()
                    .sorted(serialNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
    }

    private static String filterByFavorite(Database database, Action command, List<Serial> filteredList) {
        Comparator<Serial> serialFavComparator
                = Comparator.comparingInt(Serial::getFavoriteCounter);

        filteredList = filteredList.stream()
                .filter(s -> s.getFavoriteCounter() != 0)
                .sorted(serialFavComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

    private static String filterByRating(Database database, Action command, List<Serial> filteredList) {
        Util.setShowsAverage(database);

        Comparator<Serial> serialAverageComparator
                = Comparator.comparingDouble(Serial::getAverage);

        filteredList = filteredList.stream()
                .filter(m -> m.getAverage() != 0.0)
                .sorted(serialAverageComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

    private static String filterByDuration(Database database, Action command, List<Serial> filteredList) {
        Util.setSerialDuration(database);

        Comparator<Serial> serialDurationComparator
                = Comparator.comparingInt(Serial::getDuration);

        filteredList = filteredList.stream()
                .sorted(serialDurationComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

    private static String filterByViews(Database database, Action command, List<Serial> filteredList) {
        Util.setSerialViews(database);

        Comparator<Serial> serialViewsComparator
                = Comparator.comparingInt(Serial::getViews);

        filteredList = filteredList.stream()
                .filter(m -> m.getViews() != 0)
                .sorted(serialViewsComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }
}
