package database;

import common.Constants;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationAction {
    public static String executeRec(Database database, Action command) {

        User user  = Search.searchUser(database, command.getUsername());
        String subcriptionType = user.getSubscriptionType();

        switch (command.getType()) {
            case Constants.STANDARD:
                return standardRec(database, user);
            case Constants.BEST_UNSEEN:
                return bestUnseenRec(database, user);
            case Constants.POPULAR:
                return popularRec(database, user, subcriptionType);
            case Constants.FAVORITE:
                return favoriteRec(database, user, subcriptionType);
            case Constants.SEARCH:
                return searchRec(database, user, subcriptionType, command.getGenre());
            default:
                return "";
        }
    }

    private static String standardRec(Database database, User user) {
        String unseenShow = null;

        for (Movie m : database.getMovies()) {
            if (!user.getHistory().containsKey(m.getTitle())) {
                unseenShow = m.getTitle();

                return "StandardRecommendation result: " + unseenShow;
            }
        }

        for (Serial s : database.getSerials()) {
            if (!user.getHistory().containsKey(s.getTitle())) {
                unseenShow = s.getTitle();

                return "StandardRecommendation result: " + unseenShow;
            }
        }

        if (unseenShow == null) {
            return "StandardRecommendation cannot be applied!";
        }

        return "";
    }

    private static String bestUnseenRec(Database database, User user) {
        Util.setMoviesAverage(database);
        Util.setShowsAverage(database);

        Comparator<Movie> movieAverageComparator
                = Comparator.comparingDouble(Movie::getAverage);

        Comparator<Movie> idMovieComparator
                = Comparator.comparingInt(Movie::getId);

        List<Movie> movieUnseenList = database.getMovies().stream()
                .filter(m -> !user.getHistory().containsKey(m.getTitle()))
                .sorted(idMovieComparator)
                .sorted(movieAverageComparator.reversed())
                .collect(Collectors.toList());

        if (movieUnseenList.isEmpty()) {

            Comparator<Serial> serialAverageComparator
                    = Comparator.comparingDouble(Serial::getAverage);

            Comparator<Serial> idSerialComparator
                    = Comparator.comparingInt(Serial::getId);

            List<Serial> serialUnseenList = database.getSerials().stream()
                    .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                    .sorted(idSerialComparator)
                    .sorted(serialAverageComparator.reversed())
                    .collect(Collectors.toList());

            if (serialUnseenList.isEmpty()) {
                return "BestRatedUnseenRecommendation cannot be applied!";
            }
            else {
                return "BestRatedUnseenRecommendation result: " + serialUnseenList.get(0).getTitle();
            }
        }
        else {
            return "BestRatedUnseenRecommendation result: " + movieUnseenList.get(0).getTitle();
        }
    }

    private static String favoriteRec(Database database, User user, String subscription) {
        if (subscription.equals(Constants.BASIC)) {
            return "FavoriteRecommendation cannot be applied!";
        }

        List<Show> showList = new ArrayList<Show>();

        showList.addAll(database.getMovies());
        showList.addAll(database.getSerials());

        Comparator<Show> showFavComparator
                = Comparator.comparingInt(Show::getFavoriteCounter);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        List<Show> showUnseenList = showList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .filter(s -> s.getFavoriteCounter() != 0)
                .sorted(idShowComparator)
                .sorted(showFavComparator.reversed())
                .collect(Collectors.toList());

        if (showUnseenList.isEmpty()) {
            return "FavoriteRecommendation cannot be applied!";
        }
        else {
            return "FavoriteRecommendation result: " + showUnseenList.get(0).getTitle();
        }
    }

    private static String searchRec(Database database, User user, String subscription, String genre) {
        if (subscription.equals(Constants.BASIC)) {
            return "SearchRecommendation cannot be applied!";
        }

        List<Show> showList = new ArrayList<Show>();

        showList.addAll(database.getMovies());
        showList.addAll(database.getSerials());

        Comparator<Show> titleShowComparator
                = Comparator.comparing(Show::getTitle);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        List<Show> showUnseenList = showList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .filter(s -> s.getGenres().contains(genre))
                .sorted(idShowComparator)
                .sorted(titleShowComparator)
                .collect(Collectors.toList());

        if (showUnseenList.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }
        else {
            String listMessage = null;
            List<String> listConvertedToNames = null;

            listConvertedToNames = showUnseenList.stream()
                    .map(Show::getTitle)
                    .collect(Collectors.toList());

            listMessage = String.join(", ", listConvertedToNames);

            return "SearchRecommendation result: [" + listMessage + "]";
        }
    }

    private static String popularRec(Database database, User user, String subscription) {
        if (subscription.equals(Constants.BASIC)) {
            return "PopularRecommendation cannot be applied!";
        }

        Util.setMovieViews(database);
        Util.setSerialViews(database); //!!!!!!!!

        List<Show> showList = new ArrayList<Show>();

        showList.addAll(database.getMovies());
        showList.addAll(database.getSerials());

        List<Show> showUnseenList = showList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .collect(Collectors.toList());

        int x = 5;

        if (showUnseenList.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        }

        ArrayList<String> genreListForUnseen = new ArrayList<String>();

        for (Show s : showUnseenList) {
            genreListForUnseen.addAll(s.getGenres());
        }

        int y = 4;

        List<String> listNoDuplicates = genreListForUnseen.stream()
                .distinct()
                .collect(Collectors.toList());

        int z = 5;

        Map<String, Integer> genrePopularity = new HashMap<String, Integer>();

        for (String g : listNoDuplicates) {
            genrePopularity.put(g, 0);
        }

        int w = 5;

        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet()) {
            for (Show s : showList) {
                for (String g : s.getGenres()) {
                    if (g.equals(entry.getKey())) {
                        entry.setValue(entry.getValue() + s.getViews());
                    }
                }
            }
        }

        int q = 5;

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : genrePopularity.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0)
            {
                maxEntry = entry;
            }
        }

        String mostPopularGenre = maxEntry.getKey();
        List<Show> showsPopular = new ArrayList<Show>();

        for (Show s : showUnseenList) {
            for (String g : s.getGenres()) {
                if (g.equals(mostPopularGenre)) {
                    showsPopular.add(s);
                }
            }
        }

        Comparator<Show> titleShowComparator
                = Comparator.comparing(Show::getTitle);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        showsPopular = showsPopular.stream()
                .sorted(idShowComparator)
                .collect(Collectors.toList());

        if (showsPopular.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        }
        else {
            return "PopularRecommendation result: " + showsPopular.get(0).getTitle();
        }
    }
}
