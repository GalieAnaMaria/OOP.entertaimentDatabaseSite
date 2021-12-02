package database.commands;

import database.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Utility class designed to handle auxiliary tasks for
 *  the rest of the methods
 */
public final class Util {
    /**
     * For coding style
     */
    private Util() {
    }

    /**
     * Method returning the list of all the shows (movies and serials)
     * into one singular list
     */
    public static List<Show> setShowsList(final Database database) {
        List<Show> showsList = new ArrayList<>();

        showsList.addAll(database.getMovies());
        showsList.addAll(database.getSerials());

        return showsList;
    }

    /**
     *  Method that increases the (favorite counter) for a show specifically after a command
     *  of such nature has been requested by a user
     */
    public static void increaseCounterShow(final Database database, final Action command) {
        List<Show> showsList = setShowsList(database);

        for (Show s : showsList) {
            if (s.getTitle().equals(command.getTitle())) {
                s.setFavoriteCounter(s.getFavoriteCounter() + 1);
            }
        }
    }

    /**
     *  Method setting the (average) for all the movies from the database
     *  according to all the information existing at the moment of request by
     *  an action/user
     */
    public static void setMoviesAverage(final Database database) {
        for (Movie m : database.getMovies()) {
            double sum = m.getRatings().values().stream().mapToDouble(d -> d).sum();

            if (sum == 0.0) {
                m.setAverage(0.0);
            } else {
                m.setAverage(sum / (m.getRatings().size() - 1));

            }
        }
    }

    /**
     *  Method setting the (average) for all the serials from the database
     *  according to all the information existing at the moment of request by
     *  an action/user
     *  The criteria for setting the serial average is done taking into the
     *  consideration the ratings of all the seasons
     */
    public static void setSerialAverage(final Database database) {
        for (Serial serial : database.getSerials()) {
            Double sumTotal = 0.0;

            for (Season season : serial.getSeasons()) {
                Double sumSeason = 0.0;

                sumSeason = season.getRatings().values().stream().mapToDouble(d -> d).sum();

                if (sumSeason != 0.0) {
                    sumTotal = sumTotal + sumSeason / (season.getRatings().size() - 1);
                }
            }

            if (sumTotal != 0.0) {
                serial.setAverage(sumTotal / serial.getNumberOfSeasons());
            }
        }
    }

    /**
     *  Method setting the duration/time length of a serial for all the shows from the database
     *  according to all the information about the serial/seasons existing at the
     *  moment of request by an action/user
     */
    public static void setSerialDuration(final Database database) {
        for (Serial show : database.getSerials()) {
            int duration = 0;

            for (Season s : show.getSeasons()) {
                duration = duration + s.getDuration();
            }

            show.setDuration(duration);
        }
    }

    /**
     *  Method setting the (view counter) for all the shows from the database
     *  according to all the information existing at the moment of request by
     *  an action/user
     */
    public static void setShowsViews(final Database database) {
        List<Show> showsList = setShowsList(database);

        for (Show s : showsList) {
            for (User u : database.getUsers()) {
                if (u.getHistory().containsKey(s.getTitle())) {
                    s.setViews(s.getViews() + u.getHistory().get(s.getTitle()));
                }
            }
        }
    }

    /**
     *  Method setting the (number of awards) for all the actors from the database
     */
    public static void setNumberAwards(final Database database) {
        for (Actor a : database.getActors()) {
            Integer sum = a.getAwards().values().stream().mapToInt(i -> i).sum();
            a.setAwardsNumber(sum);
        }
    }

    /**
     *  Method setting the (average for the whole filmography of an actor) from the database
     *  according to all the information existing at the moment of request by
     *  an action/user
     */
    public static void setFilmographyAverage(final Database database) {
        setMoviesAverage(database);
        setSerialAverage(database);

        for (Actor a : database.getActors()) {
            Double sumShows = 0.0;
            int nrVideos = 0;

            for (String show : a.getFilmography()) {
                for (Movie m : database.getMovies()) {
                    if (m.getTitle().equals(show)) {
                        double score = m.getAverage();

                        if (score != 0) {
                            sumShows = sumShows + m.getAverage();
                            nrVideos++;
                        }

                    }
                }

                for (Serial s : database.getSerials()) {
                    if (s.getTitle().equals(show)) {
                        double score = s.getAverage();

                        if (score != 0) {
                            sumShows = sumShows + s.getAverage();
                            nrVideos++;
                        }
                    }
                }
            }
            if (nrVideos != 0) {
                a.setAverageFilmography(sumShows / nrVideos);
            }
        }
    }

    /**
     *  Method passing the list of all the genres found according to the
     *  ones the shows from a given list are categorized in
     *  (The received list will contain no duplicates)
     */
    public static List<String> getShowsGenres(final List<Show> showsList) {
        ArrayList<String> genreListForUnseenShows = new ArrayList<>();

        for (Show s : showsList) {
            genreListForUnseenShows.addAll(s.getGenres());
        }

        List<String> noDuplicatesList = genreListForUnseenShows.stream()
                .distinct()
                .collect(Collectors.toList());

        return noDuplicatesList;
    }

    /**
     *  Method that calculates the most popular genre from a given list of genres
     *  looking through the list of shows given and adding up all the views of all
     *  the shows from each specific genre
     *  The genre which has the movies with the most views to their name will be
     *  received post call
     */
    public static String getPopularGenre(final List<Show> showsList, final List<String> genreList) {
        Map<String, Integer> genrePopularity = new HashMap<>();

        for (String g : genreList) {
            genrePopularity.put(g, 0);
        }

        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet()) {
            for (Show s : showsList) {
                for (String g : s.getGenres()) {
                    if (g.equals(entry.getKey())) {
                        entry.setValue(entry.getValue() + s.getViews());
                    }
                }
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0) {
                maxEntry = entry;
            }
        }

        String mostPopularGenre = maxEntry.getKey();
        return mostPopularGenre;
    }

    /**
     *  Method converting a list of shows into a string message composed of the show titles
     *  If the list is empty the message will display no titles
     */
    public static String convertListToString(final List<Show> showList) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

        if (showList.isEmpty()) {
            return "Query result: []";
        }

        listConvertedToNames = showList.stream()
                .map(Show::getTitle)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: ["
                + listMessage
                + "]";
    }
}
