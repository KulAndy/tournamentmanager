package com.example.tournamentmanager.model;

import com.example.tournamentmanager.calculation.FIDECalculation;
import com.example.tournamentmanager.calculation.PZSzachCalculation;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.example.tournamentmanager.calculation.FIDECalculation.FIDE_FLOOR;
import static com.example.tournamentmanager.calculation.PZSzachCalculation.PZSZACH_FLOOR;
import static com.example.tournamentmanager.calculation.PZSzachCalculation.getNorm;

@XmlRootElement(name = "player")
public class Player implements Serializable, Cloneable {
    private static final Short[] phonePrefixesList = new Short[]{1, 20, 211, 212, 213, 216, 218, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 27, 290, 291, 297, 298, 299, 30, 31, 32, 33, 34, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 36, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 385, 386, 387, 389, 39, 40, 41, 420, 421, 423, 43, 44, 45, 46, 47, 48, 49, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 51, 52, 53, 54, 55, 56, 57, 58, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 60, 61, 62, 63, 64, 65, 66, 670, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683, 685, 686, 687, 688, 689, 690, 691, 692, 800, 808, 81, 82, 84, 850, 852, 853, 855, 856, 86, 870, 880, 881, 882, 883, 886, 90, 91, 92, 93, 94, 95, 960, 961, 962, 963, 964, 965, 966, 967, 968, 970, 971, 972, 973, 974, 975, 976, 977, 979, 98, 992, 993, 994, 995, 996, 997, 998};
    private static Float winPoints = 1.0F;
    private static Float drawPoints = 0.5F;
    private static Float losePoints = 0.0F;
    private static Float forfeitWinPoints = 1.0F;
    private static Float forfeitLosePoints = 0F;
    private static Float byePoints = 1.0F;
    private static Float halfByePoints = 0.5F;
    private transient final FloatProperty tb1 = new SimpleFloatProperty(0);
    private transient final FloatProperty tb2 = new SimpleFloatProperty(0);
    private transient final FloatProperty tb3 = new SimpleFloatProperty(0);
    private transient final FloatProperty tb4 = new SimpleFloatProperty(0);
    private transient final FloatProperty tb5 = new SimpleFloatProperty(0);
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
    private int randomValue;

    public Player() {
        this("");
    }

    public Player(String name) {
        this(
                Federation.FIDE, "", name, Title.bk, PZSZACH_FLOOR, (int) FIDE_FLOOR,
                "", null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Integer fiderating, Integer localRating) {
        this(
                Federation.FIDE, "", name, Title.bk, localRating, fiderating,
                "", null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Title title) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, null), (int) FIDE_FLOOR,
                "", null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Title title, Sex sex) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, sex), (int) FIDE_FLOOR,
                "", null, sex, null, null, null, null, null, null
        );
    }

    public Player(String name, Integer fiderating, Title title) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, null), fiderating,
                "", null, null, null, null, null, null, null, null
        );
    }

    public Player(String name, Integer fiderating, Title title, Sex sex) {
        this(
                Federation.FIDE, "", name, title, PZSzachCalculation.getTitleValue(title, sex), fiderating,
                "", null, sex, null, null, null, null, null, null
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

    public static Float getWinPoints() {
        return winPoints;
    }

    public static void setWinPoints(Float winPoints) {
        Player.winPoints = winPoints;
    }

    public static Float getDrawPoints() {
        return drawPoints;
    }

    public static void setDrawPoints(Float drawPoints) {
        Player.drawPoints = drawPoints;
    }

    public static Float getLosePoints() {
        return losePoints;
    }

    public static void setLosePoints(Float losePoints) {
        Player.losePoints = losePoints;
    }

    public static Float getForfeitWinPoints() {
        return forfeitWinPoints;
    }

    public static void setForfeitWinPoints(Float forfeitWinPoints) {
        Player.forfeitWinPoints = forfeitWinPoints;
    }

    public static Float getForfeitLosePoints() {
        return forfeitLosePoints;
    }

    public static void setForfeitLosePoints(Float forfeitLosePoints) {
        Player.forfeitLosePoints = forfeitLosePoints;
    }

    public static Float getByePoints() {
        return byePoints;
    }

    public static void setByePoints(Float byePoints) {
        Player.byePoints = byePoints;
    }

    public static Float getHalfByePoints() {
        return halfByePoints;
    }

    public static void setHalfByePoints(Float halfByePoints) {
        Player.halfByePoints = halfByePoints;
    }

    public static Short[] getPhonePrefixesList() {
        return phonePrefixesList;
    }

    public Float getTb1() {
        return tb1.get();
    }

    public void setTb1(float tb1) {
        this.tb1.set(tb1);
    }

    public FloatProperty tb1Property() {
        return tb1;
    }

    public Float getTb2() {
        return tb2.get();
    }

    public void setTb2(float tb2) {
        this.tb2.set(tb2);
    }

    public FloatProperty tb2Property() {
        return tb2;
    }

    public Float getTb3() {
        return tb3.get();
    }

    public void setTb3(float tb3) {
        this.tb3.set(tb3);
    }

    public FloatProperty tb3Property() {
        return tb3;
    }

    public Float getTb4() {
        return tb4.get();
    }

    public void setTb4(float tb4) {
        this.tb4.set(tb4);
    }

    public FloatProperty tb4Property() {
        return tb4;
    }

    public Float getTb5() {
        return tb5.get();
    }

    public void setTb5(float tb5) {
        this.tb5.set(tb5);
    }

    public FloatProperty tb5Property() {
        return tb5;
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
        if (!getRounds().isEmpty()) {
            for (int i = getRounds().size() - 1; i >= 0; i--) {
                Game game = getRound(i);
                if (!game.isForfeit()) {
                    return getRoundColor(game);
                }
            }
        }
        return null;
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
        if (getPZSzachPoints() < getPZSzachRounds().size() / 3.0f) {
            return null;
        }
        Title norm = getNorm(getRatingPerformancePZSzach(), getPlayedGamedNumber(), getSex());
        if (norm != null && PZSzachCalculation.getTitleValue(norm, getSex()) > PZSzachCalculation.getTitleValue(getTitle(), getSex())) {
            return norm;
        }
        return null;
    }

    public Title getFideNorm(int minGames, boolean twoFeds) {
        return FIDECalculation.getNorm(this, minGames, twoFeds);
    }

    public float getFideChange() {
        float chg = 0.0F;
        if (getFideRating() == FIDE_FLOOR) {
            return FIDECalculation.getInitRating(getFideOpponents(), getFidePoints());
        } else {
            for (Game game : getFideRounds()) {
                if (game.getWhite() == this) {
                    chg += game.getPointsForWhite() - FIDECalculation.getExpectedResult(this.getFideRating(), game.getBlack().getFideRating())[0];
                } else {
                    chg += game.getPointsForBlack() - FIDECalculation.getExpectedResult(this.getFideRating(), game.getWhite().getFideRating())[0];
                }
            }
        }
        return chg;
    }

    public float getAverageFideRating() {
        return FIDECalculation.getAverageRating(getFideOpponents());
    }

    public int getAverageRatingPZSzach() {
        return PZSzachCalculation.getAverageRating(this);
    }

    public int getWinsNumber() {
        int wons = 0;
        for (Game game : getRounds()) {
            if (!game.isForfeit() && getRoundResult(game) == Result.WIN) {
                wons++;
            }
        }

        return wons;
    }

    public int getLosesNumber() {
        int loses = 0;
        for (Game game : getRounds()) {
            if (!game.isForfeit() && getRoundResult(game) == Result.LOSE) {
                loses++;
            }
        }
        return loses;
    }

    public int getDrawsNumber() {
        int draws = 0;
        for (Game game : getRounds()) {
            if (!game.isForfeit() && getRoundResult(game) == Result.DRAW) {
                draws++;
            }
        }

        return draws;
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
                if (opponent.getFideRating() != null && opponent.getFideRating() > FIDE_FLOOR) {
                    fideRounds.add(round);
                }
            }
        }
        return fideRounds;
    }

    public ArrayList<Game> getPZSzachRounds() {
        ArrayList<Game> rounds = getRounds();
        ArrayList<Game> pzszachRounds = new ArrayList<>();
        for (Game round : rounds) {
            if (!round.isForfeit()) {
                Player opponent = getOpponent(round);
                if (opponent.getLocalRating() != null && opponent.getLocalRating() >= PZSZACH_FLOOR) {
                    pzszachRounds.add(round);
                }
            }
        }
        return pzszachRounds;
    }

    public float getPointInRound(int n) {
        float points = 0;
        for (int i = 0; i < n && i < getRounds().size(); i++) {
            Game round = getRounds().get(i);
            Color color = getRoundColor(round);
            if (color != null) {
                switch (color) {
                    case WHITE -> {
                        if (!Float.isNaN(round.getPointsForWhite())) {
                            points += round.getPointsForWhite();
                        }
                    }
                    case BLACK -> {
                        if (!Float.isNaN(round.getPointsForBlack())) {
                            points += round.getPointsForBlack();
                        }
                    }
                }
            }
        }
        return points;
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

    public ArrayList<Player> getFideOpponents() {
        ArrayList<Player> opponents = new ArrayList<>();
        for (Game round : getRounds()) {
            Player opponent = getOpponent(round);
            if (!round.isForfeit() && opponent.getFideRating() != null && opponent.getFideRating() > FIDE_FLOOR) {
                opponents.add(opponent);
            }
        }

        return opponents;
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
        setLocalRating(getPZSzachRating());
    }

    public Integer getLocalRating() {
        return localRating;
    }

    public void setLocalRating(Integer localRating) {
        if (localRating != null && localRating > 0) {
            this.localRating = localRating;
        } else {
            this.localRating = PZSZACH_FLOOR;
        }
    }

    public Integer getPZSzachRating() {
        if (title == Title.bk && getFederation() != Federation.POL) {
            if (getSex() == Sex.FEMALE) {
                if (getFideRating() > 2450) {
                    return 2600;
                } else if (getFideRating() > 2400) {
                    return 2450;
                } else if (getFideRating() > 2300) {
                    return 2400;
                } else if (getFideRating() > 2250) {
                    return 2300;
                } else if (getFideRating() > 2200) {
                    return 2250;
                } else if (getFideRating() > 2100) {
                    return 2200;
                } else if (getFideRating() > 2000) {
                    return 2100;
                } else if (getFideRating() > 1900) {
                    return 2000;
                } else if (getFideRating() > 1800) {
                    return 1900;
                } else if (getFideRating() > 1700) {
                    return 1800;
                } else if (getFideRating() > 1600) {
                    return 1700;
                } else if (getFideRating() > 1400) {
                    return 1600;
                } else if (getFideRating() > 1250) {
                    return 1400;
                } else if (getFideRating() > 1100) {
                    return 1250;
                } else if (getFideRating() > 1000) {
                    return 1100;
                }
            } else {
                if (getFideRating() > 2450) {
                    return 2600;
                } else if (getFideRating() > 2400) {
                    return 2450;
                } else if (getFideRating() > 2300) {
                    return 2400;
                } else if (getFideRating() > 2200) {
                    return 2300;
                } else if (getFideRating() > 2100) {
                    return 2200;
                } else if (getFideRating() > 2000) {
                    return 2100;
                } else if (getFideRating() > 1900) {
                    return 2000;
                } else if (getFideRating() > 1800) {
                    return 1900;
                } else if (getFideRating() > 1600) {
                    return 1800;
                } else if (getFideRating() > 1400) {
                    return 1600;
                } else if (getFideRating() > 1200) {
                    return 1400;
                } else if (getFideRating() > 1000) {
                    return 1200;
                }
            }
        }

        return PZSzachCalculation.getTitleValue(getTitle(), getSex());
    }

    public Integer getFideRating() {
        if (fideRating == null) {
            return (int) FIDE_FLOOR;
        }
        return Integer.max(fideRating, FIDE_FLOOR);
    }

    public void setFideRating(Integer fideRating) {
        if (fideRating != null && fideRating > 0) {
            this.fideRating = fideRating;
        } else {
            this.fideRating = (int) FIDE_FLOOR;
        }
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = Objects.requireNonNullElse(club, "");
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

    public int getRatingPerformancePZSzach() {
        return PZSzachCalculation.getRatingPerformance(this);
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

    @Override
    public Player clone() {
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void generateRandomValue() {
        this.randomValue = new Random().nextInt();
    }

    public int getRandomValue() {
        return randomValue;
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