package database.commands;

import common.Constants;
import database.Action;
import database.Database;
import database.Show;
import database.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class designed to handle recommendation requests by the user
 * This action has Standard features and Premium features
 * (which are locked for Basic subscriptions)
 */
public final class RecommendationAction {
    /**
     * For coding style
     */
    private RecommendationAction() {
    }

    /**
     * Method redirecting the command depending on the recommendation type requested
     * to the afferent methods designed to handle the action
     * If the action is undefined the user will only get a blank message
     */
    public static String executeRec(final Database database, final Action command) {

        User user = Search.searchUser(database, command.getUsername());
        String subscriptionType = user.getSubscriptionType();

        switch (command.getType()) {
            case Constants.STANDARD:
                return standardRec(database, user);
            case Constants.BEST_UNSEEN:
                return bestUnseenRec(database, user);
            case Constants.POPULAR:
                return popularRec(database, user, subscriptionType);
            case Constants.FAVORITE:
                return favoriteRec(database, user, subscriptionType);
            case Constants.SEARCH:
                return searchRec(database, user, subscriptionType, command.getGenre());
            default:
                return "";
        }
    }

    /**
     *  Method applies a standard recommendation research through unseen shows
     *  that could be recommended to the user
     *  The recommendation will be displayed according to the order in which the shows has
     *  been inserted in the database
     *  If all the shows from the database have been watched by the user this action
     *  will throw an error and not be applied
     */
    private static String standardRec(final Database database, final User user) {
        String unseenShow = null;

        List<Show> showsList = Util.setShowsList(database);

        for (Show s : showsList) {
            if (!user.getHistory().containsKey(s.getTitle())) {
                unseenShow = s.getTitle();

                return "StandardRecommendation result: "
                        + unseenShow;
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    /**
     *  Method applies a standard recommendation research through unseen shows
     *  that could be recommended to the user
     *  The recommendation will be displayed according to the order in which the show has
     *  been inserted in the database but also the average grade rating
     *  If all the shows from the database have been watched by the user this action
     *  will throw an error and not be applied
     */
    private static String bestUnseenRec(final Database database, final User user) {
        Util.setMoviesAverage(database);
        Util.setSerialAverage(database);

        Comparator<Show> showAverageComparator
                = Comparator.comparingDouble(Show::getAverage);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        List<Show> showsList = Util.setShowsList(database);

        List<Show> showsUnseenList = showsList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .sorted(idShowComparator)
                .sorted(showAverageComparator.reversed())
                .collect(Collectors.toList());

        if (showsUnseenList.isEmpty()) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        } else {
            return "BestRatedUnseenRecommendation result: "
                    + showsUnseenList.get(Constants.ZERO).getTitle();
        }
    }

    /**
     *  Method applies a Premium recommendation research through unseen shows
     *  that could be recommended to the user
     *  Only users with premium subscription can use this feature
     *  The recommendation will be displayed according to the order in which the show has
     *  been inserted in the database but also the favorite counter of the show
     *  If all the shows from the database have been watched by the user this action
     *  will throw an error and not be applied
     */
    private static String favoriteRec(final Database database, final User user,
                                      final String subscription) {
        if (subscription.equals(Constants.BASIC)) {
            return "FavoriteRecommendation cannot be applied!";
        }

        List<Show> showsList = Util.setShowsList(database);

        Comparator<Show> showFavComparator
                = Comparator.comparingInt(Show::getFavoriteCounter);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        List<Show> showUnseenList = showsList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .filter(s -> s.getFavoriteCounter() != 0)
                .sorted(idShowComparator)
                .sorted(showFavComparator.reversed())
                .collect(Collectors.toList());

        if (showUnseenList.isEmpty()) {
            return "FavoriteRecommendation cannot be applied!";
        } else {
            return "FavoriteRecommendation result: "
                    + showUnseenList.get(Constants.ZERO).getTitle();
        }
    }

    /**
     *  Method applies a Premium recommendation research through unseen shows
     *  from a specific requested genre that could be recommended to the user
     *  Only users with premium subscription can use this feature
     *
     *  The recommendation will be displayed in a list form of all the unseen shows
     *  according to the order in which the shows have been inserted in the database
     *  but also alphabetical for the titles
     *  If all the shows from the database have been watched by the user this action
     *  will throw an error and not be applied
     */
    private static String searchRec(final Database database, final User user,
                                    final String subscription, final String genre) {
        if (subscription.equals(Constants.BASIC)) {
            return "SearchRecommendation cannot be applied!";
        }

        List<Show> showsList = Util.setShowsList(database);

        Comparator<Show> titleShowComparator
                = Comparator.comparing(Show::getTitle);

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        List<Show> showUnseenList = showsList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .filter(s -> s.getGenres().contains(genre))
                .sorted(idShowComparator)
                .sorted(titleShowComparator)
                .collect(Collectors.toList());

        if (showUnseenList.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        } else {
            String listMessage = null;
            List<String> listConvertedToNames = null;

            listConvertedToNames = showUnseenList.stream()
                    .map(Show::getTitle)
                    .collect(Collectors.toList());

            listMessage = String.join(", ", listConvertedToNames);

            return "SearchRecommendation result: ["
                    + listMessage
                    + "]";
        }
    }

    /**
     *  Method applies a Premium recommendation research through unseen shows
     *  from the most (popular genre) that could be recommended to the user
     *  Only users with premium subscription can use this feature
     *
     *  The recommendation will be displayed according to the order in which the show has
     *  been inserted in the database
     *  If all the shows from the database have been watched by the user this action
     *  will throw an error and not be applied
     */
    private static String popularRec(final Database database, final User user,
                                     final String subscription) {
        if (subscription.equals(Constants.BASIC)) {
            return "PopularRecommendation cannot be applied!";
        }

        Util.setShowsViews(database);
        List<Show> showsList = new ArrayList<>();
        showsList.addAll(database.getMovies());
        showsList.addAll(database.getSerials());

        List<Show> showUnseenList = showsList.stream()
                .filter(s -> !user.getHistory().containsKey(s.getTitle()))
                .collect(Collectors.toList());

        if (showUnseenList.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        }

        List<String> genreList = Util.getShowsGenres(showUnseenList);
        String mostPopularGenre = Util.getPopularGenre(showsList, genreList);
        List<Show> showsPopular = new ArrayList<>();

        for (Show s : showUnseenList) {
            for (String g : s.getGenres()) {
                if (g.equals(mostPopularGenre)) {
                    showsPopular.add(s);
                }
            }
        }

        Comparator<Show> idShowComparator
                = Comparator.comparingInt(Show::getId);

        showsPopular = showsPopular.stream()
                .sorted(idShowComparator)
                .collect(Collectors.toList());

        if (showsPopular.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        } else {
            return "PopularRecommendation result: "
                    + showsPopular.get(Constants.ZERO).getTitle();
        }
    }
}
