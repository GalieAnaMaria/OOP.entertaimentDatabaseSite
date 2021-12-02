package database;

import actor.ActorsAwards;

import javax.xml.crypto.Data;
import java.util.Map;

public class Util {
    public static void increaseCounterShow(Database database, Action command) {
        for (Movie m : database.getMovies()) {
            if (m.getTitle().equals(command.getTitle())) {
                m.setFavoriteCounter(m.getFavoriteCounter() + 1);
            }
        }

        for (Serial s : database.getSerials()) {
            if (s.getTitle().equals(command.getTitle())) {
                s.setFavoriteCounter(s.getFavoriteCounter() + 1);
            }
        }
    }

    public static void setMoviesAverage(Database database) {
        for (Movie m : database.getMovies()) {
            double sum = m.getRatings().values().stream().mapToDouble(d -> d).sum();

            if (sum == 0.0) {
                m.setAverage(0.0);
            }
            else {
                m.setAverage(sum/(m.getRatings().size() - 1));

            }
        }
    }

    public static void setShowsAverage(Database database) {
        for (Serial show : database.getSerials()) {
            Double sumTotal = 0.0;

            for (Season s : show.getSeasons()) {
                Double sumSeason = 0.0;

                sumSeason = s.getRatings().values().stream().mapToDouble(d -> d).sum();

                if (sumSeason != 0.0) {
                    sumTotal = sumTotal + sumSeason/(s.getRatings().size() - 1);
                }
            }

            if (sumTotal != 0.0) {
                show.setAverage(sumTotal / show.getNumberOfSeasons());
            }
            int b;
        }
        int c;
    }

    public static void setSerialDuration(Database database) {
        for (Serial show : database.getSerials()) {
            int duration = 0;

            for (Season s : show.getSeasons()) {
                duration = duration + s.getDuration();
            }

            show.setDuration(duration);
        }
    }

    public static void setMovieViews(Database database) {
        for (Movie m : database.getMovies()) {
            for (User u : database.getUsers()) {
                if (u.getHistory().containsKey(m.getTitle())) {
                    m.setViews(m.getViews() + u.getHistory().get(m.getTitle()));
                }
            }
        }
    }

    public static void setSerialViews(Database database) {
        for (Serial s : database.getSerials()) {
            for (User u : database.getUsers()) {
                if (u.getHistory().containsKey(s.getTitle())) {
                    s.setViews(s.getViews() + u.getHistory().get(s.getTitle()));
                }
            }
        }
    }

    public static void setNumberAwards(Database database) {
        for (Actor a : database.getActors()) {
            Integer sum = a.getAwards().values().stream().mapToInt(i -> i).sum();
            a.setAwardsNumber(sum);
        }
    }

    public static void setFilmographyAverage(Database database) {
        setMoviesAverage(database);
        setShowsAverage(database);

        for (Actor a : database.getActors()) {
            Double sumShows = 0.0;
            int no_videos = 0;
            for (String show : a.getFilmography()) {
                for (Movie m : database.getMovies()) {
                    if (m.getTitle().equals(show)) {
                        double score = m.getAverage();
                        if(score != 0){
                            sumShows = sumShows + m.getAverage();
                            no_videos++;
                        }

                    }
                }

                for (Serial s : database.getSerials()) {
                    if (s.getTitle().equals(show)) {
                        double score = s.getAverage();
                        if(score != 0){
                            sumShows = sumShows + s.getAverage();
                            no_videos++;
                        }

                    }
                }
            }
            if(no_videos != 0) {
                a.setAverageFilmography(sumShows/no_videos);
            }
        }
    }
}
