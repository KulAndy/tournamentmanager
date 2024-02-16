package com.example.tournamentmanager.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import static com.example.tournamentmanager.calculation.PZSzachCalculation.PZSZACH_FLOOR;

public class TrfTournament {
    private String name;
    private String city;
    private Federation federation;
    private Date startDate;
    private Date endDate;
    private Tournament.TournamentSystem system;
    private String chiefArbiter;
    private String deputyChiefArbiter;
    private ArrayList<Date> roundDates = new ArrayList<>();
    private String allottedTimes;
    private ArrayList<TrfPlayer> players = new ArrayList<>();

    private byte roundsNo = 0;

    public TrfTournament(String trfPath) {
        this(new File(trfPath));
    }

    public TrfTournament(File trfFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(trfFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String contentType = line.substring(0, 3);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                switch (contentType) {
                    case "012" -> setName(line.substring(3).trim());
                    case "022" -> setCity(line.substring(3).trim());
                    case "032" -> {
                        try {
                            setFederation(Federation.valueOf(line.substring(3).trim()));
                        } catch (IllegalArgumentException e) {
                            setFederation(Federation.FIDE);
                        }
                    }
                    case "042" -> {
                        try {
                            setStartDate(dateFormat.parse(line.substring(3).trim()));
                        } catch (ParseException e) {
                            try {
                                setStartDate(dateFormat2.parse(line.substring(3).trim()));
                            } catch (ParseException ignored) {
                            }
                        }
                    }
                    case "052" -> {
                        try {
                            setEndDate(dateFormat.parse(line.substring(3).trim()));
                        } catch (ParseException e) {
                            try {
                                setEndDate(dateFormat2.parse(line.substring(3).trim()));
                            } catch (ParseException ignored) {
                            }
                        }
                    }
                    case "092" -> {
                        if (line.substring(3).trim().toLowerCase().contains("robin")) {
                            setSystem(Tournament.TournamentSystem.ROUND_ROBIN);
                        } else {
                            setSystem(Tournament.TournamentSystem.SWISS);
                        }
                    }
                    case "102" -> setChiefArbiter(line.substring(3).trim());
                    case "112" -> setDeputyChiefArbiter(line.substring(3).trim());
                    case "122" -> setAllottedTimes(line.substring(3).trim());
                    case "132" -> {
                        String[] roundDatesArray = line.substring(3).trim().split(" +");
                        SimpleDateFormat roundDateFormatter1 = new SimpleDateFormat("yy/MM/dd");
                        SimpleDateFormat roundDateFormatter2 = new SimpleDateFormat("yy-MM-dd");
                        for (String roundDate : roundDatesArray) {
                            try {
                                getRoundDates().add(roundDateFormatter1.parse(roundDate));
                            } catch (ParseException e2) {
                                try {
                                    getRoundDates().add(roundDateFormatter2.parse(roundDate));
                                } catch (ParseException ignored) {
                                }
                            }
                        }
                        setRoundsNo((byte) roundDatesArray.length);
                    }
                    case "001" -> {
                        int startNo = Integer.parseInt(line.substring(4, 8).trim());
                        Player.Sex playerSex = line.charAt(9) == 'w' ? Player.Sex.FEMALE : Player.Sex.MALE;
                        Title playerTitle = Title.getTitle(line.substring(10, 13).trim());
                        String playerName = line.substring(14, 47).trim();
                        int playerRating;
                        try {
                            playerRating = Integer.parseInt(line.substring(48, 52).trim());
                        } catch (NumberFormatException e) {
                            playerRating = PZSZACH_FLOOR;
                        }
                        Federation playerFederation;
                        try {
                            playerFederation = Federation.valueOf(line.substring(53, 56));
                        } catch (IllegalArgumentException e) {
                            playerFederation = Federation.FIDE;
                        }
                        int playerFideId;
                        try {
                            playerFideId = Integer.parseInt(line.substring(57, 68).trim());
                        } catch (NumberFormatException e) {
                            playerFideId = 0;
                        }
                        String[] birthElems = new String[0];
                        try {
                            birthElems = line.substring(69, 79).split("/");
                        } catch (Exception e) {
                            try {
                                birthElems = line.substring(69, 79).split("-");
                            } catch (Exception ignored) {
                            }
                        }
                        short playerYearOfBirth = 0;
                        byte playerMonthOfBirth = 0;
                        byte playerDayOfBirth = 0;
                        if (birthElems.length == 3) {
                            try {
                                playerYearOfBirth = Short.parseShort(birthElems[0].trim());
                                playerMonthOfBirth = Byte.parseByte(birthElems[1].trim());
                                playerDayOfBirth = Byte.parseByte(birthElems[2].trim());
                            } catch (Exception ignored) {
                            }
                        }
                        float playerPoints = Optional.of(Float.parseFloat(line.substring(80, 84).replace(',', '.'))).orElse(0f);
                        ArrayList<TrfRound> playerRounds = new ArrayList<>();
                        StringBuilder roundsRaw = new StringBuilder(line.substring(91));
                        int fill = (10 - (roundsRaw.length() % 10)) % 10;
                        roundsRaw.append(" ".repeat(fill));
                        ArrayList<String> rawRoundsList = new ArrayList<>();
                        try {
                            int counter = 0;
                            while (true) {
                                String rawRound = roundsRaw.substring(counter * 10, counter++ * 10 + 9);
                                rawRoundsList.add(rawRound);
                            }
                        } catch (Exception ignored) {
                        }
                        for (String rawRound : rawRoundsList) {
                            int opponentId = 0;
                            try {
                                opponentId = Integer.parseInt(rawRound.substring(0, 4).trim());
                            } catch (NumberFormatException ignored) {
                            }
                            char color = rawRound.charAt(5);
                            char result = rawRound.charAt(7);
                            playerRounds.add(new TrfRound(opponentId, color, result));
                        }
                        getPlayers().add(new TrfPlayer(
                                getPlayers().size() + 1,
                                playerSex,
                                playerTitle,
                                playerName,
                                playerRating,
                                playerFederation,
                                playerFideId,
                                playerYearOfBirth,
                                playerMonthOfBirth,
                                playerDayOfBirth,
                                playerPoints,
                                playerRounds
                        ));
                    }
                }
            }

            if (getRoundsNo() == 0) {
                if (getRoundsNo() == 0) {
                    Optional<Integer> maxRounds = players.stream()
                            .map(player -> player.getRounds().size())
                            .max(Comparator.naturalOrder());
                    setRoundsNo((maxRounds.orElse(0)).byteValue());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Federation getFederation() {
        return federation;
    }

    public void setFederation(Federation federation) {
        this.federation = federation;
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

    public Tournament.TournamentSystem getSystem() {
        return system;
    }

    public void setSystem(Tournament.TournamentSystem system) {
        this.system = system;
    }

    public String getChiefArbiter() {
        return chiefArbiter;
    }

    public void setChiefArbiter(String chiefArbiter) {
        this.chiefArbiter = chiefArbiter;
    }

    public String getDeputyChiefArbiter() {
        return deputyChiefArbiter;
    }

    public void setDeputyChiefArbiter(String deputyChiefArbiter) {
        this.deputyChiefArbiter = deputyChiefArbiter;
    }

    public ArrayList<Date> getRoundDates() {
        return roundDates;
    }

    public void setRoundDates(ArrayList<Date> roundDates) {
        this.roundDates = roundDates;
    }

    public ArrayList<TrfPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<TrfPlayer> players) {
        this.players = players;
    }

    public String getAllottedTimes() {
        return allottedTimes;
    }

    public void setAllottedTimes(String allottedTimes) {
        this.allottedTimes = allottedTimes;
    }

    public byte getRoundsNo() {
        return roundsNo;
    }

    public void setRoundsNo(byte roundsNo) {
        this.roundsNo = roundsNo;
    }

    public static class TrfRound {

        private int opponentId;
        private char color;
        private char result;

        public TrfRound(int opponentId, char color, char result) {
            setOpponentId(opponentId);
            setColor(color);
            setResult(result == ' ' ? '\0' : result);
        }

        @Override
        public String toString() {
            return opponentId + " " + color + " " + result;
        }

        public int getOpponentId() {
            return opponentId;
        }

        public void setOpponentId(int opponentId) {
            this.opponentId = opponentId;
        }

        public char getColor() {
            return color;
        }

        public void setColor(char color) {
            this.color = color;
        }

        public char getResult() {
            return result;
        }

        public void setResult(char result) {
            this.result = result;
        }

    }

    public static class TrfPlayer {
        private int startRank;
        private Player.Sex sex;
        private Title title;
        private String name;
        private Integer rating;
        private Federation federation;
        private int fideNo;
        private short yearOfBirth;
        private byte monthOfBirth;
        private byte dayOfBirth;
        private float points;
        private ArrayList<TrfRound> rounds;

        public TrfPlayer(int startRank, Player.Sex sex, Title title, String name, Integer rating, Federation federation, int fideNo, short yearOfBirth, byte monthOfBirth, byte dayOfBirth, float points, ArrayList<TrfRound> rounds) {
            setStartRank(startRank);
            setSex(sex);
            setTitle(title);
            setName(name);
            setRating(rating);
            setFederation(federation);
            setFideNo(fideNo);
            setYearOfBirth(yearOfBirth);
            setMonthOfBirth(monthOfBirth);
            setDayOfBirth(dayOfBirth);
            setPoints(points);
            setRounds(rounds);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            Field[] fields = this.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true); // Make private fields accessible
                    String fieldName = field.getName();
                    Object fieldValue = field.get(this);
                    result.append(fieldName).append(": ").append(fieldValue).append(", ");
                }
            } catch (IllegalAccessException ignored) {
            }
            return result.toString();
        }

        public int getStartRank() {
            return startRank;
        }

        public void setStartRank(int startRank) {
            this.startRank = startRank;
        }

        public Player.Sex getSex() {
            return sex;
        }

        public void setSex(Player.Sex sex) {
            this.sex = sex;
        }

        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public Federation getFederation() {
            return federation;
        }

        public void setFederation(Federation federation) {
            this.federation = federation;
        }

        public short getYearOfBirth() {
            return yearOfBirth;
        }

        public void setYearOfBirth(short yearOfBirth) {
            this.yearOfBirth = yearOfBirth;
        }

        public int getFideNo() {
            return fideNo;
        }

        public void setFideNo(int fideNo) {
            this.fideNo = fideNo;
        }

        public byte getMonthOfBirth() {
            return monthOfBirth;
        }

        public void setMonthOfBirth(byte monthOfBirth) {
            this.monthOfBirth = monthOfBirth;
        }

        public byte getDayOfBirth() {
            return dayOfBirth;
        }

        public void setDayOfBirth(byte dayOfBirth) {
            this.dayOfBirth = dayOfBirth;
        }

        public float getPoints() {
            return points;
        }

        public void setPoints(float points) {
            this.points = points;
        }

        public ArrayList<TrfRound> getRounds() {
            return rounds;
        }

        public void setRounds(ArrayList<TrfRound> rounds) {
            this.rounds = rounds;
        }

    }

}
