package com.example.tournamentmanager.calculation;

import com.example.tournamentmanager.model.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class PZSzachCalculation {
    public static final int PZSZACH_FLOOR = 1000;

    public static int getRatingPerformance(Player player) {
        return getRatingPerformance(getAverageRating(player), getRatingDelta(player));
    }

    public static int getRatingPerformance(int avg, int delta) {
        return avg + delta;
    }

    public static int getRatingDelta(Player player) {
        long n = player.getRounds().parallelStream().filter(game -> !game.isForfeit()).count();
        long wins = player.getRounds().parallelStream().filter(game -> !game.isForfeit() && player.getRoundResult(game) == Result.WIN).count();
        long loses = player.getRounds().parallelStream().filter(game -> !game.isForfeit() && player.getRoundResult(game) == Result.LOSE).count();

        return getRatingDelta((int) n, (int) wins, (int) loses);
    }

    public static int getRatingDelta(int n, int wins, int loses) {
        return (400 / (n + 1)) * (wins - loses);
    }

    public static int getAverageRating(Player player) {
        int sum = 0;
        int count = 0;
        for (Game round : player.getRounds()) {
            if (!round.isForfeit()) {
                if (round.getWhite() == player) {
                    sum += round.getBlack().getPZSzachRating();
                } else {
                    sum += round.getWhite().getPZSzachRating();
                }
                count++;
            }
        }

        return getAverageRating(player, sum, count);
    }

    public static int getAverageRating(Player player, int opponentsRatingSum, int n) {
        return (player.getPZSzachRating() + opponentsRatingSum) / (n + 1);
    }


    public static Integer getTitleValue(Title title, Player.Sex sex) {
        if (title == null) {
            return PZSZACH_FLOOR;
        }
        int baseValue;
        switch (title) {
            case GM -> baseValue = 2600;
            case IM -> baseValue = 2450;
            case WGM -> baseValue = 2400;
            case FM -> baseValue = 2300;
            case WIM -> baseValue = 2250;
            case CM -> baseValue = 2200;
            case WFM -> baseValue = 2100;
            case WCM -> baseValue = 2000;
            default -> {
                if (sex == Player.Sex.FEMALE) {
                    baseValue = switch (title) {
                        case M -> 2200;
                        case K_PLUS_PLUS, K_PLUS -> 2100;
                        case K -> 2000;
                        case I_PLUS_PLUS, I_PLUS -> 1900;
                        case I -> 1800;
                        case II_PLUS -> 1700;
                        case II -> 1600;
                        case III -> 1400;
                        case IV -> 1250;
                        case V -> 1100;
                        default -> PZSZACH_FLOOR;
                    };
                } else
                    baseValue = switch (title) {
                        case M -> 2400;
                        case K_PLUS_PLUS, K_PLUS -> 2300;
                        case K -> 2200;
                        case I_PLUS_PLUS, I_PLUS -> 2100;
                        case I -> 2000;
                        case II_PLUS -> 1900;
                        case II -> 1800;
                        case III -> 1600;
                        case IV -> 1400;
                        case V -> 1200;
                        default -> PZSZACH_FLOOR;
                    };
            }
        }
        return baseValue;
    }

    public static Title getNorm(int ratingPerformance, int gamesNo, Player.Sex sex) {
        if (gamesNo >= 9) {
            if (getNorm9Rounds(ratingPerformance, sex) != null) {
                return getNorm9Rounds(ratingPerformance, sex);
            }
            if (getNorm7Rounds(ratingPerformance, sex) != null) {
                return getNorm7Rounds(ratingPerformance, sex);
            }
            return getNorm5Rounds(ratingPerformance, sex);
        } else if (gamesNo >= 7) {
            if (getNorm7Rounds(ratingPerformance, sex) != null) {
                return getNorm7Rounds(ratingPerformance, sex);
            }
            return getNorm5Rounds(ratingPerformance, sex);
        } else if (gamesNo >= 5) {
            return getNorm5Rounds(ratingPerformance, sex);
        }
        return null;
    }

    private static Title getNorm5Rounds(int ratingPerformance, Player.Sex sex) {
        if (sex == Player.Sex.FEMALE) {
            return (ratingPerformance >= 1150) ? Title.IV : (ratingPerformance >= 1000) ? Title.V : null;
        } else {
            return (ratingPerformance >= 1300) ? Title.IV : (ratingPerformance >= 1050) ? Title.V : null;
        }
    }

    private static Title getNorm7Rounds(int ratingPerformance, Player.Sex sex) {
        return (sex == Player.Sex.FEMALE) ? getNorm7RoundsFemale(ratingPerformance) : getNorm7RoundsMale(ratingPerformance);
    }

    private static Title getNorm7RoundsFemale(int ratingPerformance) {
        return (ratingPerformance >= 1800) ? Title.I : (ratingPerformance >= 1600) ? Title.II : (ratingPerformance >= 1350) ? Title.III : null;
    }

    private static Title getNorm7RoundsMale(int ratingPerformance) {
        return (ratingPerformance >= 1800) ? Title.II : (ratingPerformance >= 1550) ? Title.III : null;
    }

    private static Title getNorm9Rounds(int ratingPerformance, Player.Sex sex) {
        return (sex == Player.Sex.FEMALE) ? getNorm9RoundsFemale(ratingPerformance) : getNorm9RoundsMale(ratingPerformance);
    }

    private static Title getNorm9RoundsFemale(int ratingPerformance) {
        return (ratingPerformance >= 2200) ? Title.M : (ratingPerformance >= 2000) ? Title.K : null;
    }

    private static Title getNorm9RoundsMale(int ratingPerformance) {
        return (ratingPerformance >= 2400) ? Title.M : (ratingPerformance >= 2200) ? Title.K : null;
    }

    private static int getAverageOpponentRating(Player player) {
        int sum = player.getRounds().parallelStream()
                .filter(game -> !game.isForfeit())
                .mapToInt(game -> player.getOpponent(game).getPZSzachRating())
                .sum();

        long count = player.getRounds().parallelStream().filter(game -> !game.isForfeit()).count();

        return (count == 0) ? 0 : sum / (int) count;
    }

    private static Title calculateTitle(Player player, int rounds) {
        int loses = player.getPZSzachRounds().size() >= rounds ? player.getPZSzachRounds().size() : player.getLosesNumber() + rounds - player.getPZSzachRounds().size();
        int wins = player.getWinsNumber();
        float points = rounds / 2.0f + (wins - loses) * 0.5f;
        if (points < rounds / 3.0f) {
            return null;
        }
        int delta = getRatingDelta(rounds, wins, loses);
        int avg = getAverageOpponentRating(player);
        int ratingPerformance = getRatingPerformance(getAverageRating(player, avg * rounds, rounds), delta);
        return getNorm(ratingPerformance, rounds, player.getSex());
    }

    public static Title getLessRoundTitle(Player player, Title ceil) {
        switch (ceil) {
            case M, K, I -> {
                return Stream.of(9, 7, 5)
                        .parallel()
                        .filter(rounds -> player.getPlayedGamedNumber() < rounds)
                        .map(rounds -> calculateTitle(player, rounds))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
            }
            case II, III -> {
                return Stream.of(7, 5)
                        .parallel()
                        .filter(rounds -> player.getPlayedGamedNumber() < rounds)
                        .map(rounds -> calculateTitle(player, rounds))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
            }
            default -> {
                return Stream.of(5)
                        .filter(rounds -> player.getPlayedGamedNumber() < rounds)
                        .map(rounds -> calculateTitle(player, rounds))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);

            }
        }
    }


    private static Title getTitleRangeRound(Player player, Title ceil, int roundsNorm) {
        List<Title> results = Collections.synchronizedList(new ArrayList<>());

        List<Game> games = player.getRounds();
        IntStream.rangeClosed(roundsNorm, games.size())
                .parallel()
                .forEach(i -> {
                    List<Game> currentGames = games.subList(0, i);
                    int playedGames = (int) currentGames.stream().filter(game -> !game.isForfeit()).count();

                    if (playedGames >= roundsNorm) {
                        int sum = currentGames.parallelStream()
                                .filter(game -> !game.isForfeit())
                                .mapToInt(game -> player.getOpponent(game).getPZSzachRating())
                                .sum();

                        long count = currentGames.parallelStream().filter(game -> !game.isForfeit()).count();
                        long wins = currentGames.parallelStream().filter(game -> !game.isForfeit() && player.getRoundResult(game) == Result.WIN).count();
                        long loses = currentGames.parallelStream().filter(game -> !game.isForfeit() && player.getRoundResult(game) == Result.LOSE).count();
                        long draws = currentGames.parallelStream().filter(game -> !game.isForfeit() && player.getRoundResult(game) == Result.DRAW).count();

                        int avg = getAverageRating(player, sum, (int) count);
                        int delta = getRatingDelta((int) count, (int) wins, (int) loses);

                        if (wins + (draws) * 0.5 >= count / 3.0f) {
                            Title title = getNorm(avg + delta, (int) count, player.getSex());
                            results.add(title);
                        }
                    }
                });

        Title result = results.parallelStream()
                .filter(title -> getTitleValue(title, player.getSex()) <= getTitleValue(ceil, player.getSex()))
                .max(Comparator.comparingInt(title -> getTitleValue(title, player.getSex())))
                .orElse(null);

        return lowerTitle(result, ceil);
    }

    private static Title getTitleRangeRound(Player player, Title ceil) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<Title> task1 = CompletableFuture.supplyAsync(() ->
                getTitleRangeRound(player, ceil, 9), executorService);

        CompletableFuture<Title> task2 = CompletableFuture.supplyAsync(() ->
                getTitleRangeRound(player, ceil, 7), executorService);

        CompletableFuture<Title> task3 = CompletableFuture.supplyAsync(() ->
                getTitleRangeRound(player, ceil, 5), executorService);

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

        try {
            allTasks.get();
        } catch (InterruptedException | ExecutionException ignored) {
        } finally {
            executorService.shutdown();
        }

        List<Title> titles = Stream.of(task1, task2, task3)
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(title -> getTitleValue(title, player.getSex())))
                .toList();

        return titles.isEmpty() ? null : titles.getFirst();
    }

    private static Title smallNorm(Player player) {
        if (player.getPlayedGamedNumber() > 8) {
            return null;
        } else if (player.getPlayedGamedNumber() < 5) {
            return null;
        } else {
            if (player.getPZSzachPoints() < player.getPZSzachRounds().size() / 3.0f) {
                return null;
            }
            return getNorm9Rounds(getRatingPerformance(player), player.getSex());
        }
    }

    public static Title getTitleWithoutLowering(Player player) {
        AtomicInteger wins = new AtomicInteger(0);
        int draws = player.getDrawsNumber();
        int loses = player.getLosesNumber();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger sum = new AtomicInteger(0);
        AtomicReference<Title> title = new AtomicReference<>(null);
        ArrayList<Player> defeated = new ArrayList<>();
        for (Game game : player.getRounds()) {
            if (!game.isForfeit()) {
                switch (player.getRoundResult(game)) {
                    case WIN -> defeated.add(player.getOpponent(game));
                    case DRAW, LOSE -> {
                        count.incrementAndGet();
                        sum.addAndGet(player.getOpponent(game).getPZSzachRating());
                    }
                }
            }
        }

        defeated.parallelStream().sorted(Comparator.comparingInt(Player::getPZSzachRating).reversed())
                .forEachOrdered(opponent -> {
                    sum.addAndGet(opponent.getPZSzachRating());
                    count.getAndIncrement();
                    wins.getAndIncrement();
                    if (wins.get() + draws * 0.5f >= count.get() / 3.0f) {
                        int delta = getRatingDelta(count.get(), wins.get(), loses);
                        int avg = (sum.get() + player.getPZSzachRating()) / (count.get() + 1);
                        Title title1 = getNorm(delta + avg, count.get(), player.getSex());
                        if (getTitleValue(title1, player.getSex()) > getTitleValue(title.get(), player.getSex())) {
                            title.set(title1);
                        }
                    }
                });

        return title.get();
    }

    public static Title getNorm(Player player, Tournament tournament) {
        Title ceil = tournament.getRating().getMaxTitle();
        if (ceil == Title.bk || !tournament.getRating().getPZSzachRated()) {
            return null;
        }
        Title title = player.getPlayerNorm();
        Title title1;

        if (
                (!tournament.getRating().getPZSzach46() ||
                        tournament.getSystem() != Tournament.TournamentSystem.ROUND_ROBIN) &&
                        getTitleValue(player.getTitle(), player.getSex()) < getTitleValue(Title.M, player.getSex())
        ) {
            if (tournament.getRating().getPZSzach43()) {
                title1 = getLessRoundTitle(player, ceil);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                }
            }
            if (tournament.getRating().getPZSzach44()) {
                title1 = getTitleWithoutLowering(player);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                }
            }

            if (tournament.getRating().getPZSzach45()) {
                title1 = getTitleRangeRound(player, ceil);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                }
            }

        }

        if (tournament.getRating().getPZSzach47()) {
            switch (ceil) {
                case M, K, I -> {
                    title1 = smallNorm(player);
                    title1 = lowerTitle(title1, ceil);
                    if (
                            getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                    getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                    ) {
                        title = title1;
                    }
                }
            }
        }

        return title;
    }

    public static String getNormRemarks(Player player, Tournament tournament) {
        Title ceil = tournament.getRating().getMaxTitle();
        if (ceil == Title.bk || !tournament.getRating().getPZSzachRated()) {
            return null;
        }
        Title title = player.getPlayerNorm();
        String remarks = "";
        Title title1;

        if (
                (!tournament.getRating().getPZSzach46() ||
                        tournament.getSystem() != Tournament.TournamentSystem.ROUND_ROBIN) &&
                        getTitleValue(player.getTitle(), player.getSex()) < getTitleValue(Title.M, player.getSex())
        ) {
            if (tournament.getRating().getPZSzach43()) {
                title1 = getLessRoundTitle(player, ceil);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                    remarks = "4.3";
                }
            }
            if (tournament.getRating().getPZSzach44()) {
                title1 = getTitleWithoutLowering(player);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                    remarks = "4.4";
                }
            }

            if (tournament.getRating().getPZSzach45()) {
                title1 = getTitleRangeRound(player, ceil);
                title1 = lowerTitle(title1, ceil);
                if (
                        getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                ) {
                    title = title1;
                    remarks = "4.5";
                }
            }

        }

        if (tournament.getRating().getPZSzach47()) {
            switch (ceil) {
                case M, K, I -> {
                    title1 = smallNorm(player);
                    title1 = lowerTitle(title1, ceil);
                    if (
                            getTitleValue(title1, player.getSex()) > getTitleValue(player.getTitle(), player.getSex()) &&
                                    getTitleValue(title1, player.getSex()) > getTitleValue(title, player.getSex())
                    ) {
                        remarks = "4.7";
                    }
                }
            }
        }

        return remarks;
    }

    public static Title lowerTitle(Title title, Title ceil) {
        int titleValue = getTitleValue(title, null);
        int ceilValue = getTitleValue(ceil, null);
        return titleValue < ceilValue ? title : ceil;
    }
}
