package database;

import java.util.List;

/**
 *  The class created to hold all the information received
 *  from input through the DatabaseService class
 *  The lists from this class will provide the needed
 *  info to execute the commands
 */
public final class Database {
    /**
     * Data about actors from input
     */
    private List<Actor> actors;
    /**
     * Data about users from input
     */
    private List<User> users;
    /**
     * Data about commands from input
     */
    private List<Action> commands;
    /**
     * Data about movies from input
     */
    private List<Movie> movies;
    /**
     * Data about serials from input
     */
    private List<Serial> serials;

    public Database() {
    }

    public List<database.Actor> getActors() {
        return actors;
    }

    public void setActors(final List<Actor> actors) {
        this.actors = actors;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }

    public List<Action> getCommands() {
        return commands;
    }

    public void setCommands(final List<Action> commands) {
        this.commands = commands;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final List<Movie> movies) {
        this.movies = movies;
    }

    public List<Serial> getSerials() {
        return serials;
    }

    public void setSerials(final List<Serial> serials) {
        this.serials = serials;
    }
}
