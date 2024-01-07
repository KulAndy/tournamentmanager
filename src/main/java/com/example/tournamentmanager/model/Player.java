package com.example.tournamentmanager.model;

import com.example.tournamentmanager.calculation.FIDECalculation;
import com.example.tournamentmanager.calculation.PZSzachCalculation;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.example.tournamentmanager.calculation.PZSzachCalculation.getNorm;

@XmlRootElement(name = "player")
public class Player implements Serializable {
    private static final Short[] phonePrefixesList = new Short[]{1, 20, 211, 212, 213, 216, 218, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 27, 290, 291, 297, 298, 299, 30, 31, 32, 33, 34, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 36, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 385, 386, 387, 389, 39, 40, 41, 420, 421, 423, 43, 44, 45, 46, 47, 48, 49, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 51, 52, 53, 54, 55, 56, 57, 58, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 60, 61, 62, 63, 64, 65, 66, 670, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683, 685, 686, 687, 688, 689, 690, 691, 692, 800, 808, 81, 82, 84, 850, 852, 853, 855, 856, 86, 870, 880, 881, 882, 883, 886, 90, 91, 92, 93, 94, 95, 960, 961, 962, 963, 964, 965, 966, 967, 968, 970, 971, 972, 973, 974, 975, 976, 977, 979, 98, 992, 993, 994, 995, 996, 997, 998};
    private static Float pointsForWin = 1.0F;
    private static Float pointsForDraw = 0.5F;
    private static Float pointsForLose = 0.0F;
    private static Float pointsForForfeitWin = 1.0F;
    private static Float pointsForForfeitLose = 0F;
    private static Float pointsForBye = 1.0F;
    private static Float pointsForHalfBye = 0.5F;
    private Federation federation;
    private String state;
    private String name;
    private Title title = Title.bk;
    private Integer localRating;
    private Integer fideRating;
    private String club;
    private Sex sex;
    private String eMail;
    private Integer localId;
    private Integer fideId;
    private String remarks;
    private Short phonePrefix;
    private Integer phoneNumber;
    private ArrayList<Game> rounds;
    private ObjectId playerid;
    private int YearOfBirth;
    private byte MonthOfBirth;
    private byte DayOfBirth;


    public Player() {
        this("");
    }


    public Player(String name) {
        this(
                Federation.FIDE, "", name, Title.bk, 1000, 1000,
                null, null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Integer fiderating, Integer localRating) {
        this(
                Federation.FIDE, "", name, Title.bk, localRating, fiderating,
                null, null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Title title) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, null), 1000,
                null, null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Title title, Sex sex) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, sex), 1000,
                null, null, sex, null, null, null, null, null, null
        );
    }


    public Player(String name, Integer fiderating, Title title) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, null), fiderating,
                null, null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Integer fiderating, Title title, Sex sex) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, sex), fiderating,
                null, null, sex, null, null, null, null, null, null
        );
    }

    public Player(
            Federation federation, String state, String name, Title title, Integer localRating, Integer fideRating,
            String club, String dateOfBirth, Sex sex, String eMail, Short phonePrefix, Integer phoneNumber, Integer localId, Integer fideId, String remarks

    ) {
        setFederation(federation);
        setState(state);
        setName(name);
        setTitle(title);
        setLocalRating(localRating);
        setFideRating(fideRating);
        setClub(club);
        setDateOfBirth(dateOfBirth);
        setSex(sex);
        setEMail(eMail);
        setPhonePrefix(phonePrefix);
        setPhoneNumber(phoneNumber);
        setLocalId(localId);
        setFideId(fideId);
        setRemarks(remarks);
        setPlayerid(new ObjectId());
        setRounds(new ArrayList<>());
    }

    public Player(String name, Sex sex, Title title, Integer rating, Federation federation, int fideNo, short yearOfBirth, byte monthOfBirth, byte dayOfBirth) {
        this(
                federation, "", name, title, PZSzachCalculation.getTitleValue(title, sex), rating,
                null, yearOfBirth + "/" + (monthOfBirth < 10 ? "0" + monthOfBirth : monthOfBirth) + "/" + (dayOfBirth < 10 ? "0" + dayOfBirth : dayOfBirth), sex, null, null, null, null, fideNo, null
        );
    }

    public static Float getPointsForWin() {
        return pointsForWin;
    }

    public static void setPointsForWin(Float pointsForWin) {
        Player.pointsForWin = pointsForWin;
    }

    public static Float getPointsForDraw() {
        return pointsForDraw;
    }

    public static void setPointsForDraw(Float pointsForDraw) {
        Player.pointsForDraw = pointsForDraw;
    }

    public static Float getPointsForLose() {
        return pointsForLose;
    }

    public static void setPointsForLose(Float pointsForLose) {
        Player.pointsForLose = pointsForLose;
    }

    public static Float getPointsForForfeitWin() {
        return pointsForForfeitWin;
    }

    public static void setPointsForForfeitWin(Float pointsForForfeitWin) {
        Player.pointsForForfeitWin = pointsForForfeitWin;
    }

    public static Float getPointsForForfeitLose() {
        return pointsForForfeitLose;
    }

    public static void setPointsForForfeitLose(Float pointsForForfeitLose) {
        Player.pointsForForfeitLose = pointsForForfeitLose;
    }

    public static Float getPointsForBye() {
        return pointsForBye;
    }

    public static void setPointsForBye(Float pointsForBye) {
        Player.pointsForBye = pointsForBye;
    }

    public static Float getPointsForHalfBye() {
        return pointsForHalfBye;
    }

    public static void setPointsForHalfBye(Float pointsForHalfBye) {
        Player.pointsForHalfBye = pointsForHalfBye;
    }

    public static Short[] getPhonePrefixesList() {
        return phonePrefixesList;
    }

    public int getColorPreference() {
        int preference = 0;
        Color color = getLastColor();
        if (color != null) {
            for (int i = getRounds().size() - 1; i >= 0; i--) {
                Game game = getRound(i);
                if (!game.isForfeit()) {
                    if (getRoundColor(game) == color) {
                        if (color == Color.WHITE) {
                            preference++;
                        } else {
                            preference--;
                        }
                    } else {
                        return preference;
                    }
                }
            }
        }
        return preference;
    }

    public Color getLastColor() {
        if (getRounds().size() != 0) {
            for (int i = getRounds().size() - 1; i >= 0; i--) {
                Game game = getRound(i);
                if (!game.isForfeit()) {
                    return getRoundColor(game);
                }
            }
        }
        return null;
    }

    public Number getTiebreak(Tournament.Tiebreak.TbMethod tiebreak) {
        switch (tiebreak) {
            case KOYA -> {
                return getKoya();
            }
            case WINS -> {
                return getWonsNumber();
            }
            case POINTS -> {
                return getPoints();
            }
            case BUCHOLZ -> {
                return getBucholz();
            }
            case PROGRESS -> {
                return getProgress();
            }
            case BUCHOLZ_CUT1 -> {
                return getBucholzCut1();
            }
            case SONNEN_BERGER -> {
                return getBerger();
            }
            case WINS_WITH_BLACK -> {
                return getWonsWithBlackNumber();
            }
            case GAMES_WITH_BLACK -> {
                return getGamesPlayedWithBlack();
            }
            case RATING_PERFORMENCE_FIDE -> {
                return getRatingPerformanceFide();
            }
            case AVERAGE_OPPONENTS_RATING -> {
                return getAverageFideRating();
            }
            case RATING_PERFORMENCE_PZSZACH -> {
                return getRatingPerformancePZSzach();
            }
            case AVERAGE_OPPONENTS_LOCAL_RATING -> {
                return getAverageRatingPZSzach();
            }
            default -> {
                return 0;
            }
        }
    }

    public float getRatingPerformanceFide() {
        return FIDECalculation.getRatingPerformance(getFideOpponents(), getPoints());
    }

    @Override
    public String toString() {
        return "playerID " + getPlayerid() +
                ", name: " + getName() +
                ", FED: " + getFederation() +
                ", state: " + getState() +
                ", title: " + getTitle() +
                ", local rating: " + getLocalRating() +
                ", fide rating: " + getFideRating() +
                ", club: " + getClub() +
                ", birth: " + getDateOfBirth() +
                ", sex: " + getSex() +
                ", email: " + getEMail() +
                ", local ID: " + getLocalId() +
                ", fide ID: " + getFideId() +
                ", phone: +" + getPhonePrefix() + " " + getPhoneNumber() +
                ", remarks: " + getRemarks()
                ;
    }

    public void addRound(Game game) {
        rounds.add(game);
    }

    public Title getPlayerNorm() {
        Title norm = getNorm(getRatingPerformancePZSzach(), getPlayedGamedNumber(), getSex());
        if (norm != null && PZSzachCalculation.getTitleValue(norm, getSex()) > PZSzachCalculation.getTitleValue(getTitle(), getSex())) {
            return norm;
        }
        return null;
    }

    public float getKoya() {
        float koya = 0;
        for (Game round : getRounds()) {
            Player opponent = getOpponent(round);
            if (opponent.getPoints() >= getRounds().size() / 2.0) {
                Result result = getRoundResult(round);
                if (result == Result.WIN) {
                    koya++;
                } else if (result == Result.DRAW) {
                    koya += 0.5F;
                }

            }
        }

        return koya;
    }

    public int getRatingPerformancePZSzach() {
        return PZSzachCalculation.getRatingPerformance(this);
    }

    public int getRatingDelta() {
        return PZSzachCalculation.getRatingDelta(this);
    }

    public int getAverageRatingPZSzach() {
        return PZSzachCalculation.getAverageRating(this);
    }

    public int getAverageFideRating() {
        return FIDECalculation.getAverageRating(getFideOpponents());
    }

    public float getFideChange() {
        float chg = 0.0F;
        for (Game game : getFideRounds()) {
            if (game.getWhite() == this) {
                chg += FIDECalculation.getExpectedResult(this.getFideRating(), game.getBlack().getFideRating())[0];
            } else {
                chg += FIDECalculation.getExpectedResult(this.getFideRating(), game.getWhite().getFideRating())[0];
            }
        }

        return chg;
    }

    public float getProgress() {
        float progress = 0;
        float points = 0;
        for (Game round : getRounds()) {
            Result result = getRoundResult(round);
            if (result != null) {
                switch (result) {
                    case WIN -> points++;
                    case DRAW -> points += 0.5F;
                }
            }
            progress += points;
        }

        return progress;
    }

    public float getBerger() {
        float berger = 0;
        for (Game round : getRounds()) {
            Result result = getRoundResult(round);
            Color color = getRoundColor(round);
            if (result != null) {
                switch (result) {
                    case WIN -> {
                        if (color == Color.WHITE) {
                            berger += round.getBlack().getPoints();
                        } else if (color == Color.BLACK) {
                            berger += round.getWhite().getPoints();
                        }
                    }
                    case DRAW -> {
                        if (color == Color.WHITE) {
                            berger += round.getBlack().getPoints() / 2;
                        } else if (color == Color.BLACK) {
                            berger += round.getWhite().getPoints() / 2;
                        }
                    }
                }
            }
        }

        return berger;
    }

    public float getBucholzCut1() {
        float bucholz = getBucholz();
        float minPoints = Float.MAX_VALUE;
        for (Player player : getOpponents()) {
            minPoints = Float.min(player.getPoints(), minPoints);
        }

        if (minPoints == Float.MAX_VALUE) {
            return bucholz;
        } else {
            return bucholz - minPoints;
        }
    }

    public float getBucholz() {
        float bucholz = 0;
        String[] reservedNames = {"bye", "haslfbye", "unpaired"};
        for (Game round : getRounds()) {
            Player opponent = getOpponent(round);
            Float addition;
            if (Arrays.asList(reservedNames).contains(opponent.getName())) {
                addition = (float) (0.5 * (getRounds().size() - getRounds().indexOf(round) - 1));
            } else if (
                    opponent.getRounds().size() == opponent.getPlayedGamedNumber()
            ) {
                addition = opponent.getPoints();
            } else {
                addition = (float) (opponent.getFidePoints() + 0.5 * (opponent.getRounds().size() - opponent.getPlayedGamedNumber()));
            }
            if (!addition.isNaN()) {
                bucholz += addition;
            }
        }
        return bucholz;
    }

    public int getWonsWithBlackNumber() {
        int wons = 0;
        for (Game round : getRounds()) {
            if (isRoundWon(round) && getRoundColor(round) == Color.BLACK) {
                wons++;
            }
        }
        return wons;
    }

    public int getWonsNumber() {
        int wons = 0;
        for (int i = 0; i < getRounds().size(); i++) {
            if (isRoundWon(i)) {
                wons++;
            }
        }

        return wons;
    }

    public int getGamesPlayedWithBlack() {
        int blacks = 0;
        for (Game round : getRounds()) {
            if (
                    round.getBlack().getPlayerid() == this.getPlayerid()
                            && !round.isForfeit()
            ) {
                blacks++;
            }
        }
        return blacks;
    }

    public Float getFidePoints() {
        float points = 0;
        for (Game game : getFideRounds()) {
            points += getPointInGame(game);
        }
        return points;
    }

    private float getPointInGame(Game game) {
        if (game.getWhite() == this) {
            return game.getPointsForWhite();
        } else {
            return game.getPointsForBlack();
        }
    }

    public Float getPZSzachPoints() {
        float points = 0;
        for (Game game : getPZSzachRounds()) {
            points += getPointInGame(game);
        }
        return points;
    }

    public Float getPoints() {
        float points = 0f;
        for (Game round : getRounds()) {
            float roundPoints = getRoundPoints(round);
            if (!Float.isNaN(roundPoints)) {
                points += roundPoints;
            }
        }
        return points;
    }

    public int getPlayedGamedNumber() {
        int played = 0;
        for (Game round : getRounds()) {
            if (!round.isForfeit()) {
                played++;
            }
        }
        return played;
    }

    public int getFidePlayedGamedNumber() {
        return getFideRounds().size();
    }

    public ArrayList<Game> getFideRounds() {
        ArrayList<Game> rounds = getRounds();
        ArrayList<Game> fideRounds = new ArrayList<>();
        for (Game round : rounds) {
            if (!round.isForfeit()) {
                Player opponent = getOpponent(round);
                if (opponent.getFideRating() != null && opponent.getFideRating() > 1000) {
                    fideRounds.add(round);
                }
            }
        }
        return fideRounds;
    }

    public ArrayList<Game> getPZSzachRounds() {
        ArrayList<Game> rounds = getRounds();
        ArrayList<Game> fideRounds = new ArrayList<>();
        for (Game round : rounds) {
            if (!round.isForfeit()) {
                Player opponent = getOpponent(round);
                if (opponent.getLocalRating() != null && opponent.getLocalRating() > 1000) {
                    fideRounds.add(round);
                }
            }
        }
        return fideRounds;
    }

    public float getPointInRound(int n) {
        float points = 0;
        for (int i = 0; i < n && i < getRounds().size(); i++) {
            Game round = getRounds().get(i);
            Result result = getRoundResult(round);
            if (result == null) {
                return 0;
            }
            switch (result) {
                case WIN -> points++;
                case DRAW -> points += 0.5F;
            }
        }
        return points;
    }

    public float getRoundPoints(int n) {
        Game round = getRound(n);
        Color color = getRoundColor(n);
        if (color == Color.WHITE) {
            return round.getPointsForWhite();
        } else {
            return round.getPointsForBlack();
        }
    }

    public float getRoundPoints(Game round) {
        Color color = getRoundColor(round);
        if (color == Color.WHITE) {
            return round.getPointsForWhite();
        } else {
            return round.getPointsForBlack();
        }
    }

    public Player getOpponent(Game round) {
        if (round.getWhite() == this) {
            return round.getBlack();
        } else if (round.getBlack() == this) {
            return round.getWhite();
        } else {
            return null;
        }
    }

    public ArrayList<Player> getOpponents() {
        ArrayList<Player> opponents = new ArrayList<>();
        for (Game round : getRounds()) {
            if (!round.isForfeit()) {
                opponents.add(getOpponent(round));
            }
        }

        return opponents;
    }

    public ArrayList<Player> getFideOpponents() {
        ArrayList<Player> opponents = new ArrayList<>();
        for (Game round : getRounds()) {
            Player opponent = getOpponent(round);
            if (!round.isForfeit() && opponent.getFideRating() != null && opponent.getFideRating() > 1000) {
                opponents.add(opponent);
            }
        }

        return opponents;
    }

    public boolean isRoundWon(Game round) {
        Color color = getRoundColor(round);
        if (color == Color.WHITE && round.getPointsForWhite() == 1.0) {
            return true;
        } else return color == Color.BLACK && round.getPointsForBlack() == 1.0;
    }

    public boolean isRoundWon(int n) {
        Color color = getRoundColor(n);
        Game round = getRound(n);
        if (color == Color.WHITE && round.getPointsForWhite() == 1.0) {
            return true;
        } else return color == Color.BLACK && round.getPointsForBlack() == 1.0;
    }

    public boolean hasRoundBeenPlayed(int n) {
        Game round = getRound(n);
        return !round.isForfeit();
    }

    public Result getRoundResult(int n) {
        Game round = getRound(n);
        Color color = getRoundColor(n);
        if (color == Color.WHITE) {
            if (round.getPointsForWhite() == 1) {
                return Result.WIN;
            } else if (round.getPointsForWhite() == 0.5) {
                return Result.DRAW;
            }
        } else {
            if (round.getPointsForBlack() == 1) {
                return Result.WIN;
            } else if (round.getPointsForBlack() == 0.5) {
                return Result.DRAW;
            }
        }
        return Result.LOSE;
    }

    public Result getRoundResult(Game round) {
        Color color;
        if (round.getWhite() == this) {
            color = Color.WHITE;
        } else {
            color = Color.BLACK;
        }
        if (color == Color.WHITE) {
            return round.getWhiteResult();
        } else {
            return round.getBlackResult();
        }
    }

    public Game getRound(int n) {
        return getRounds().get(n);
    }

    public Game getRound(Player player) {
        if (player == this) {
            return null;
        }
        for (Game game : getRounds()) {
            if (game.getWhite() == player || game.getBlack() == player) {
                return game;
            }
        }
        return null;
    }

    public Game getLastRound() {
        return getRound(getRounds().size() - 1);
    }

    public void updateRoundResults(Integer n, Result whiteResult, Result blackResult, Boolean forfeit) {
        Game round = getRound(n);
        round.setWhiteResult(whiteResult);
        round.setBlackResult(blackResult);
        round.setForfeit(forfeit);
    }

    public void updateLastRoundResult(Result whiteResult, Result blackResult, Boolean forfeit) {
        updateRoundResults(getRounds().size() - 1, whiteResult, blackResult, forfeit);
    }

    public Color getRoundColor(int n) {
        Game round = getRound(n);
        if (round.getWhite().getPlayerid() == this.getPlayerid()) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public Color getRoundColor(Game round) {
        if (round.getWhite() == this) {
            return Color.WHITE;
        } else if (round.getBlack() == this) {
            return Color.BLACK;
        }
        return null;
    }

    public Federation getFederation() {
        return federation;
    }

    public void setFederation(Federation federation) {
        if (federation != null) {
            this.federation = federation;
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state != null) {
            this.state = state;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        if (title != null) {
            this.title = title;
        }
    }

    public Integer getLocalRating() {
        return localRating;
    }

    public void setLocalRating(Integer localRating) {
        if (localRating != null && localRating > 0) {
            this.localRating = localRating;
        } else {
            this.localRating = 1000;
        }
    }

    public Integer getFideRating() {
        return fideRating;
    }

    public void setFideRating(Integer fideRating) {
        if (fideRating != null && fideRating > 0) {
            this.fideRating = fideRating;
        } else {
            this.fideRating = 1000;
        }
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        if (club != null) {
            this.club = club;
        }
    }

    public String getDateOfBirth() {
        return YearOfBirth + "/" +
                (MonthOfBirth < 10 ? "0" + MonthOfBirth : MonthOfBirth)
                + "/" +
                (DayOfBirth < 10 ? "0" + DayOfBirth : DayOfBirth);
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (dateOfBirth != null) {
            try {
                setYearOfBirth(Integer.parseInt(dateOfBirth.substring(0, 4)));
            } catch (Exception ignored) {
            }
            try {
                setMonthOfBirth(Byte.parseByte(dateOfBirth.substring(5, 7)));
            } catch (Exception ignored) {
            }
            try {
                setDayOfBirth(Byte.parseByte(dateOfBirth.substring(8)));
            } catch (Exception ignored) {
            }
        }
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        if (sex != null) {
            this.sex = sex;
        }
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        if (eMail != null) {
            this.eMail = eMail;
        }
    }

    public Short getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(Short phonePrefix) {
        if (phonePrefix != null && phonePrefix > 0) {
            boolean exists = false;

            for (Short prefix : phonePrefixesList) {
                if (phonePrefix.equals(prefix)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                this.phonePrefix = phonePrefix;
            }
        }
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        if (phoneNumber != null && phoneNumber > 0) {
            this.phoneNumber = phoneNumber;
        }
    }

    public Integer getLocalId() {
        return localId;
    }

    public void setLocalId(Integer localId) {
        if (localId == null || localId > 0) {
            this.localId = localId;
        }
    }

    public Integer getFideId() {
        return fideId;
    }

    public void setFideId(Integer fideId) {
        if (fideId == null || fideId > 0) {
            this.fideId = fideId;
        }
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        if (remarks != null) {
            this.remarks = remarks;
        }
    }

    @XmlElementWrapper(name = "rounds")
    @XmlAnyElement(lax = true)

    public ArrayList<Game> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<Game> rounds) {
        this.rounds = rounds;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public ObjectId getPlayerid() {
        return playerid;
    }

    public void setPlayerid(ObjectId playerid) {
        this.playerid = playerid;
    }

    public void setPlayerid(int number) {
        StringBuilder hexString = new StringBuilder(Integer.toHexString(number));

        while (hexString.length() < 24) {
            hexString.insert(0, "0");
        }
        ObjectId uuid = new ObjectId(hexString.toString());
        setPlayerid(uuid);
    }

    public int getYearOfBirth() {
        return YearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        YearOfBirth = yearOfBirth;
    }

    public byte getMonthOfBirth() {
        return MonthOfBirth;
    }

    public void setMonthOfBirth(byte monthOfBirth) {
        MonthOfBirth = monthOfBirth;
    }

    public byte getDayOfBirth() {
        return DayOfBirth;
    }

    public void setDayOfBirth(byte dayOfBirth) {
        DayOfBirth = dayOfBirth;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Player other = (Player) obj;

        return Objects.equals(getPlayerid().toString(), other.getPlayerid().toString()) && Objects.equals(getName(), other.getName());
    }

    public enum Sex {
        MALE,
        FEMALE
    }

    public enum Color {
        WHITE,
        BLACK
    }
}