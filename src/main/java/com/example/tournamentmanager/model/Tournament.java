package com.example.tournamentmanager.model;

import com.example.tournamentmanager.comparator.PairingComparator;
import com.example.tournamentmanager.comparator.ResultsComparator;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tournamentmanager.helper.GeneralHelper.ProgressMessageBox.convertToTitleCase;

@XmlRootElement(name = "tournament")
public class Tournament implements Serializable {
    private final Tiebreak tiebreak = new Tiebreak();
    private final transient ObservableList<ArrayList<Game>> roundsObs = FXCollections.observableArrayList();
    private final ArrayList<Withdraw> withdraws = new ArrayList<>();
    private final transient ObservableList<Withdraw> withdrawsObs = FXCollections.observableArrayList();
    private final ArrayList<ResultPredicate<Player>> predicates = new ArrayList<>();
    private final transient ObservableList<ResultPredicate<Player>> predicatesObs = FXCollections.observableArrayList();
    private final transient ObservableList<Schedule.ScheduleElement> scheduleElementsObs = FXCollections.observableArrayList();
    private String name;
    private Date startDate;
    private Date endDate;
    private String place;
    private short gameTime;
    private short increment;
    private byte controlMove;
    private byte controlAddition;
    private Type type;
    private TournamentSystem system;
    private Rating rating = new Rating();
    private byte roundsNumber;
    private String arbiter;
    private String email;
    private String organizer;
    private transient ObservableList<Player> playersObs;
    private PlayerList players = new PlayerList();
    private ArrayList<ArrayList<Game>> rounds = new ArrayList<>();
    private PairingComparator pairingComparator;
    private ResultsComparator resultsComparator;
    private Schedule schedule;
    private Player.Color firstColor = null;


    public Tournament(SwsxTournament swsxTournament) {
        setName(swsxTournament.getName());
        setPlace(swsxTournament.getPlace());
        setStartDate(swsxTournament.getStartDate());
        setEndDate(swsxTournament.getEndDate());
        if (swsxTournament.getReportFide() != null && !swsxTournament.getReportFide().getChiefArbiter().getFullName().isEmpty()) {
            setArbiter(swsxTournament.getReportFide().getChiefArbiter().getFullName());
        } else if (swsxTournament.getReportPol() != null && !swsxTournament.getReportPol().getChiefArbiter().getFullName().isEmpty()) {
            setArbiter(swsxTournament.getReportPol().getChiefArbiter().getFullName());
        } else {
            setArbiter(swsxTournament.getArbiter());
        }

        setRoundsNumber(swsxTournament.getRoundsNo());
        setPlace(swsxTournament.getPlace());
        getRating().setMaxTitle(swsxTournament.getMaxNorm());

        String allottedTimes = swsxTournament.getRate();

        String basicTimeRegex = "(\\d+)'|'*(\\d+)'*"; // Matches basic time in minutes
        String controlMoveRegex = "(\\d+)\\s*/"; // Matches control move
        String controlAdditionRegex = "(\\d+)\\+'"; // Matches control addition
        String incrementRegex = "(\\d+)''|''(\\d+)"; // Matches increment

        Pattern basicTimePattern = Pattern.compile(basicTimeRegex);
        Pattern controlMovePattern = Pattern.compile(controlMoveRegex);
        Pattern controlAdditionPattern = Pattern.compile(controlAdditionRegex);
        Pattern incrementPattern = Pattern.compile(incrementRegex);

        Matcher basicTimeMatcher = basicTimePattern.matcher(allottedTimes);
        Matcher controlMoveMatcher = controlMovePattern.matcher(allottedTimes);
        Matcher controlAdditionMatcher = controlAdditionPattern.matcher(allottedTimes);
        Matcher incrementMatcher = incrementPattern.matcher(allottedTimes);

        short basicTime = 0;
        short increment = 0;
        byte controlMove = 0;
        byte timeAddition = 0;

        if (basicTimeMatcher.find()) {
            String basicTimeString = basicTimeMatcher.group(1) != null ? basicTimeMatcher.group(1) : basicTimeMatcher.group(2);
            if (basicTimeString != null) {
                basicTime = Short.parseShort(basicTimeString);
            }
        }

        if (controlMoveMatcher.find()) {
            String controlMoveString = controlMoveMatcher.group(1);
            if (controlMoveString != null) {
                controlMove = Byte.parseByte(controlMoveString);
            }
        }

        if (controlAdditionMatcher.find()) {
            String controlAdditionString = controlAdditionMatcher.group(1);
            if (controlAdditionString != null) {
                controlAddition = Byte.parseByte(controlAdditionString);
            }
        }

        if (incrementMatcher.find()) {
            String incrementString = incrementMatcher.group(1) != null ? incrementMatcher.group(1) : incrementMatcher.group(2);
            if (incrementString != null) {
                increment = Short.parseShort(incrementString);
            }
        }

        setGameTime(basicTime);
        setIncrement(increment);
        setControlMove(controlMove);
        setControlAddition(controlAddition);
        setSystem(swsxTournament.getSystem());
        int totalTime = basicTime + increment + (controlMove == 0 ? 0 : timeAddition);
        if (totalTime >= 60) {
            setType(Type.STANDARD);
        } else if (totalTime <= 10) {
            setType(Type.BLITZ);
        } else {
            setType(Type.RAPID);
        }

        ArrayList<SwsxTournament.SwsxPlayer> swsxPlayers = swsxTournament.getPlayers();
        ArrayList<ArrayList<Game>> rounds = new ArrayList<>();
        PlayerList players = new PlayerList();
        ArrayList<ArrayList<ObjectId>> roundIds = new ArrayList<>();
        for (SwsxTournament.SwsxPlayer player : swsxPlayers) {
            Player playerTmp = new Player(
                    player.getFullName(),
                    player.getSex(),
                    player.getTitle(),
                    (int) (getType() == Type.BLITZ ? player.getFideRatingBlitz() :
                            getType() == Type.RAPID ? player.getFideRatingRapid() :
                                    player.getFideRatingClassic()),
                    player.getFederation(),
                    player.getFideId(),
                    player.getYearOfBorn(),
                    player.getMonthOfBorn(),
                    player.getDayOfBorn()
            );
            playerTmp.setLocalId(player.getPolId());
            playerTmp.setPlayerid(player.getPlayerId());

            players.add(playerTmp);
            if (player.isWithdrawFromTournament()) {
                getWithdrawsObs().add(
                        new Withdraw(
                                playerTmp,
                                Withdraw.WithdrawType.TOURNAMENT,
                                null
                        )
                );
            }
        }

        for (SwsxTournament.SwsxPlayer player : swsxPlayers) {
            ArrayList<SwsxTournament.SwsxRound> playerRounds = player.getRounds();
            for (int i = 0; i < playerRounds.size(); i++) {
                if (roundIds.size() <= i) {
                    roundIds.add(new ArrayList<>());
                }

                if (rounds.size() <= i) {
                    rounds.add(new ArrayList<>());
                }

                if (!roundIds.get(i).contains(player.getPlayerId())) {
                    SwsxTournament.SwsxRound round = playerRounds.get(i);
                    Player white;
                    Player black;
                    Result whiteResult;
                    Result blackResult = null;
                    StringBuilder hexString = new StringBuilder(Integer.toHexString(round.getOpponentId()));

                    while (hexString.length() < 24) {
                        hexString.insert(0, "0");
                    }
                    ObjectId opponentId = new ObjectId(hexString.toString());
                    boolean forfeit = true;
                    if (round.getColor() == Player.Color.BLACK) {
                        black = players.get(player.getPlayerId());
                        blackResult = player.getRounds().get(i).getResult();
                        if (round.getStatus() == 1) {
                            white = players.get(opponentId);
                            if (white == players.getBye()) {
                                whiteResult = Result.LOSE;
                            } else if (white == players.getHalfbye()) {
                                whiteResult = Result.DRAW;
                                blackResult = Result.DRAW;
                            } else if (white == players.getUnpaired()) {
                                whiteResult = Result.WIN;
                            } else {
                                whiteResult = swsxPlayers.get(players.indexOf(white)).getRounds().get(i).getResult();
                                forfeit = false;
                            }
                        } else if (round.getStatus() == 2) {
                            if (round.getPoints() == 0.5f) {
                                white = players.getHalfbye();
                                whiteResult = Result.DRAW;
                                blackResult = Result.DRAW;
                            } else {
                                white = players.getBye();
                                whiteResult = Result.LOSE;
                                blackResult = Result.WIN;
                            }
                        } else {
                            white = players.getUnpaired();
                            whiteResult = Result.WIN;
                            blackResult = Result.LOSE;
                        }
                    } else {
                        white = players.get(player.getPlayerId());
                        whiteResult = player.getRounds().get(i).getResult();
                        if (round.getStatus() == 1) {
                            black = players.get(opponentId);
                            if (black == players.getBye()) {
                                blackResult = Result.LOSE;
                            } else if (black == players.getHalfbye()) {
                                whiteResult = Result.DRAW;
                                blackResult = Result.DRAW;
                            } else if (black == players.getUnpaired()) {
                                whiteResult = Result.WIN;
                            } else {
                                blackResult = swsxPlayers.get(players.indexOf(black)).getRounds().get(i).getResult();
                                forfeit = false;
                            }
                        } else if (round.getStatus() == 2) {
                            if (round.getPoints() == 0.5f) {
                                black = players.getHalfbye();
                                whiteResult = Result.DRAW;
                                blackResult = Result.DRAW;
                            } else {
                                black = players.getBye();
                                whiteResult = Result.WIN;
                                blackResult = Result.LOSE;
                            }
                        } else {
                            black = players.getUnpaired();
                            whiteResult = Result.LOSE;
                            blackResult = Result.WIN;
                        }
                    }
                    if (white == players.getBye() || white == players.getHalfbye() || white == players.getUnpaired()) {
                        Player tmpPlayer = white;
                        Result tmpResult = whiteResult;
                        white = black;
                        black = tmpPlayer;
                        whiteResult = blackResult;
                        blackResult = tmpResult;
                    }

                    if (black == players.getBye()) {
                        whiteResult = Result.WIN;
                        blackResult = Result.LOSE;
                        forfeit = true;
                    } else if (black == players.getHalfbye()) {
                        whiteResult = Result.DRAW;
                        blackResult = Result.DRAW;
                        getWithdrawsObs().add(
                                new Withdraw(
                                        white,
                                        Withdraw.WithdrawType.HALFBYE,
                                        (byte) (i + 1)
                                )
                        );
                        forfeit = true;
                    } else if (black == players.getUnpaired()) {
                        whiteResult = Result.LOSE;
                        blackResult = Result.WIN;
                        forfeit = true;
                        if (round.getStatus() == 0) {
                            if (!isTournamentWithdraw(white)) {
                                getWithdrawsObs().add(
                                        new Withdraw(
                                                white,
                                                Withdraw.WithdrawType.TOURNAMENT,
                                                null
                                        )
                                );
                            }
                        } else {
                            getWithdrawsObs().add(
                                    new Withdraw(
                                            white,
                                            Withdraw.WithdrawType.ROUND,
                                            (byte) (i + 1)
                                    )
                            );
                        }
                    }

                    Game game = new Game(white, black, whiteResult, blackResult, round.isForfeit() || forfeit);
                    roundIds.get(i).add(player.getPlayerId());
                    roundIds.get(i).add(opponentId);
                    rounds.get(i).add(game);


                }
            }
        }

        for (ArrayList<Game> round : rounds) {
            for (Game game : round) {
                game.getWhite().addRound(game);
                game.getBlack().addRound(game);
            }
            if (system == TournamentSystem.SWISS) {
                round.sort(new PairingComparator());
            }
        }


        setPlayers(players);
        setRounds(rounds);

        try {
            setSchedule(new Schedule(swsxTournament.getReportPol().getSchedule()));
        } catch (Exception e) {
            setSchedule(new Schedule());
            e.printStackTrace();
        }
        getScheduleElementsObs().addAll(getSchedule());

        setSchedule(new Schedule());
        getScheduleElementsObs().addAll(getSchedule());

        setPairingComparator(new PairingComparator(getPlayersObs()));
        setResultsComparator(new ResultsComparator(getTiebreak()));
        getPlayers().getComparator().setCriteria1(swsxTournament.getSort0());
        getPlayers().getComparator().setCriteria2(swsxTournament.getSort1());
        getPlayers().getComparator().setCriteria3(swsxTournament.getSort2());
        getPlayers().getComparator().setCriteria4(swsxTournament.getSort3());
        getPlayers().getComparator().setCriteria5(swsxTournament.getSort4());
        getPlayersObs().addListener((ListChangeListener<? super Player>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPlayers().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPlayers().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPlayers().subList(from, to + 1).clear();
                    getPlayers().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getRounds().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getRounds().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getRounds().subList(from, to + 1).clear();
                    getRounds().addAll(from, change.getList().subList(from, to + 1));
                }
            }

            for (Player player : getPlayersObs()) {
                player.getRounds().clear();
            }

            for (ArrayList<Game> round : getRoundsObs()) {
                for (Game game : round) {
                    game.getWhite().addRound(game);
                    game.getBlack().addRound(game);
                }
            }

        });

        getWithdrawsObs().addListener((ListChangeListener<? super Withdraw>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getWithdraws().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getWithdraws().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getWithdraws().subList(from, to + 1).clear();
                    getWithdraws().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getPredicatesObs().addListener((ListChangeListener<? super ResultPredicate<Player>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPredicates().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPredicates().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPredicates().subList(from, to + 1).clear();
                    getPredicates().addAll(from, change.getList().subList(from, to + 1));
                }
            }
            System.out.println(predicates);
        });

        setSchedule(new Schedule());
        getScheduleElementsObs().addAll(getSchedule());

        getScheduleElementsObs().addListener((ListChangeListener<? super Schedule.ScheduleElement>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getSchedule().addAll(change.getAddedSubList()
                            .stream().filter(e -> e.getType() == Schedule.ScheduleElement.Type.ROUND)
                            .toList()
                    );
                }
                if (change.wasRemoved()) {
                    getSchedule().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getSchedule().subList(from, to + 1).clear();
                    getSchedule().addAll(from, change.getList().subList(from, to + 1)
                            .stream().filter(e -> e.getType() != Schedule.ScheduleElement.Type.BRIEFING && e.getType() != Schedule.ScheduleElement.Type.CLOSING_CEREMONY)
                            .toList()
                    );
                }
            }
        });
    }


    public Tournament(TrfTournament trfTournament) {
        setName(trfTournament.getName());
        setPlace(trfTournament.getCity());
        setStartDate(trfTournament.getStartDate());
        setEndDate(trfTournament.getEndDate());
        setArbiter(trfTournament.getChiefArbiter());
        setRoundsNumber(trfTournament.getRoundsNo());

        String allottedTimes = trfTournament.getAllottedTimes();

        String basicTimeRegex = "\\d+\\s*min"; // Matches basic time in minutes
        String incrementRegex = "(\\d+)\\s*sec\\s*increment\\s*per\\s*move"; // Matches move increment
        String controlMoveRegex = "(\\d+)\\s*min/([\\d]+)moves"; // Matches control move and time addition

        Pattern basicTimePattern = Pattern.compile(basicTimeRegex);
        Pattern incrementPattern = Pattern.compile(incrementRegex);
        Pattern controlMovePattern = Pattern.compile(controlMoveRegex);

        Matcher basicTimeMatcher = basicTimePattern.matcher(allottedTimes);
        Matcher incrementMatcher = incrementPattern.matcher(allottedTimes);
        Matcher controlMoveMatcher = controlMovePattern.matcher(allottedTimes);

        short basicTime = 0;
        short moveIncrement = 0;
        byte controlMove = 0;
        byte timeAddition = 0;

        if (basicTimeMatcher.find()) {
            String basicTimeStr = basicTimeMatcher.group();
            basicTime = Short.parseShort(basicTimeStr.replaceAll("\\D+", ""));
        }

        if (incrementMatcher.find()) {
            String incrementStr = incrementMatcher.group(1);
            moveIncrement = Short.parseShort(incrementStr);
        }

        if (controlMoveMatcher.find()) {
            String controlMoveStr = controlMoveMatcher.group(1);
            String timeAdditionStr = controlMoveMatcher.group(2);

            controlMove = Byte.parseByte(controlMoveStr);
            timeAddition = Byte.parseByte(timeAdditionStr);
        }

        setGameTime(basicTime);
        setIncrement(moveIncrement);
        setControlMove(controlMove);
        setControlAddition(timeAddition);
        setSystem(trfTournament.getSystem());

        int totalTime = basicTime + increment + (controlMove == 0 ? 0 : timeAddition);
        if (totalTime >= 60) {
            setType(Type.STANDARD);
        } else if (totalTime <= 10) {
            setType(Type.BLITZ);
        } else {
            setType(Type.RAPID);
        }

        ArrayList<TrfTournament.TrfPlayer> trfPlayers = trfTournament.getPlayers();
        ArrayList<ArrayList<Game>> rounds = new ArrayList<>();
        PlayerList players = new PlayerList();
        ArrayList<ArrayList<Integer>> roundIds = new ArrayList<>();
        for (TrfTournament.TrfPlayer player : trfPlayers) {
            players.add(
                    new Player(
                            player.getName(),
                            player.getSex(),
                            player.getTitle(),
                            player.getRating(),
                            player.getFederation(),
                            player.getFideNo(),
                            player.getYearOfBirth(),
                            player.getMonthOfBirth(),
                            player.getDayOfBirth()
                    )
            );
        }

        for (TrfTournament.TrfPlayer player : trfPlayers) {
            ArrayList<TrfTournament.TrfRound> playerRounds = player.getRounds();
            for (int i = 0; i < playerRounds.size(); i++) {
                if (roundIds.size() <= i) {
                    roundIds.add(new ArrayList<>());
                }
                if (rounds.size() <= i) {
                    rounds.add(new ArrayList<>());
                }
                if (!roundIds.get(i).contains(player.getStartRank())) {
                    TrfTournament.TrfRound round = playerRounds.get(i);
                    Player white;
                    Player black;
                    char whiteResult;
                    char blackResult;
                    int opponentId = round.getOpponentId();
                    if (round.getColor() == 'b') {
                        black = players.get(player.getStartRank() - 1);
                        blackResult = player.getRounds().get(i).getResult();
                        if (opponentId == 0) {
                            switch (blackResult) {
                                case 'U', 'F' -> {
                                    white = players.getBye();
                                    whiteResult = '-';
                                }
                                case 'H' -> {
                                    white = players.getHalfbye();
                                    whiteResult = 'H';
                                }
                                default -> {
                                    white = players.getUnpaired();
                                    whiteResult = '+';
                                }
                            }

                        } else {
                            white = players.get(round.getOpponentId() - 1);
                            whiteResult = trfPlayers.get(round.getOpponentId() - 1).getRounds().get(i).getResult();
                        }
                    } else {
                        white = players.get(player.getStartRank() - 1);
                        whiteResult = player.getRounds().get(i).getResult();
                        if (opponentId == 0) {
                            switch (whiteResult) {
                                case 'U', 'F' -> {
                                    black = players.getBye();
                                    blackResult = '-';
                                }
                                case 'H' -> {
                                    black = players.getHalfbye();
                                    blackResult = 'H';
                                }
                                default -> {
                                    black = players.getUnpaired();
                                    blackResult = '+';
                                }
                            }

                        } else {
                            blackResult = trfPlayers.get(round.getOpponentId() - 1).getRounds().get(i).getResult();
                            black = players.get(round.getOpponentId() - 1);
                        }
                    }
                    Game game = new Game(white, black);
                    boolean whiteForfeit = true;
                    boolean blackForfeit = true;
                    switch (whiteResult) {
                        case '\0' -> game.setWhiteResult(null);
                        case '+', 'W', '1', 'U', 'F' -> game.setWhiteResult(Result.WIN);
                        case '=', 'H' -> game.setWhiteResult(Result.DRAW);
                        default -> game.setWhiteResult(Result.LOSE);
                    }
                    switch (whiteResult) {
                        case '1', '0', '=', 'W', 'D', 'L' -> whiteForfeit = false;
                    }

                    switch (blackResult) {
                        case '\0' -> game.setBlackResult(null);
                        case '+', 'W', '1', 'U', 'F' -> game.setBlackResult(Result.WIN);
                        case '=', 'H' -> game.setBlackResult(Result.DRAW);
                        default -> game.setBlackResult(Result.LOSE);
                    }
                    switch (blackResult) {
                        case '1', '0', '=', 'W', 'D', 'L' -> blackForfeit = false;
                    }

                    game.setForfeit(whiteForfeit && blackForfeit);
                    roundIds.get(i).add(player.getStartRank());
                    roundIds.get(i).add(round.getOpponentId());
                    rounds.get(i).add(game);
                }

            }

        }

        for (ArrayList<Game> round : rounds) {
            for (Game game : round) {
                game.getWhite().addRound(game);
                game.getBlack().addRound(game);
            }
            if (system == TournamentSystem.SWISS) {
                round.sort(new PairingComparator());
            }
        }


        setPlayers(players);
        setRounds(rounds);

        setPairingComparator(new PairingComparator(playersObs));
        setResultsComparator(new ResultsComparator(getTiebreak()));

        Schedule schedule1 = Schedule.createFromDates(trfTournament.getRoundDates());
        schedule1.setBriefing(new Schedule.ScheduleElement(Schedule.ScheduleElement.Type.BRIEFING, (byte) 0, getStartDate()));
        schedule1.setClosing(new Schedule.ScheduleElement(Schedule.ScheduleElement.Type.CLOSING_CEREMONY, (byte) 0, getEndDate()));
        setSchedule(schedule1);

        getScheduleElementsObs().addAll(getSchedule());
        getPlayersObs().addListener((ListChangeListener<? super Player>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPlayers().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPlayers().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPlayers().subList(from, to + 1).clear();
                    getPlayers().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getRounds().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getRounds().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getRounds().subList(from, to + 1).clear();
                    getRounds().addAll(from, change.getList().subList(from, to + 1));
                }
            }

            for (Player player : getPlayersObs()) {
                player.getRounds().clear();
            }

            for (ArrayList<Game> round : getRoundsObs()) {
                for (Game game : round) {
                    game.getWhite().addRound(game);
                    game.getBlack().addRound(game);
                }
            }

        });

        getWithdrawsObs().addListener((ListChangeListener<? super Withdraw>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getWithdraws().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getWithdraws().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getWithdraws().subList(from, to + 1).clear();
                    getWithdraws().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getPredicatesObs().addListener((ListChangeListener<? super ResultPredicate<Player>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPredicates().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPredicates().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPredicates().subList(from, to + 1).clear();
                    getPredicates().addAll(from, change.getList().subList(from, to + 1));
                }
            }
            System.out.println(predicates);
        });

        setSchedule(new Schedule());
        getScheduleElementsObs().addAll(getSchedule());

        getScheduleElementsObs().addListener((ListChangeListener<? super Schedule.ScheduleElement>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getSchedule().addAll(change.getAddedSubList()
                            .stream().filter(e -> e.getType() == Schedule.ScheduleElement.Type.ROUND)
                            .toList()
                    );
                }
                if (change.wasRemoved()) {
                    getSchedule().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getSchedule().subList(from, to + 1).clear();
                    getSchedule().addAll(from, change.getList().subList(from, to + 1)
                            .stream().filter(e -> e.getType() != Schedule.ScheduleElement.Type.BRIEFING && e.getType() != Schedule.ScheduleElement.Type.CLOSING_CEREMONY)
                            .toList()
                    );
                }
            }
        });
    }


    public Tournament(
    ) {
        this(
                "", new Date(), new Date(), "", (short) 0, (short) 0, (byte) 0, (byte) 0, Type.STANDARD, TournamentSystem.SWISS, (byte) 0,
                "", "", "", new PlayerList());

    }

    Tournament(
            String name,
            Date startDate,
            Date endDate,
            String place,
            short gameTime,
            short increment,
            byte controlMove,
            byte controlAddition,
            Type type,
            TournamentSystem system,
            byte roundsNumber,
            String arbiter,
            String email,
            String organizer,
            PlayerList players
    ) {
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setPlace(place);
        setGameTime(gameTime);
        setIncrement(increment);
        setControlAddition(controlMove);
        setControlAddition(controlAddition);
        setType(type);
        setSystem(system);
        setRoundsNumber(roundsNumber);
        setArbiter(arbiter);
        setEmail(email);
        setOrganizer(organizer);
        setPlayers(players);
        playersObs = FXCollections.observableArrayList(getPlayers());
        FXCollections.sort(getPlayersObs(), getPlayers().getComparator());
        getPlayersObs().addListener((ListChangeListener<? super Player>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPlayers().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPlayers().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPlayers().subList(from, to + 1).clear();
                    getPlayers().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getRounds().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getRounds().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getRounds().subList(from, to + 1).clear();
                    getRounds().addAll(from, change.getList().subList(from, to + 1));
                }
            }

            for (Player player : getPlayersObs()) {
                player.getRounds().clear();
            }

            for (ArrayList<Game> round : getRoundsObs()) {
                for (Game game : round) {
                    game.getWhite().addRound(game);
                    game.getBlack().addRound(game);
                }
            }

        });

        getWithdrawsObs().addListener((ListChangeListener<? super Withdraw>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getWithdraws().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getWithdraws().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getWithdraws().subList(from, to + 1).clear();
                    getWithdraws().addAll(from, change.getList().subList(from, to + 1));
                }
            }
        });

        getPredicatesObs().addListener((ListChangeListener<? super ResultPredicate<Player>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getPredicates().addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    getPredicates().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getPredicates().subList(from, to + 1).clear();
                    getPredicates().addAll(from, change.getList().subList(from, to + 1));
                }
            }
            System.out.println(predicates);
        });

        setSchedule(new Schedule());
        getScheduleElementsObs().addAll(getSchedule());

        getScheduleElementsObs().addListener((ListChangeListener<? super Schedule.ScheduleElement>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    getSchedule().addAll(change.getAddedSubList()
                            .stream().filter(e -> e.getType() == Schedule.ScheduleElement.Type.ROUND)
                            .toList()
                    );
                }
                if (change.wasRemoved()) {
                    getSchedule().removeAll(change.getRemoved());
                }
                if (change.wasUpdated()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    getSchedule().subList(from, to + 1).clear();
                    getSchedule().addAll(from, change.getList().subList(from, to + 1)
                            .stream().filter(e -> e.getType() != Schedule.ScheduleElement.Type.BRIEFING && e.getType() != Schedule.ScheduleElement.Type.CLOSING_CEREMONY)
                            .toList()
                    );
                }
            }
        });


        setPairingComparator(new PairingComparator(getPlayersObs()));
        setResultsComparator(new ResultsComparator(getTiebreak()));
    }

    public boolean isTournamentWithdraw(Player player) {
        for (Withdraw withdraw : getWithdraws()) {
            if (withdraw.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "tournament\n" +
                "name: " + getName() + "\n" +
                "date:\n\t" + getStartDate() + " - " + getEndDate() + "\n" +
                "place: " + getPlace() + "\n" +
                "game time: " + getGameTime() + "\n" +
                "increment: " + getIncrement() + "\n" +
                "control" + "\n\t" + "move: " + getControlMove() + "\n\taddition " + getControlAddition() + "\n" +
                "type: " + getType() + "\n" +
                "system: " + getSystem() + "\n" +
                "round number: " + getRoundsNumber() + "\n" +
                "arbiter: " + getArbiter() + "\n" +
                "email: " + getEmail() + "\n" +
                "organizer: " + getOrganizer() + "\n" +
                "rating:" + getRating() + "\n" +
                "tiebreak" + getTiebreak() + "\n" +
                "players: " + getPlayers() + "\n";
    }

    public ResultsComparator getResultsComparator() {
        return resultsComparator;
    }

    public void setResultsComparator(ResultsComparator resultsComparator) {
        this.resultsComparator = resultsComparator;
    }


    public ArrayList<Game> getRound(int roundNo) {
        return rounds.get(roundNo);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getArbiter() {
        return arbiter;
    }

    public void setArbiter(String arbiter) {
        this.arbiter = arbiter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Tiebreak getTiebreak() {
        return tiebreak;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public short getIncrement() {
        return increment;
    }

    public void setIncrement(short increment) {
        this.increment = increment;
    }

    public byte getControlMove() {
        return controlMove;
    }

    public void setControlMove(byte controlMove) {
        this.controlMove = controlMove;
    }

    public byte getControlAddition() {
        return controlAddition;
    }

    public void setControlAddition(byte controlAddition) {
        this.controlAddition = controlAddition;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public byte getRoundsNumber() {
        return roundsNumber;
    }

    public void setRoundsNumber(byte roundsNumber) {
        if (getScheduleElementsObs().size() >= 2) {
            if (getScheduleElementsObs().size() - 2 < roundsNumber) {
                for (int i = getScheduleElementsObs().size() - 2; i < roundsNumber; i++) {
                    getScheduleElementsObs().add(getScheduleElementsObs().size() - 1, new Schedule.ScheduleElement(Schedule.ScheduleElement.Type.ROUND, (byte) (i + 1)));
                }
            } else if (getScheduleElementsObs().size() - 2 > roundsNumber) {
                getScheduleElementsObs().subList(roundsNumber + 1, getScheduleElementsObs().size() - 1).clear();
            }
        }
        this.roundsNumber = roundsNumber;
    }


    @XmlElementWrapper(name = "players")
    @XmlAnyElement(lax = true)
    public PlayerList getPlayers() {
        return players;
    }

    public void setPlayers(PlayerList players) {
        this.players = players;
    }

    public short getGameTime() {
        return gameTime;
    }

    public void setGameTime(short gameTime) {
        this.gameTime = gameTime;
    }

    public TournamentSystem getSystem() {
        return system;
    }

    public void setSystem(TournamentSystem system) {
        this.system = system;
    }


    public ArrayList<ArrayList<Game>> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<ArrayList<Game>> rounds) {
        this.rounds = rounds;
    }

    @XmlTransient
    public ObservableList<Player> getPlayersObs() {
        return playersObs;
    }

    public PairingComparator getPairingComparator() {
        if (getSystem() == TournamentSystem.SWISS) {
            return pairingComparator;
        } else {
            return null;
        }
    }

    public void setPairingComparator(PairingComparator pairingComparator) {
        this.pairingComparator = pairingComparator;
    }

    public ObservableList<ArrayList<Game>> getRoundsObs() {
        return roundsObs;
    }

    public ArrayList<Withdraw> getWithdraws() {
        return withdraws;
    }

    public ObservableList<Withdraw> getWithdrawsObs() {
        return withdrawsObs;
    }

    public ArrayList<ResultPredicate<Player>> getPredicates() {
        return predicates;
    }

    public ObservableList<ResultPredicate<Player>> getPredicatesObs() {
        return predicatesObs;
    }

    public ObservableList<Schedule.ScheduleElement> getScheduleElementsObs() {
        return scheduleElementsObs;
    }

    public Player.Color getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(Player.Color firstColor) {
        this.firstColor = firstColor;
    }

    public enum Type implements Serializable {
        STANDARD,
        RAPID,
        BLITZ,
        OTHER;

        public static Type getType(String symbol) {
            switch (symbol) {
                case "2" -> {
                    return RAPID;
                }
                case "3" -> {
                    return BLITZ;
                }
                case "4" -> {
                    return OTHER;
                }
                default -> {
                    return STANDARD;
                }
            }
        }
    }

    public enum TournamentSystem implements Serializable {
        SWISS,
        ROUND_ROBIN,
        CUP;

        @Override
        public String toString() {
            String value = super.toString();
            return value.replaceAll("_", " ");
        }
    }

    public static class Tiebreak implements Serializable {
        private boolean FIDEMode;
        private TbMethod tiebreak1 = TbMethod.POINTS;
        private TbMethod tiebreak2 = TbMethod.BUCHOLZ_CUT1;
        private TbMethod tiebreak3 = TbMethod.BUCHOLZ;
        private TbMethod tiebreak4 = TbMethod.SONNEN_BERGER;
        private TbMethod tiebreak5 = TbMethod.PROGRESS;
        private Float winPoints = 1.0F;
        private Float drawPoints = 0.5F;
        private Float losePoints = 0.0F;
        private Float forfeitWinPoints = 1.0F;
        private Float forfeitLosePoints = 0F;
        private Float byePoints = 1.0F;
        private Float halfByePoints = 0.5F;

        @Override
        public String toString() {
            return "\n\ttiebreak 1: " + getTiebreak1() +
                    "\n\ttiebreak 2: " + getTiebreak2() +
                    "\n\ttiebreak 3: " + getTiebreak3() +
                    "\n\ttiebreak 4: " + getTiebreak4() +
                    "\n\ttiebreak 5: " + getTiebreak5() +
                    "\n\twin points: " + getWinPoints() +
                    "\n\tdraw points: " + getDrawPoints() +
                    "\n\tlose points: " + getLosePoints() +
                    "\n\tforfeit winPoints: " + getForfeitWinPoints() +
                    "\n\tforfeit losePoints: " + getForfeitLosePoints() +
                    "\n\tbye points: " + getByePoints() +
                    "\n\thalfbye points: " + getHalfByePoints();
        }

        public Float getHalfByePoints() {
            return halfByePoints;
        }

        public void setHalfByePoints(Float halfByePoints) {
            this.halfByePoints = halfByePoints;
        }

        public boolean isFIDEMode() {
            return FIDEMode;
        }

        public void setFIDEMode(boolean FIDEMode) {
            this.FIDEMode = FIDEMode;
        }

        public TbMethod getTiebreak1() {
            return tiebreak1;
        }

        public void setTiebreak1(TbMethod tiebreak1) {
            this.tiebreak1 = tiebreak1;
        }

        public TbMethod getTiebreak2() {
            return tiebreak2;
        }

        public void setTiebreak2(TbMethod tiebreak2) {
            this.tiebreak2 = tiebreak2;
        }

        public TbMethod getTiebreak3() {
            return tiebreak3;
        }

        public void setTiebreak3(TbMethod tiebreak3) {
            this.tiebreak3 = tiebreak3;
        }

        public TbMethod getTiebreak4() {
            return tiebreak4;
        }

        public void setTiebreak4(TbMethod tiebreak4) {
            this.tiebreak4 = tiebreak4;
        }

        public TbMethod getTiebreak5() {
            return tiebreak5;
        }

        public void setTiebreak5(TbMethod tiebreak5) {
            this.tiebreak5 = tiebreak5;
        }

        public Float getWinPoints() {
            return winPoints;
        }

        public void setWinPoints(Float winPoints) {
            this.winPoints = winPoints;
            Player.setWinPoints(winPoints);
        }

        public Float getDrawPoints() {
            return drawPoints;
        }

        public void setDrawPoints(Float drawPoints) {
            this.drawPoints = drawPoints;
            Player.setDrawPoints(drawPoints);
        }

        public Float getLosePoints() {
            return losePoints;
        }

        public void setLosePoints(Float losePoints) {
            this.losePoints = losePoints;
            Player.setLosePoints(losePoints);
        }

        public Float getForfeitWinPoints() {
            return forfeitWinPoints;
        }

        public void setForfeitWinPoints(Float forfeitWinPoints) {
            this.forfeitWinPoints = forfeitWinPoints;
            Player.setForfeitWinPoints(forfeitWinPoints);
        }

        public Float getForfeitLosePoints() {
            return forfeitLosePoints;
        }

        public void setForfeitLosePoints(Float forfeitLosePoints) {
            this.forfeitLosePoints = forfeitLosePoints;
            Player.setForfeitLosePoints(forfeitLosePoints);
        }

        public Float getByePoints() {
            return byePoints;
        }

        public void setByePoints(Float byePoints) {
            this.byePoints = byePoints;
            Player.setByePoints(byePoints);
        }

        public enum TbMethod implements Serializable {
            POINTS,
            PLAYOFF,
            DUEL,
            WINS,
            GAMES_WITH_BLACK,
            WINS_WITH_BLACK,
            RATING_PERFORMENCE_FIDE,
            RATING_PERFORMENCE_PZSZACH,
            AVERAGE_OPPONENTS_RATING,
            AVERAGE_OPPONENTS_RATING_CUT1,
            AVERAGE_OPPONENTS_LOCAL_RATING,
            BUCHOLZ,
            MEDIA_BUCHOLZ,
            BUCHOLZ_CUT1,
            MODIFIED_BUCHOLZ,
            SONNEN_BERGER,
            PROGRESS,
            KOYA;

            public static TbMethod getTbMethod(String symbol) {
                switch (symbol) {
                    case "11" -> {
                        return POINTS;
                    }
                    case "1" -> {
                        return BUCHOLZ;
                    }
                    case "2", "23" -> {
                        return MEDIA_BUCHOLZ;
                    }
                    case "28" -> {
                        return BUCHOLZ_CUT1;
                    }
                    case "3" -> {
                        return PROGRESS;
                    }
                    case "4" -> {
                        return WINS;
                    }
                    case "5" -> {
                        return AVERAGE_OPPONENTS_RATING;
                    }
                    case "29" -> {
                        return AVERAGE_OPPONENTS_RATING_CUT1;
                    }
                    case "6" -> {
                        return SONNEN_BERGER;
                    }
                    case "7" -> {
                        return KOYA;
                    }
                    case "8" -> {
                        return DUEL;
                    }
                    case "22" -> {
                        return RATING_PERFORMENCE_PZSZACH;
                    }
                    default -> {
                        return null;
                    }
                }
            }

            public String prettyText() {
                switch (this) {
                    case GAMES_WITH_BLACK -> {
                        return "Black";
                    }
                    case WINS_WITH_BLACK -> {
                        return "Black wins";
                    }
                    case BUCHOLZ -> {
                        return "Bch";
                    }
                    case BUCHOLZ_CUT1 -> {
                        return "CBch";
                    }
                    case SONNEN_BERGER -> {
                        return "S.B.";
                    }
                    case PROGRESS -> {
                        return "Prog";
                    }
                    case RATING_PERFORMENCE_PZSZACH -> {
                        return "RtgPerfPol";
                    }
                    case AVERAGE_OPPONENTS_RATING -> {
                        return "AvgOppRtg";
                    }
                    case AVERAGE_OPPONENTS_LOCAL_RATING -> {
                        return "AvgOppLocRtg";
                    }
                    case AVERAGE_OPPONENTS_RATING_CUT1 -> {
                        return "AvgOppRtgCut1";
                    }
                    case MEDIA_BUCHOLZ -> {
                        return "MeBch";
                    }
                    case RATING_PERFORMENCE_FIDE -> {
                        return "RtgPerf";
                    }
                    case MODIFIED_BUCHOLZ -> {
                        return "MoBch";
                    }
                    default -> {
                        return convertToTitleCase(toString());
                    }
                }
            }

        }
    }

    public static class Rating implements Serializable {
        private Boolean PZSzachRated = false;
        private Boolean FIDERated = false;
        private byte minInitGames;
        private short ratingFloor;
        private Boolean PZSzach43 = true;
        private Boolean PZSzach44 = true;
        private Boolean PZSzach46 = true;
        private Boolean PZSzach47 = true;
        private Title maxTitle;
        private Boolean twoOtherFederations = true;
        private byte minTitleGames;

        Rating() {
            this((byte) 5, (short) 1000, Title.M, (byte) 9);
        }

        Rating(byte minInitGames, short ratingFloor, Title maxTitle, byte minTitleGames) {
            setMinInitGames(minInitGames);
            setRatingFloor(ratingFloor);
            setMaxTitle(maxTitle);
            setMinTitleGames(minTitleGames);
        }

        @Override
        public String toString() {
            return "\n\tPZSzach rated: " + getPZSzachRated() +
                    "\n\tFIDE rated: " + getFIDERated() +
                    "\n\tmin init games: " + getMinInitGames() +
                    "\n\trating floor: " + getRatingFloor() +
                    "\n\tPZSzach 4.3: " + getPZSzach43() +
                    "\n\tPZSzach 4.2: " + getPZSzach44() +
                    "\n\tPZSzach 4.6: " + getPZSzach46() +
                    "\n\tPZSzach 4.7: " + getPZSzach47() +
                    "\n\tmax Title: " + getMaxTitle() +
                    "\n\ttwo other federations: " + getTwoOtherFederations() +
                    "\n\tmin title games: " + getMinTitleGames();
        }

        public Boolean getPZSzachRated() {
            return PZSzachRated;
        }

        public void setPZSzachRated(Boolean PZSzachRated) {
            if (PZSzachRated != null) {
                this.PZSzachRated = PZSzachRated;
            }
        }

        public Boolean getFIDERated() {
            return FIDERated;
        }

        public void setFIDERated(Boolean FIDERated) {
            if (FIDERated != null) {
                this.FIDERated = FIDERated;
            }
        }

        public byte getMinInitGames() {
            return minInitGames;
        }

        public void setMinInitGames(byte minInitGames) {
            this.minInitGames = minInitGames;
        }

        public short getRatingFloor() {
            return ratingFloor;
        }

        public void setRatingFloor(short ratingFloor) {
            this.ratingFloor = ratingFloor;
        }

        public Boolean getPZSzach43() {
            return PZSzach43;
        }

        public void setPZSzach43(Boolean PZSzach43) {
            if (PZSzach43 != null) {
                this.PZSzach43 = PZSzach43;
            }
        }

        public Boolean getPZSzach44() {
            return PZSzach44;
        }

        public void setPZSzach44(Boolean PZSzach44) {
            if (PZSzach44 != null) {
                this.PZSzach44 = PZSzach44;
            }
        }

        public Boolean getPZSzach46() {
            return PZSzach46;
        }

        public void setPZSzach46(Boolean PZSzach46) {
            if (PZSzach46 != null) {
                this.PZSzach46 = PZSzach46;
            }
        }

        public Boolean getPZSzach47() {
            return PZSzach47;
        }

        public void setPZSzach47(Boolean PZSzach47) {
            if (PZSzach47 != null) {
                this.PZSzach47 = PZSzach47;
            }
        }

        public Title getMaxTitle() {
            return maxTitle;
        }

        public void setMaxTitle(Title maxTitle) {
            this.maxTitle = maxTitle;
        }

        public Boolean getTwoOtherFederations() {
            return twoOtherFederations;
        }

        public void setTwoOtherFederations(Boolean twoOtherFederations) {
            if (twoOtherFederations != null) {
                this.twoOtherFederations = twoOtherFederations;
            }
        }

        public byte getMinTitleGames() {
            return minTitleGames;
        }

        public void setMinTitleGames(byte minTitleGames) {
            this.minTitleGames = minTitleGames;
        }

    }
}