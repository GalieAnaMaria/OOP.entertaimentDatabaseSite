package database.commands;

import actor.ActorsAwards;
import common.Constants;
import database.Action;
import database.Actor;
import database.Database;
import utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Class designed to handle filtering command for actors
 *  according to the specific criteria
 */
public final class FilterActors {
    /**
     * For coding style
     */
    private FilterActors() {
    }

    /**
     * Method redirecting the query command depending on the command's type requested
     * to the afferent methods designed to handle the action
     * If the action is undefined the user will only get a blank message
     */
    public static String filterActors(final Database database, final Action command) {
        Comparator<Actor> actorNameComparator
                = Comparator.comparing(Actor::getName);

        switch (command.getCriteria()) {
            case Constants.FILTER_DESCRIPTION:
                return filterByDescription(database, command, actorNameComparator);
            case Constants.AWARDS:
                return filterByAwards(database, command, actorNameComparator);
            case Constants.AVERAGE:
                return filterByRating(database, command, actorNameComparator);
            default:
                return "";
        }
    }

    /**
     *  Method filtering the actors from the database according to the
     *  criteria that the words provided by the command are found in their filmography description
     *  and are later sorted by alphabetical order
     */
    private static String filterByDescription(final Database database, final Action command,
                                              final Comparator<Actor> actorNameComparator) {
        List<Actor> filteredList = new ArrayList<>();

        for (Actor a : database.getActors()) {
            List<String> descriptionWords = Arrays.asList(a.getCareerDescription()
                    .replaceAll("[^a-zA-Z ]", " ")
                    .toLowerCase()
                    .split("\\s+"));

            if (descriptionWords.containsAll(command.getFilters().get(2))) {
                filteredList.add(a);
            }
        }

        filteredList = filteredList.stream()
                .sorted(actorNameComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method filtering the actors from the database according to the
     *  number of total awards won if they match the award titles provided by the command
     */
    private static String filterByAwards(final Database database, final Action command,
                                         final Comparator<Actor> actorNameComparator) {
        List<Actor> filteredList = new ArrayList<>();
        List<ActorsAwards> commandAwards = new ArrayList<>();

        for (String awards : command.getFilters().get(Constants.THREE)) {
            commandAwards.add(Utils.stringToAwards(awards));
        }

        Util.setNumberAwards(database);

        for (Actor a : database.getActors()) {
            List<ActorsAwards> awardsKeys = new ArrayList<>(a.getAwards().keySet());

            if (awardsKeys.containsAll(commandAwards)) {
                filteredList.add(a);
            }
        }

        Comparator<Actor> actorAwardComparator
                = Comparator.comparing(Actor::getAwardsNumber);

        filteredList = filteredList.stream()
                .sorted(actorNameComparator)
                .sorted(actorAwardComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method filtering the actors from the database according to their
     *  filmography average and sorts them in alphabetical order after their names
     */
    private static String filterByRating(final Database database, final Action command,
                                         final Comparator<Actor> actorNameComparator) {
        List<Actor> filteredList;

        Util.setFilmographyAverage(database);

        Comparator<Actor> actorAverageComparator
                = Comparator.comparing(Actor::getAverageFilmography);


        filteredList = database.getActors().stream()
                .filter(a -> a.getAverageFilmography() != 0)
                .sorted(actorNameComparator)
                .sorted(actorAverageComparator)
                .collect(Collectors.toList());

        return finalOrdering(command, filteredList);
    }

    /**
     *  Method reversing the list and trimming it down if requested specifically
     */
    private static String finalOrdering(final Action command, List<Actor> filteredList) {

        if (command.getSortType().equals(Constants.DESC)) {
            Collections.reverse(filteredList);
        }

        if (command.getCriteria().equals(Constants.AVERAGE)) {
            filteredList = filteredList.stream()
                    .limit(command.getNumber())
                    .collect(Collectors.toList());
        }

        return convertListToString(filteredList);
    }

    /**
     *  Method converting the given list into a string message
     *  containing the actors' names
     *  If the list is empty it will show a message with no names
     */
    private static String convertListToString(final List<Actor> filteredList) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

        if (filteredList.isEmpty()) {
            return "Query result: []";
        }

        listConvertedToNames = filteredList.stream()
                .map(Actor::getName)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: ["
                + listMessage
                + "]";
    }
}
