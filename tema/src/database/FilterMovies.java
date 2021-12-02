package database;

import common.Constants;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterMovies {
    public static String filterMovieYearGenre(Database database, Action command) {
        Comparator<Movie> movieNameComparator
                = Comparator.comparing(Movie::getTitle);

        List<Movie> filteredList = filterYearGenreName(database, command,
                movieNameComparator);

        switch (command.getCriteria()) {
            case Constants.FAVORITE:
                return filterByFavorite(database, command, filteredList);
            case Constants.RATINGS:
                return filterByRating(database, command, filteredList);
            case Constants.LONGEST:
                return filterByDuration(database, command, filteredList);
            case Constants.MOST_VIEWED:
                return  filterByViews(database, command, filteredList);
            default:
                return "";
        }
    }

    private static String convert(Action command, List<Movie> filteredList) {
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
                .map(Movie::getTitle)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: [" + listMessage + "]";
    }

    private static List<Movie> filterYearGenreName(Database database, Action command,
                                                  Comparator<Movie> movieNameComparator) {
        if (command.getFilters().get(1).get(0) == null && command.getFilters().get(0).get(0) != null) {
            List<Movie> filteredList = database.getMovies().stream()
                    .filter(m -> String.valueOf(m.getYear()).equals(command.getFilters().get(0).get(0)))
                    .sorted(movieNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
        else if (command.getFilters().get(0).get(0) == null && command.getFilters().get(1).get(0) != null) {
            List<Movie> filteredList = database.getMovies().stream()
                    .filter(m -> m.getGenres().contains(command.getFilters().get(1).get(0)))
                    .sorted(movieNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
        else if (command.getFilters().get(1).get(0) != null && command.getFilters().get(0).get(0) != null) {
            List<Movie> filteredList = database.getMovies().stream()
                    .filter(m -> String.valueOf(m.getYear()).equals(command.getFilters().get(0).get(0)))
                    .filter(m -> m.getGenres().contains(command.getFilters().get(1).get(0)))
                    .sorted(movieNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
        else {
            List<Movie> filteredList = database.getMovies().stream()
                    .sorted(movieNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
    }

    private static String filterByFavorite(Database database, Action command, List<Movie> filteredList) {
        Comparator<Movie> movieFavComparator
                = Comparator.comparingInt(Movie::getFavoriteCounter);

        filteredList = filteredList.stream()
                .filter(m -> m.getFavoriteCounter() != 0)
                .sorted(movieFavComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

   private static String filterByRating(Database database, Action command,
                                        List<Movie> filteredList) {
        Util.setMoviesAverage(database);

        Comparator<Movie> movieAverageComparator
                = Comparator.comparingDouble(Movie::getAverage);

        filteredList = filteredList.stream()
                .filter(m -> m.getAverage() != 0.0)
                .sorted(movieAverageComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

    private static String filterByDuration(Database database, Action command, List<Movie> filteredList) {
        Comparator<Movie> movieLengthComparator
                = Comparator.comparingInt(Movie::getDuration);

        filteredList = filteredList.stream()
                .sorted(movieLengthComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }

    private static String filterByViews(Database database, Action command, List<Movie> filteredList) {
        Util.setMovieViews(database);

        Comparator<Movie> movieViewsComparator
                = Comparator.comparingInt(Movie::getViews);

        filteredList = filteredList.stream()
                .filter(m -> m.getViews() != 0)
                .sorted(movieViewsComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }
}
