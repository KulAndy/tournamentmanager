package com.example.tournamentmanager.operation;

import com.example.tournamentmanager.MainController;
import com.example.tournamentmanager.helper.DialogHelper;
import com.example.tournamentmanager.model.*;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.tournamentmanager.operation.FileOperation.*;

public class FIDEOperation {
    public static void downloadFIDEList() {
        final int[] standardProcessed = {2};
        final int[] rapidProcessed = {2};
        final int[] blitzProcessed = {2};
        Task<Void> taskStandard = new Task<>() {
            @Override
            protected Void call() {
                try {
                    downloadZip("http://ratings.fide.com/download/standard_rating_list_xml.zip", "standard_rating_list_xml.zip");
                    standardProcessed[0] = 1;
                    convertXMLToSQLite("standard_rating_list.xml", "standard_rating_list.db");
                    standardProcessed[0] = 0;
                } catch (IOException | URISyntaxException ignored) {
                }
                return null;
            }
        };
        Task<Void> taskRapid = new Task<>() {
            @Override
            protected Void call() {
                try {
                    downloadZip("http://ratings.fide.com/download/rapid_rating_list_xml.zip", "rapid_rating_list_xml.zip");
                    rapidProcessed[0] = 1;
                    convertXMLToSQLite("rapid_rating_list.xml", "rapid_rating_list.db");
                    rapidProcessed[0] = 0;
                } catch (IOException | URISyntaxException ignored) {
                }
                return null;
            }
        };
        Task<Void> taskBlitz = new Task<>() {
            @Override
            protected Void call() {
                try {
                    downloadZip("http://ratings.fide.com/download/blitz_rating_list_xml.zip", "blitz_rating_list_xml.zip");
                    blitzProcessed[0] = 1;
                    convertXMLToSQLite("blitz_rating_list.xml", "blitz_rating_list.db");
                    blitzProcessed[0] = 0;
                } catch (IOException | URISyntaxException ignored) {
                }
                return null;
            }
        };

        Thread threadStandard = new Thread(taskStandard);
        Thread threadRapid = new Thread(taskRapid);
        Thread threadBlitz = new Thread(taskBlitz);

        threadStandard.start();
        threadRapid.start();
        threadBlitz.start();
        try {
            threadStandard.join();
            threadRapid.join();
            threadBlitz.join();
        } catch (InterruptedException ignored) {
        }

        StringBuilder importResult = getStringBuilder(standardProcessed, rapidProcessed, blitzProcessed);
        if (standardProcessed[0] == 0 && rapidProcessed[0] == 0 && blitzProcessed[0] == 0) {
            DialogHelper.info(String.valueOf(importResult));
        } else {
            DialogHelper.error(String.valueOf(importResult));
        }

    }

    @NotNull
    private static StringBuilder getStringBuilder(int[] standardProcessed, int[] rapidProcessed, int[] blitzProcessed) {
        StringBuilder statment = new StringBuilder();
        switch (standardProcessed[0]) {
            case 0 -> statment.append("Standard list downloaded and converted succesfully\n");
            case 1 ->
                    statment.append("Standard list downloaded and unzipped successfully, but couldn't convert xml to local database\n");
            default -> statment.append("Couldn't download standard list\n");
        }
        switch (rapidProcessed[0]) {
            case 0 -> statment.append("Rapid list downloaded and converted succesfully\n");
            case 1 ->
                    statment.append("Rapid list downloaded and unzipped successfully, but couldn't convert xml to local database\n");
            default -> statment.append("Couldn't download rapid list\n");
        }
        switch (blitzProcessed[0]) {
            case 0 -> statment.append("Blitz list downloaded and converted succesfully");
            case 1 ->
                    statment.append("Blitz list downloaded and unzipped successfully, but couldn't convert xml to local database");
            default -> statment.append("Couldn't download blitz list");
        }
        return statment;
    }

    public static ArrayList<Player> searchPlayer(String name, Tournament.Type type) {
        ArrayList<Player> players = new ArrayList<>();
        if (name.trim().isEmpty()) {
            return players;
        }
        name = unidecode(name);
        name = name.replaceAll(",", "");
        try {
            try {
                players.addAll(searchInPolDb(name, type));
            } catch (SQLException e) {
                convertCsvToSqlite("rejestr_czlonkow.csv", "rejestr_czlonkow.db");
                players.addAll(searchInPolDb(name, type));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            if (name.length() > 3) {
                players.addAll(searchInFideDb(name, type));
            }
        } catch (SQLException e) {
            File file;
            switch (type) {
                case BLITZ -> {
                    file = new File("blitz_rating_list.xml");
                    if (file.exists()) {
                        convertXMLToSQLite("blitz_rating_list.xml", "blitz_rating_list.db");
                        players.addAll(searchPlayer(name, type));
                    }
                }
                case RAPID -> {
                    file = new File("rapid_rating_list.xml");
                    if (file.exists()) {
                        convertXMLToSQLite("rapid_rating_list.xml", "rapid_rating_list.db");
                        players.addAll(searchPlayer(name, type));
                    }
                }
                default -> {
                    file = new File("standard_rating_list.xml");
                    if (file.exists()) {
                        convertXMLToSQLite("standard_rating_list.xml", "standard_rating_list.db");
                        players.addAll(searchPlayer(name, type));
                    }
                }
            }

        }

        return players;
    }

    private static ArrayList<Player> searchInFideDb(String player, Tournament.Type type) throws SQLException {
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:";
            switch (type) {
                case BLITZ -> url += "blitz_rating_list.db";
                case RAPID -> url += "rapid_rating_list.db";
                default -> url += "standard_rating_list.db";
            }
            connection = DriverManager.getConnection(url);
            String query = "SELECT fideid, name, country, sex, title, rating, k, birthday FROM players WHERE REPLACE(name, ',', '') LIKE ? ORDER BY name";
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, player + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                int fideid = resultSet.getInt("fideid");
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                String sex = resultSet.getString("sex");
                String title = resultSet.getString("title");
                Integer rating = resultSet.getInt("rating");
                int k = resultSet.getInt("k");
                int birthday = resultSet.getInt("birthday");
                Federation fed = Federation.FIDE;
                try {
                    fed = Federation.valueOf(country);
                } catch (IllegalArgumentException ignored) {
                }

                players.add(new Player(
                        fed, null, name, Title.getTitle(title),
                        1000, rating, null, birthday + "-00-00", Objects.equals(sex, "M") ? Player.Sex.MALE : Player.Sex.FEMALE,
                        null, null, null, null, fideid, null

                ));
            }

            resultSet.close();
            statement.close();
            connection.close();
            return players;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Player> searchInPolDb(String player, Tournament.Type type) throws SQLException {
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:rejestr_czlonkow.db";
            connection = DriverManager.getConnection(url);
            String query = "SELECT id, NAZWISKO_IMIE, PLEC,DATA_URODZENIA, WZSZACH," +
                    "ID_FIDE,ELO,TYTUL,KLUB," +
                    "_KOL_CZL_R_FIDE_SZ,_KOL_CZL_R_FIDE_BL FROM players WHERE REPLACE(formatted, ',', '') LIKE ? ORDER BY NAZWISKO_IMIE";
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, player + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String NAZWISKO_IMIE = resultSet.getString("NAZWISKO_IMIE");
                String PLEC = resultSet.getString("PLEC");
                String DATA_URODZENIA = resultSet.getString("DATA_URODZENIA");
                String WZSZACH = resultSet.getString("WZSZACH");
                int ID_FIDE = resultSet.getInt("ID_FIDE");
                int ELO = resultSet.getInt("ELO");
                String TYTUL = resultSet.getString("TYTUL");
                String KLUB = resultSet.getString("KLUB");
                int _KOL_CZL_R_FIDE_SZ = resultSet.getInt("_KOL_CZL_R_FIDE_SZ");
                int _KOL_CZL_R_FIDE_BL = resultSet.getInt("_KOL_CZL_R_FIDE_BL");

                players.add(new Player(
                        Federation.POL, WZSZACH, NAZWISKO_IMIE, Title.getTitle(TYTUL),
                        1000, switch (type) {
                    case BLITZ -> _KOL_CZL_R_FIDE_BL;
                    case RAPID -> _KOL_CZL_R_FIDE_SZ;
                    default -> ELO;
                }
                        , KLUB, DATA_URODZENIA, Objects.equals(PLEC, "M") ? Player.Sex.MALE : Player.Sex.FEMALE,
                        null, null, null, id, ID_FIDE, null

                ));
            }

            resultSet.close();
            statement.close();
            connection.close();
            return players;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Player> searchSimilarFide(Player player, Tournament.Type type) {
        ArrayList<Player> players = new ArrayList<>();
        Integer fideId = player.getFideId();
        if (fideId != null && fideId > 0) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:";
                switch (type) {
                    case BLITZ -> url += "blitz_rating_list.db";
                    case RAPID -> url += "rapid_rating_list.db";
                    default -> url += "standard_rating_list.db";
                }
                Connection connection = DriverManager.getConnection(url);
                String query = "SELECT fideid, name, country, sex, title, rating, k, birthday FROM players WHERE fideid LIKE ? ORDER BY name";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + fideId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int retrievedFideId = resultSet.getInt("fideid");
                    if (retrievedFideId == fideId) {
                        String name = resultSet.getString("name");
                        String country = resultSet.getString("country");
                        String sex = resultSet.getString("sex");
                        String title = resultSet.getString("title");
                        int rating = resultSet.getInt("rating");
                        int k = resultSet.getInt("k");
                        int birthday = resultSet.getInt("birthday");
                        Federation fed;
                        try {
                            fed = Federation.valueOf(country);
                        } catch (IllegalArgumentException e) {
                            fed = Federation.FIDE;
                        }
                        players.add(new Player(
                                fed, null, name, Title.getTitle(title),
                                1000, rating, null, birthday + "-00-00", Objects.equals(sex, "M") ? Player.Sex.MALE : Player.Sex.FEMALE,
                                null, null, null, null, retrievedFideId, null
                        ));
                    }
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                final String decodedName = unidecode(player.getName()).replaceAll(",", "");
                players = searchInFideDb(decodedName, type);
                players = (ArrayList<Player>) players.parallelStream()
                        .filter(elem ->
                                Objects.equals(elem.getName(), decodedName) && elem.getYearOfBirth() == player.getYearOfBirth()
                        )
                        .collect(Collectors.toList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return players;
    }

    public static ArrayList<Player> searchSimilarPol(Player player, Tournament.Type type) {
        ArrayList<Player> players = new ArrayList<>();
        Integer polId = player.getLocalId();
        if (polId != null && polId > 0) {
            Connection connection;
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:rejestr_czlonkow.db";
                connection = DriverManager.getConnection(url);
                String query = "SELECT id, NAZWISKO_IMIE, PLEC,DATA_URODZENIA, WZSZACH," +
                        "ID_FIDE,ELO,TYTUL,KLUB," +
                        "_KOL_CZL_R_FIDE_SZ,_KOL_CZL_R_FIDE_BL FROM players WHERE id = ? ORDER BY NAZWISKO_IMIE";
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, polId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String NAZWISKO_IMIE = resultSet.getString("NAZWISKO_IMIE");
                    String PLEC = resultSet.getString("PLEC");
                    String DATA_URODZENIA = resultSet.getString("DATA_URODZENIA");
                    String WZSZACH = resultSet.getString("WZSZACH");
                    int ID_FIDE = resultSet.getInt("ID_FIDE");
                    int ELO = resultSet.getInt("ELO");
                    String TYTUL = resultSet.getString("TYTUL");
                    String KLUB = resultSet.getString("KLUB");
                    int _KOL_CZL_R_FIDE_SZ = resultSet.getInt("_KOL_CZL_R_FIDE_SZ");
                    int _KOL_CZL_R_FIDE_BL = resultSet.getInt("_KOL_CZL_R_FIDE_BL");

                    players.add(new Player(
                            Federation.POL, WZSZACH, NAZWISKO_IMIE, Title.getTitle(TYTUL),
                            1000, switch (type) {
                        case BLITZ -> _KOL_CZL_R_FIDE_BL;
                        case RAPID -> _KOL_CZL_R_FIDE_SZ;
                        default -> ELO;
                    }
                            , KLUB, DATA_URODZENIA, Objects.equals(PLEC, "M") ? Player.Sex.MALE : Player.Sex.FEMALE,
                            null, null, null, id, ID_FIDE, null

                    ));
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                final String decodedName = unidecode(player.getName()).replaceAll(",", "");
                players = searchInPolDb(decodedName, type);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return players;
    }

    public static void selectTrfReport(Tournament tournament) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("trf reports", "*.txt", "*.trf"));
        if (tournament.getName().trim().isEmpty()) {
            fileChooser.setInitialFileName("raport.txt");
        } else {
            fileChooser.setInitialFileName(tournament.getName().replaceAll("[\\/ ]", "_"));
        }
        File newFile = fileChooser.showSaveDialog(new Stage());

        try {
            saveTrfReport(trfReport(tournament), newFile);
            DialogHelper.info("Created report");
        } catch (Exception e) {
            DialogHelper.error("Couldn't create trf report");
        }
    }

    public static void saveTrfReport(String trf, File file) throws IOException {
        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
                file = new File(filePath);
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(trf);
            } catch (IOException e) {
                DialogHelper.error("Couldn't create trf report");
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException();
        }
    }

    public static String trfReport(Tournament tournament) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder trf = new StringBuilder();
        trf.append("012 ").append(tournament.getName());
        trf.append("\n022 ").append(tournament.getPlace());
        trf.append("\n032 ").append("POL");
        trf.append("\n042 ").append(dateFormat.format(tournament.getStartDate()));
        trf.append("\n052 ").append(dateFormat.format(tournament.getEndDate()));
        trf.append("\n062 ").append(tournament.getPlayers().size());
        trf.append("\n072 ").append(tournament.getPlayers().stream().filter(player -> player.getFideRating() > 1000).count());
        trf.append("\n082 0");
        trf.append("\n092 ").append(tournament.getSystem()).append(" SYSTEM");
        trf.append("\n102 ").append(tournament.getArbiter());

        StringBuilder controlTime = new StringBuilder();
        controlTime.append(tournament.getType()).append(": ");
        controlTime.append(tournament.getGameTime()).append(" min");
        if (tournament.getControlMove() > 0) {
            controlTime.append("/").append(tournament.getControlMove()).append(" moves")
                    .append(" + ").append(tournament.getControlAddition()).append("/end");
        }
        if (tournament.getIncrement() > 0) {
            controlTime.append(" + ").append(tournament.getIncrement()).append("sec increment per move starting from move 1");
        }

        trf.append("\n122 ").append(controlTime);
        trf.append("\n132").append(" ".repeat(88));
        SimpleDateFormat dateFormatRounds = new SimpleDateFormat("yy/MM/dd");
        for (Schedule.ScheduleElement element : tournament.getSchedule().getRounds()) {
            if (element.getDate() == null) {
                trf.append("00/00/00");
            } else {
                trf.append("%8s".formatted(dateFormatRounds.format(element.getDate())));
            }
            trf.append(" ".repeat(2));
        }

        if (tournament.getRoundsObs().isEmpty()) {
            if (tournament.getFirstColor() == null) {
                SecureRandom secureRandom = new SecureRandom();
                byte[] randomBytes = new byte[1]; // Adjust byte array size as needed

                secureRandom.nextBytes(randomBytes);
                int randomValue = Math.abs(randomBytes[0] % 2);
                if (randomValue == 0) {
                    trf.append("\nXXC white1");
                } else {
                    trf.append("\nXXC black1");
                }
            } else {
                switch (tournament.getFirstColor()) {
                    case WHITE -> trf.append("\nXXC white1");
                    case BLACK -> trf.append("\nXXC black1");
                }
            }
        }


        PlayerList players = tournament.getPlayers();
        players.sort(players.getComparator());
        ArrayList<Title> fideTitles = new ArrayList<>();
        fideTitles.add(Title.GM);
        fideTitles.add(Title.IM);
        fideTitles.add(Title.FM);
        fideTitles.add(Title.CM);
        fideTitles.add(Title.WGM);
        fideTitles.add(Title.WIM);
        fideTitles.add(Title.WFM);
        fideTitles.add(Title.WCM);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            trf.append("\n001 ")
                    .append("%4s".formatted(players.getUuid2startNo().get(player.getPlayerid())))
                    .append(" ")
                    .append(player.getSex() == Player.Sex.FEMALE ? "w" : "m")
                    .append(" ")
                    .append("%3s".formatted(
                            fideTitles.contains(player.getTitle()) ? player.getTitle() : ""
                    ))
                    .append("%-34s".formatted(unidecode(player.getName())))
                    .append(player.getFideRating())
                    .append(" ");
            if (player.getFederation() == null || player.getFederation() == Federation.FIDE) {
                trf.append("   ");
            } else {
                trf.append(player.getFederation());
            }
            trf.append(" ")
                    .append("%11s".formatted((player.getFideId() != null && player.getFideId() > 0) ? player.getFideId() : ""))
                    .append(" ")
                    .append("%10s".formatted(player.getDateOfBirth()))
                    .append(" ")
                    .append("%4.1f".formatted(Float.isNaN(player.getPoints()) ? 0.0 : player.getPoints()))
                    .append("     ");

            ArrayList<Game> rounds = player.getRounds();
            for (Game game : rounds) {
                Player opponent = player.getOpponent(game);
                trf.append("  ");
                if (opponent == tournament.getPlayers().getBye()) {
                    trf.append("0000 - ").append("F");
                } else if (opponent == tournament.getPlayers().getHalfbye()) {
                    trf.append("0000 - ").append("H");
                } else if (opponent == tournament.getPlayers().getUnpaired() || Objects.equals(opponent.getName(), "unpaired")) {
                    trf.append("0000 - ").append("Z");
                } else {
                    if (game.isForfeit()) {
                        trf.append("%4d".formatted(players.getUuid2startNo().get(opponent.getPlayerid())))
                                .append(" ");
                        switch (player.getRoundColor(game)) {
                            case WHITE -> trf.append("w ");
                            case BLACK -> trf.append("b ");
                        }
                        if (game.getWhiteResult() == null && game.getBlackResult() == null) {
                            trf.append(" ");
                        } else {
                            if (player.getRoundResult(game) == Result.WIN) {
                                trf.append("+");
                            } else {
                                trf.append("-");
                            }
                        }
                    } else {
                        trf.append("%4d".formatted(players.getUuid2startNo().get(opponent.getPlayerid())))
                                .append(" ");
                        switch (player.getRoundColor(game)) {
                            case WHITE -> trf.append("w ");
                            case BLACK -> trf.append("b ");
                        }
                        if (player.getRoundResult(game) == null) {
                            trf.append(" ");
                        } else {
                            switch (player.getRoundResult(game)) {
                                case WIN -> trf.append("1");
                                case DRAW -> trf.append("=");
                                default -> trf.append("0");
                            }
                        }

                    }
                }
            }
        }

        trf.append("\nXXR ").append(tournament.getRoundsNumber());
        StringBuilder withdrawed = new StringBuilder("\nXXZ");
        for (Withdraw withdraw : tournament.getWithdraws()) {
            if (withdraw.getType() == Withdraw.WithdrawType.TOURNAMENT || withdraw.getRoundNo() == tournament.getRounds().size() + 1) {
                Integer id = players.getUuid2startNo().get(withdraw.getPlayer().getPlayerid());
                if (id != null) {
                    withdrawed.append("%4d".formatted(players.getUuid2startNo().get(withdraw.getPlayer().getPlayerid())));
                }
            }
        }
        if (withdrawed.toString().trim().length() > 4) {
            trf.append(withdrawed);
        }
        trf.append("\n");
        trf.append("XCC rank\n");
        trf
                .append("XXS W=").append(Player.getWinPoints())
                .append(" D=").append(Player.getDrawPoints())
                .append(" WW=").append(Player.getWinPoints())
                .append(" BW=").append(Player.getWinPoints())
                .append(" WL=").append(Player.getLosePoints())
                .append(" BL=").append(Player.getLosePoints())
                .append(" FPB=").append(Player.getByePoints())
                .append(" HBP=").append(Player.getHalfByePoints())
                .append(" ZBP=").append(Player.getForfeitLosePoints())
                .append(" FW=").append(Player.getForfeitWinPoints())
                .append(" LW=").append(Player.getForfeitLosePoints()).append("\n");

        return trf.toString();
    }


    public static void importTrfReport(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("trf reports", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                Tournament tournament = new Tournament(new TrfTournament(selectedFile));
                TournamentOperation.loadTournament(tournament, controller);
            } catch (Exception e) {
                DialogHelper.error("An error eccured");
            }

        } else {
            DialogHelper.warning("No file selected");
        }
    }
}
