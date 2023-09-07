package com.example.lolmanager.operation;

import com.example.lolmanager.MainController;
import com.example.lolmanager.calculation.PZSzachCalculation;
import com.example.lolmanager.helper.GeneralHelper;
import com.example.lolmanager.model.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.lolmanager.helper.GeneralHelper.error;
import static com.example.lolmanager.helper.GeneralHelper.warning;
import static com.example.lolmanager.operation.FileOperation.*;

public class FIDEOperation {
    public static void downloadFIDEList() {
        try {
            downloadFIDEfile("http://ratings.fide.com/download/standard_rating_list_xml.zip", "standard_rating_list_xml.zip");
            downloadFIDEfile("http://ratings.fide.com/download/rapid_rating_list_xml.zip", "rapid_rating_list_xml.zip");
            downloadFIDEfile("http://ratings.fide.com/download/blitz_rating_list_xml.zip", "blitz_rating_list_xml.zip");
        } catch (Exception e) {
            GeneralHelper.error("Couldn't download list");
        }
        try {
            convertXMLToSQLite("standard_rating_list.xml", "standard_rating_list.db");
            convertXMLToSQLite("rapid_rating_list.xml", "rapid_rating_list.db");
            convertXMLToSQLite("blitz_rating_list.xml", "blitz_rating_list.db");
            GeneralHelper.info("List downloaded and unzipped successfully");
        } catch (Exception e) {
            GeneralHelper.warning("List downloaded and unzipped successfully, but couldn't convert xml to local database");
        }
    }

    public static void downloadFIDEfile(String url, String filename) throws IOException {
        String savePath = "./";

        URL downloadUrl = new URL(url);
        InputStream in = new BufferedInputStream(downloadUrl.openStream());
        Path archive = Path.of(savePath + filename);
        Files.copy(in, archive, StandardCopyOption.REPLACE_EXISTING);
        unzipFile(savePath + filename, savePath);
        Files.delete(archive);
    }

    public static ArrayList<Player> searchPlayer(String name, Tournament.Type type) {
        ArrayList<Player> players = new ArrayList<>();
        if (name.trim().length() == 0) {
            return players;
        }
        name = unidecode(name);
        name = name.replaceAll(",", "");
        try {
            try {
                players.addAll(searchInPolDb(name, type));
            } catch (SQLException e) {
                File file = new File("rejestr_czlonkow.csv");
                convertCsvToSqlite("rejestr_czlonkow.csv", "rejestr_czlonkow.db");
                players.addAll(searchInPolDb(name, type));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            players.addAll(searchInFideDb(name, type));
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
        if (player.length() <= 3) {
            return new ArrayList<>();
        }
        Connection connection = null;
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
                players.add(new Player(
                        Federation.valueOf(country), null, name, Title.getTitle(title),
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
        Connection connection = null;
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


    private static ArrayList<Player> searchInCsv(String player, Tournament.Type type) {
        ArrayList<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("rejestr_czlonkow.csv")))) {

            // Skip the first line (header)
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] values = line.split(",");
                    String NAZWISKO_IMIE = values[1];

                    if (unidecode(NAZWISKO_IMIE).toUpperCase().replaceAll(",", "").startsWith(player.toUpperCase().replaceAll(",", ""))) {
                        int ID;
                        try {
                            ID = Integer.parseInt(values[0]);
                        } catch (NumberFormatException e) {
                            ID = 0;
                        }
                        String PLEC = values[2];
                        String DATA_URODZENIA = values[3];
                        String NR_LICENCJI_ZAW = values[4];
                        String WZSZACH = values[5];
                        int ID_FIDE;
                        try {
                            ID_FIDE = Integer.parseInt(values[6]);
                        } catch (NumberFormatException e) {
                            ID_FIDE = 0;
                        }
                        int ELO;
                        try {
                            ELO = Integer.parseInt(values[7]);
                        } catch (NumberFormatException e) {
                            ELO = 1000;
                        }
                        Title TYTUL = Title.getTitle(values[8]);
                        String KLUB = values[9];
                        String ZN_FOTO = values[10];
                        String OBYWATELSTWO = values[11];
                        int _KOL_CZL_R_FIDE_SZ;
                        try {
                            _KOL_CZL_R_FIDE_SZ = Integer.parseInt(values[12]);
                        } catch (NumberFormatException e) {
                            _KOL_CZL_R_FIDE_SZ = 1000;
                        }
                        int _KOL_CZL_R_FIDE_BL;
                        try {
                            _KOL_CZL_R_FIDE_BL = Integer.parseInt(values[13]);
                        } catch (NumberFormatException e) {
                            _KOL_CZL_R_FIDE_BL = 1000;
                        }

                        Player.Sex sex = Objects.equals(PLEC, "M") ? Player.Sex.MALE : Player.Sex.FEMALE;
                        players.add(new Player(
                                Federation.POL, WZSZACH,
                                NAZWISKO_IMIE, TYTUL,
                                PZSzachCalculation.getTitleValue(TYTUL, sex),
                                switch (type) {
                                    case RAPID -> _KOL_CZL_R_FIDE_SZ;
                                    case BLITZ -> _KOL_CZL_R_FIDE_BL;
                                    default -> ELO;
                                },
                                KLUB, DATA_URODZENIA, sex, null,
                                null, null, ID, ID_FIDE, null
                        ));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return players;
    }

    public static void trfRaport(Tournament tournament) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder trf = new StringBuilder();
        trf.append("012 ").append(tournament.getName());
        trf.append("\n022").append(tournament.getPlace());
        trf.append("\n032 ").append("POL");
        trf.append("\n042 ").append(dateFormat.format(tournament.getStartDate()));
        trf.append("\n052 ").append(dateFormat.format(tournament.getEndDate()));
        trf.append("\n062 ").append(tournament.getPlayers().size());
        trf.append("\n072 ").append(tournament.getPlayers().stream().filter(player -> player.getFideRating() > 1000).count());
        trf.append("\n092 ").append(tournament.getType());
        trf.append("\n112 ").append(tournament.getArbiter());

        StringBuilder controlTime = new StringBuilder();
        controlTime.append(tournament.getGameTime()).append(" min");
        if (tournament.getControlMove() > 0) {
            controlTime.append("/").append(tournament.getControlMove()).append(" moves")
                    .append(" + ").append(tournament.getControlAddition()).append("/end");
        }
        if (tournament.getIncrement() > 0) {
            controlTime.append(" + ").append(tournament.getIncrement()).append("sec increment per move starting from move 1");
        }

        trf.append("\n122 ").append(controlTime);

        PlayerList players = tournament.getPlayers();
        players.sort();
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
                    .append(" ")
                    .append(player.getFideRating())
                    .append(" ")
                    .append(player.getFederation() == null ? "   " : player.getFederation())
                    .append(" ")
                    .append("%11s".formatted((player.getFideId() != null && player.getFideId() > 0) ? player.getFideId() : ""))
                    .append(" ")
                    .append(player.getDateOfBirth())
                    .append(" ")
                    .append("%4.1f".formatted(Float.isNaN(player.getPoints()) ? 0.0 : player.getPoints()))
                    .append("     ")
            ;

            ArrayList<Game> rounds = player.getRounds();
            for (Game game : rounds) {
                Player opponent = player.getOpponent(game);
                trf.append("  ");
                if (game.isForfeit()) {
                    switch (opponent.getName()) {
                        case "bye", "unpaired" -> {
                            trf.append("0000 - ").append("U");
                        }
                        case "halfbye" -> {
                            trf.append("0000 - ").append("H");
                        }
                        default -> {
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

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("trf raports", "*.txt"));
        File newFile = fileChooser.showSaveDialog(new Stage());

        if (newFile != null) {
            String filePath = newFile.getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }
            newFile = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                writer.write(String.valueOf(trf));
                GeneralHelper.info("Created raport");
            } catch (IOException e) {
                GeneralHelper.error("Couldn't create trf raport");
                e.printStackTrace();
            }

        }
    }

    public static void importTrfReport(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("trf raports", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                Tournament tournament = new Tournament(new TrfTournament(selectedFile));
                TournamentOperation.loadTournament(tournament, controller);
            } catch (Exception e) {
                e.printStackTrace();
                error("An error eccured");
            }

        } else {
            warning("No file selected");
        }

    }


}
