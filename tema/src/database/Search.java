package database;

public class Search {
    public static User searchUser (Database database, String username) {
        User user = database.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        return user;
    }

    public static Movie searchMovie (Database database, String title) {
        Movie movie = null;
        for (Movie m: database.getMovies()) {
            if (m.getTitle().equals(title)) {
                movie = m;
            }
        }
        return movie;
    }

    public static Serial searchSerial (Database database, String title) {
        Serial serial = null;
        for (Serial s: database.getSerials()) {
            if (s.getTitle().equals(title)) {
                serial = s;
            }
        }
        return serial;
    }

    public static Season searchSeason (Serial serial, int seasonNumber) {
        Season season = null;
        for (Season s: serial.getSeasons()) {
            if (s.getCurrentSeason() == seasonNumber) {
                season = s;
            }
        }
        return season;
    }

}
