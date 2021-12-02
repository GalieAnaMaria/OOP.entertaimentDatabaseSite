package database;

import actor.ActorsAwards;
import common.Constants;
import utils.Utils;
import java.util.*;
import java.util.stream.Collectors;

public class FilterActors {
    public static String filterActors(Database database, Action command) {
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

    private static String convert(Action command, List<Actor> filteredList) {
        String listMessage = null;
        List<String> listConvertedToNames = null;

        if (command.getSortType().equals(Constants.DESC)) {
            Collections.reverse(filteredList);
        }

        if (command.getCriteria().equals(Constants.AVERAGE)) {
            filteredList = filteredList.stream()
                .limit(command.getNumber())
                .collect(Collectors.toList());
        }

        if (filteredList.isEmpty()) {
            return "Query result: []";
        }

        listConvertedToNames = filteredList.stream()
                .map(Actor::getName)
                .collect(Collectors.toList());

        listMessage = String.join(", ", listConvertedToNames);

        return "Query result: [" + listMessage + "]";
    }

    private static String filterByDescription(Database database, Action command,
                                             Comparator<Actor> actorNameComparator) {
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

        return convert(command, filteredList);
    }

    private static String filterByAwards(Database database, Action command,
                                        Comparator<Actor> actorNameComparator) {
        List<Actor> filteredList = new ArrayList<>();
        List<ActorsAwards> commandAwards = new ArrayList<>();

        for (String awards : command.getFilters().get(3)) {
            commandAwards.add(Utils.stringToAwards(awards));
        }

        Util.setNumberAwards(database);

        for (Actor a : database.getActors()) {
            List<ActorsAwards> awardsKeys = new ArrayList<ActorsAwards>(a.getAwards().keySet());

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

        return convert(command, filteredList);
    }

    private static String filterByRating(Database database, Action command,
                                        Comparator<Actor> actorNameComparator) {
        List<Actor> filteredList = new ArrayList<>();

        Util.setFilmographyAverage(database);

        Comparator<Actor> actorAverageComparator
                = Comparator.comparing(Actor::getAverageFilmography);


        filteredList = database.getActors().stream()
                .filter(a -> a.getAverageFilmography() != 0)
                .sorted(actorNameComparator)
                .sorted(actorAverageComparator)
                .collect(Collectors.toList());

        return convert(command, filteredList);
    }
}
