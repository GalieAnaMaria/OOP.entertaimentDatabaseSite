package database.commands;

import common.Constants;
import database.Action;
import database.Database;
import database.Show;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Class designed to handle filtering command for shows
 *  according to the specific criteria
 */
public final class FilterShows {
    /**
     * For coding style
     */
    private FilterShows() {
    }

    /**
     * Method redirecting the query command depending on the command's type requested
     * to the afferent methods designed to handle the action
     * If the action is undefined the user will only get a blank message
     */
    public static String filter(final Database database, final Action command,
                                final List<Show> showsList) {
        Comparator<Show> showNameComparator
                = Comparator.comparing(Show::getTitle);

        List<Show> filteredList = filterYearGenreName(database, command,
                showNameComparator, showsList);

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

    /**
     *  Method responsible for sorting the list of shows from the database
     *  according to filters for year and genre
     *  The received list will be sorted after alphabetical order of the shows' titles
     */
    private static List<Show> filterYearGenreName(final Database database, final Action command,
                                                  final Comparator<Show> showNameComparator,
                                                  final List<Show> showList) {
        List<Show> filteredList = null;
        String yearFilter = command.getFilters().get(0).get(0);
        String genreFilter = command.getFilters().get(1).get(0);

        if (genreFilter == null && yearFilter != null) {
            filteredList = showList.stream()
                    .filter(m -> String.valueOf(m.getYear()).equals(yearFilter))
                    .sorted(showNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        } else if (yearFilter == null && genreFilter != null) {
            filteredList = showList.stream()
                    .filter(m -> m.getGenres().contains(genreFilter))
                    .sorted(showNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        } else if (genreFilter != null && yearFilter != null) {
            filteredList = showList.stream()
                    .filter(m -> String.valueOf(m.getYear()).equals(yearFilter))
                    .filter(m -> m.getGenres().contains(genreFilter))
                    .sorted(showNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        } else {
            filteredList = showList.stream()
                    .sorted(showNameComparator)
                    .collect(Collectors.toList());
            return filteredList;
        }
    }

    /**
     *  Method filtering in order the shows according to the favorite counter
     *  representing the number of times they'd been added to the favorite list
     *  of all the users from the database
     *  (Not including shows that have never been added to such list)
     */
    private static String filterByFavorite(final Database database,
                                           final Action command, List<Show> filteredList) {
        Comparator<Show> showFavComparator
                = Comparator.comparingInt(Show::getFavoriteCounter);

        filteredList = filteredList.stream()
                .filter(s -> s.getFavoriteCounter() != 0)
                .sorted(showFavComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method filtering in order the shows according to the ratings
     *  given by the users
     *  (Not including shows that have never been rated/given a grade)
     */
    private static String filterByRating(final Database database, final Action command,
                                         List<Show> filteredList) {
        Util.setMoviesAverage(database);
        Util.setSerialAverage(database);

        Comparator<Show> movieAverageComparator
                = Comparator.comparingDouble(Show::getAverage);

        filteredList = filteredList.stream()
                .filter(m -> m.getAverage() != 0.0)
                .sorted(movieAverageComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method filtering in order the shows according to the time length
     */
    private static String filterByDuration(final Database database, final Action command,
                                           List<Show> filteredList) {
        Util.setSerialDuration(database);

        Comparator<Show> showLengthComparator
                = Comparator.comparingInt(Show::getDuration);

        filteredList = filteredList.stream()
                .sorted(showLengthComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method filtering in order the shows according to the number of views
     *  each have received from users
     *  (Counting the rewatches)
     *  (Not including shows that have never been watched by any user)
     */
    private static String filterByViews(final Database database, final Action command,
                                        List<Show> filteredList) {
        Util.setShowsViews(database);

        Comparator<Show> showViewsComparator
                = Comparator.comparingInt(Show::getViews);

        filteredList = filteredList.stream()
                .filter(m -> m.getViews() != 0)
                .sorted(showViewsComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method reversing the list if requested so specifically
     *  and trims the list however much it has been requested
     */
    private static String finalOrdering(final Action command, List<Show> filteredList) {
        if (command.getSortType().equals(Constants.DESC)) {
            Collections.reverse(filteredList);
        }

        filteredList = filteredList.stream()
                .limit(command.getNumber())
                .collect(Collectors.toList());

        return Util.convertListToString(filteredList);
    }
}
