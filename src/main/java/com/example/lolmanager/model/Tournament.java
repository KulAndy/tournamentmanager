package com.example.lolmanager.model;

import com.example.lolmanager.comparator.PairingComparator;
import com.example.lolmanager.comparator.ResultsComparator;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement(name = "tournament")
public class Tournament implements Serializable {
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
    private Tiebreak tiebreak = new Tiebreak();
    private ArrayList<ArrayList<Game>> rounds = new ArrayList<>();
    private transient ObservableList<ArrayList<Game>> roundsObs = FXCollections.observableArrayList();
    private PairingComparator pairingComparator;
    private ResultsComparator resultsComparator;

    public Tournament(TrfTournament trfTournament){
        setName(trfTournament.getName());
        setPlace(trfTournament.getCity());
        setStartDate(trfTournament.getStartDate());
        setEndDate(endDate);
        setArbiter(trfTournament.getChiefArbiter());
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

        int totalTime = basicTime + increment + (controlMove== 0 ? 0 : timeAddition );
        if (totalTime >= 60){
            setType(Type.STANDARD);
        } else if (totalTime <= 10) {
            setType(Type.BLITZ);
        }else {
            setType(Type.RAPID);
        }

        ArrayList<TrfTournament.TrfPlayer> trfPlayers = trfTournament.getPlayers();
        ArrayList<ArrayList<Game>> rounds = new ArrayList<>();
        PlayerList players = new PlayerList();
        ArrayList<ArrayList<Integer>> roundIds = new ArrayList<>();
        for (TrfTournament.TrfPlayer player: trfPlayers){
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

        for (TrfTournament.TrfPlayer player: trfPlayers){
            ArrayList<TrfTournament.TrfRound> playerRounds = player.getRounds();
            for (int i = 0; i < playerRounds.size() ; i++) {
                if (roundIds.size() <= i){
                    roundIds.add(new ArrayList<>());
                }
                if (rounds.size() <= i){
                    rounds.add(new ArrayList<>());
                }
                if (!roundIds.get(i).contains(player.getStartRank())){
                    TrfTournament.TrfRound round = playerRounds.get(i);
                    Player white;
                    Player black;
                    char whiteResult;
                    char blackResult;
                    int opponentId = round.getOpponentId();
                    if (round.getColor() == 'b'){
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

                        }else{
                            white = players.get(round.getOpponentId() - 1);
                            whiteResult = trfPlayers.get(round.getOpponentId() - 1).getRounds().get(i).getResult();
                        }
                    }else{
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

                        }else{
                            blackResult = trfPlayers.get(round.getOpponentId() - 1).getRounds().get(i).getResult();
                            black = players.get(round.getOpponentId() - 1);
                        }
                    }
                    Game game = new Game(white, black);
                    boolean whiteForfeit = true;
                    boolean blackForfeit = true;
                    switch (whiteResult) {
                        case '+', 'W', '1', 'U', 'F' -> game.setWhiteResult(Result.WIN);
                        case '=', 'H' -> game.setWhiteResult(Result.DRAW);
                        default -> game.setWhiteResult(Result.LOSE);
                    }
                    switch (whiteResult){
                        case '1', '0', '=', 'W', 'D', 'L' -> whiteForfeit = false;
                    }

                    switch (blackResult) {
                        case '+', 'W', '1', 'U', 'F' -> game.setBlackResult(Result.WIN);
                        case '=', 'H' -> game.setBlackResult(Result.DRAW);
                        default -> game.setBlackResult(Result.LOSE);
                    }
                    switch (blackResult){
                        case '1', '0', '=', 'W', 'D', 'L' -> blackForfeit = false;
                    }

                    game.setForfeit(whiteForfeit && blackForfeit);
                    roundIds.get(i).add(player.getStartRank());
                    roundIds.get(i).add(round.getOpponentId());
                    rounds.get(i).add(game);
                }
            }

        }

        for (ArrayList<Game> round : rounds){
            for (Game game: round){
                game.getWhite().addRound(game);
                game.getBlack().addRound(game);
            }
            round.sort(new PairingComparator());
        }


        setPlayers(players);
        setRounds(rounds);

        setPairingComparator(new PairingComparator(playersObs));
        setResultsComparator(new ResultsComparator(getTiebreak()));

    }


    public Tournament(
    ) {
        this(
                "", new Date(),new Date(), "", (short) 0, (short) 0, (byte) 0, (byte) 0, Type.STANDARD, TournamentSystem.SWISS, (byte) 0,
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
        playersObs.addListener((ListChangeListener<? super Player>) change -> {
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

        roundsObs.addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
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

            for (Player player: getPlayersObs()){
                player.getRounds().clear();
            }

            for (ArrayList<Game> round: getRoundsObs()){
                for(Game game: round){
                    game.getWhite().addRound(game);
                    game.getBlack().addRound(game);
                }
            }

        });

        setPairingComparator(new PairingComparator(playersObs));
        setResultsComparator(new ResultsComparator(getTiebreak()));
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
                "players: " + players + "\n";
    }

    public ResultsComparator getResultsComparator() {
        return resultsComparator;
    }

    public void setResultsComparator(ResultsComparator resultsComparator) {
        this.resultsComparator = resultsComparator;
    }



    public void addPair(Game game) {
        addPair(game, rounds.size() - 1);
    }

    public void addPair(Game game, int roundNo) {
        rounds.get(roundNo).add(game);
    }

    public void addPair(UUID white, UUID black) {
        addPair(white, black, rounds.size() - 1);
    }

    public void addPair(UUID white, UUID black, int roundNo) {
        Player whitePlayer = players.get(white);
        Player blackPlayer = players.get(black);
        Game game = new Game(whitePlayer, blackPlayer);
        rounds.get(roundNo).add(game);
    }

    public ArrayList<Game> getRound(int roundNo) {
        return rounds.get(roundNo);
    }

    public void setGameResult(String white, String black, Result whiteResult, Result blackResult) {
        setGameResult(white, black, whiteResult, blackResult, false);
    }

    public void setGameResult(String white, String black, Result whiteResult, Result blackResult, boolean forfeit) {
        for (ArrayList<Game> round : rounds) {
            for (Game game : round) {
                if (
                        Objects.equals(game.getWhite().getName(), white)
                                && Objects.equals(game.getBlack().getName(), black)
                ) {
                    game.setWhiteResult(whiteResult);
                    game.setBlackResult(blackResult);
                    game.setForfeit(forfeit);
                }
            }
        }
    }

    public void setGameResult(int roundNo, int boardNo, Result whiteResult, Result blackResult) {
        setGameResult(roundNo, boardNo, whiteResult, blackResult, false);
    }

    public void setGameResult(int roundNo, int boardNo, Result whiteResult, Result blackResult, boolean forfeit) {
        Game game = getRounds().get(roundNo).get(boardNo);
        game.setWhiteResult(whiteResult);
        game.setBlackResult(blackResult);
        game.setForfeit(forfeit);
    }

    public void newRound() {
        rounds.add(new ArrayList<>());
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

    public void setTiebreak(Tiebreak tiebreak) {
        this.tiebreak = tiebreak;
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

    public void setPlayersObs(ObservableList<Player> playersObs) {
        this.playersObs = playersObs;
    }

    public PairingComparator getPairingComparator() {
        return pairingComparator;
    }

    public void setPairingComparator(PairingComparator pairingComparator) {
        this.pairingComparator = pairingComparator;
    }

    public ObservableList<ArrayList<Game>> getRoundsObs() {
        return roundsObs;
    }

    public void setRoundsObs(ObservableList<ArrayList<Game>> roundsObs) {
        this.roundsObs = roundsObs;
    }

    public enum Type implements Serializable {
        STANDARD,
        RAPID,
        BLITZ,
        OTHER
    }

    public enum TournamentSystem implements Serializable {
        SWISS,
        ROUND_ROBIN,
        CUP,


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
        }

        public Float getDrawPoints() {
            return drawPoints;
        }

        public void setDrawPoints(Float drawPoints) {
            this.drawPoints = drawPoints;
        }

        public Float getLosePoints() {
            return losePoints;
        }

        public void setLosePoints(Float losePoints) {
            this.losePoints = losePoints;
        }

        public Float getForfeitWinPoints() {
            return forfeitWinPoints;
        }

        public void setForfeitWinPoints(Float forfeitWinPoints) {
            this.forfeitWinPoints = forfeitWinPoints;
        }

        public Float getForfeitLosePoints() {
            return forfeitLosePoints;
        }

        public void setForfeitLosePoints(Float forfeitLosePoints) {
            this.forfeitLosePoints = forfeitLosePoints;
        }

        public Float getByePoints() {
            return byePoints;
        }

        public void setByePoints(Float byePoints) {
            this.byePoints = byePoints;
        }

        public enum TbMethod implements Serializable {
            POINTS,
            PLAYOFF,
            DUEL,
            WONS,
            GAMES_WITH_BLACK,
            WONS_WITH_BLACK,
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
            KOYA

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
            if (PZSzach47) {
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