package database;

import actor.*;
import common.*;
import entertainment.*;
import fileio.*;
import utils.*;

import java.util.List;

public class Database {
    private List<Actor> actors;
    private List<User> users;
    private List<Action> commands;
    private List<Movie> movies;
    private List<Serial> serials;

    public Database() {}

    public List<database.Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Action> getCommands() {
        return commands;
    }

    public void setCommands(List<Action> commands) {
        this.commands = commands;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Serial> getSerials() {
        return serials;
    }

    public void setSerials(List<Serial> serials) {
        this.serials = serials;
    }
}