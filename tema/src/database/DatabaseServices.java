package database;

import fileio.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import common.Constants;
import database.ActionManagement;

import javax.xml.crypto.Data;

public class DatabaseServices {

    public void insert(Input input, Database database) {
        AtomicInteger idCount = new AtomicInteger(0);

        this.insertActors(input.getActors(), database);
        this.insertUsers(input.getUsers(), database);
        this.insertMovies(input.getMovies(), database, idCount);
        this.insertSerials(input.getSerials(), database, idCount);
        this.insertCommands(input.getCommands(), database);
        this.setFavoriteCounter(database);
    }

    private void insertActors(List<ActorInputData> actorsData, Database database) {
        if (actorsData != null) {
            List<Actor> actorsList = new ArrayList<>();

            actorsData.stream().forEach((a) -> {
                Actor newActor = new Actor();

                newActor.setName(a.getName());
                newActor.setCareerDescription(a.getCareerDescription());
                newActor.setFilmography(a.getFilmography());
                newActor.setAwards(a.getAwards());
                newActor.setAverageFilmography(0.0);
                newActor.setAwardsNumber(0);

                actorsList.add(newActor);
            });

            database.setActors(actorsList);
        }
    }

    private void insertUsers(List<UserInputData> usersData, Database database) {
        if (usersData != null) {
            List<User> usersList = new ArrayList<>();

            usersData.stream().forEach((u) -> {
                User newUser = new User();

                newUser.setUsername(u.getUsername());
                newUser.setSubscriptionType(u.getSubscriptionType());
                newUser.setHistory(u.getHistory());
                newUser.setFavorite(u.getFavoriteMovies());
                newUser.setNrValidRatings(0);

                usersList.add(newUser);
            });

            database.setUsers(usersList);
        }
    }

    private void insertMovies(List<MovieInputData> moviesData, Database database, AtomicInteger idCount) {
        if (moviesData != null) {
            List<Movie> moviesList = new ArrayList<>();

            moviesData.stream().forEach((m) -> {
                Movie newMovie = new Movie();
                Map<String, Double> defaultMap = new HashMap<>();
                defaultMap.put("null", 0.0);

                newMovie.setTitle(m.getTitle());
                newMovie.setYear(m.getYear());
                newMovie.setCast(m.getCast());
                newMovie.setGenres(m.getGenres());
                newMovie.setDuration(m.getDuration());
                newMovie.setRatings(defaultMap);
                newMovie.setAverage(0.0);
                newMovie.setFavoriteCounter(0);
                newMovie.setId(idCount.addAndGet(1));

                moviesList.add(newMovie);
            });

            database.setMovies(moviesList);
        }
    }

    private void insertSerials(List<SerialInputData> serialsData, Database database, AtomicInteger idCount) {
        if (serialsData != null) {
            List<Serial> serialsList = new ArrayList<>();

            for (SerialInputData s: serialsData) {
                Serial newSerial = new Serial();
                ArrayList<Season> seasonsList = new ArrayList<>();
                AtomicInteger count = new AtomicInteger(0);

                newSerial.setTitle(s.getTitle());
                newSerial.setYear(s.getYear());
                newSerial.setCast(s.getCast());
                newSerial.setGenres(s.getGenres());
                newSerial.setAverage(0.0);
                newSerial.setFavoriteCounter(0);
                newSerial.setId(idCount.addAndGet(1));
                newSerial.setDuration(0);
                newSerial.setNumberOfSeasons(s.getNumberSeason());

                s.getSeasons().stream().forEach((season) -> {
                    Season newSeason = new Season();
                    Map<String, Double> defaultMap = new HashMap<>();
                    defaultMap.put("null", 0.0);

                    newSeason.setCurrentSeason(count.addAndGet(1));
                    newSeason.setDuration(season.getDuration());
                    newSeason.setRatings(defaultMap);
                    newSeason.setAverage(0.0);

                    seasonsList.add(newSeason);
                });

                newSerial.setSeasons(seasonsList);
                serialsList.add(newSerial);
            }

            database.setSerials(serialsList);
        }
    }

    private void insertCommands(List<ActionInputData> commandsData, Database database) {
        if (commandsData != null) {
            List<Action> actionList = new ArrayList<>();

            for (ActionInputData c: commandsData) {
                Action newCommand = new Action();

                newCommand.setActionId(c.getActionId());
                newCommand.setActionType(c.getActionType());

                if (newCommand.getActionType().equals(Constants.RECOMMENDATION)) {
                    newCommand.setType(c.getType());
                    newCommand.setUsername(c.getUsername());
                    newCommand.setGenre(c.getGenre());

                }
                else if (newCommand.getActionType().equals(Constants.COMMAND)) {
                    newCommand.setType(c.getType());
                    newCommand.setUsername(c.getUsername());
                    newCommand.setGrade(c.getGrade());
                    newCommand.setTitle(c.getTitle());
                    newCommand.setSeasonNumber(c.getSeasonNumber());
                }
                else if (newCommand.getActionType().equals(Constants.QUERY)) {
                    List<List<String>> commandFilters = new ArrayList<>();

                    newCommand.setObjectType(c.getObjectType());
                    newCommand.setSortType(c.getSortType());
                    newCommand.setCriteria(c.getCriteria());
                    newCommand.setNumber(c.getNumber());
                    commandFilters.add(c.getFilters().get(0));
                    commandFilters.add(c.getFilters().get(1));
                    commandFilters.add(c.getFilters().get(2));
                    commandFilters.add(c.getFilters().get(3));
                    newCommand.setFilters(commandFilters);
                }

                actionList.add(newCommand);
            }

            database.setCommands(actionList);
        }
    }

    private void setFavoriteCounter(Database database) {
        for (User u: database.getUsers()) {
            for (Movie m: database.getMovies()) {
                if(u.getFavorite().contains(m.getTitle())) {
                    m.setFavoriteCounter(m.getFavoriteCounter() + 1);
                }
            }
            for (Serial s: database.getSerials()) {
                if(u.getFavorite().contains(s.getTitle())) {
                    s.setFavoriteCounter(s.getFavoriteCounter() + 1);
                }
            }
        }
    }
}