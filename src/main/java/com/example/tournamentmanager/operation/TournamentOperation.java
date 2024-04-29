package com.example.tournamentmanager.operation;

import com.example.tournamentmanager.MainController;
import com.example.tournamentmanager.adapter.DateAdapter;
import com.example.tournamentmanager.adapter.LocalDateAdapter;
import com.example.tournamentmanager.adapter.ObjectIdAdapter;
import com.example.tournamentmanager.helper.DialogHelper;
import com.example.tournamentmanager.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TournamentOperation {
    public static final Stage fileStage = new Stage();

    public static void loadTournament(Tournament tournament, MainController controller) {
        if (tournament != null && controller != null) {

            controller.getTourName().setText(tournament.getName());
            LocalDate localStartDate;
            LocalDate localEndDate;
            if (tournament.getStartDate() == null) {
                localStartDate = LocalDate.now();
            } else {
                localStartDate = tournament.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            if (tournament.getEndDate() == null) {
                localEndDate = LocalDate.now();
            } else {
                localEndDate = tournament.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            controller.getTourStartDate().setValue(localStartDate);
            controller.getTournament().setStartDate(Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            controller.getTourEndDate().setValue(localEndDate);
            controller.getTournament().setEndDate(Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            controller.getTourPlace().setText(tournament.getPlace());
            controller.getTourGameTime().setText(String.valueOf(tournament.getGameTime()));
            controller.getTourIncrement().setText(String.valueOf(tournament.getIncrement()));
            controller.getTourControlMove().setText(String.valueOf(tournament.getControlMove()));
            controller.getTourControlAddition().setText(String.valueOf(tournament.getControlAddition()));
            controller.getTourType().setValue(tournament.getType());
            controller.getTourRtPZSzach().setSelected(tournament.getRating().getPZSzachRated());
            controller.getTourRtFIDE().setSelected(tournament.getRating().getFIDERated());
            controller.getTourNoRounds().setText(String.valueOf(tournament.getRoundsNumber()));
            controller.getTourSystem().setValue(tournament.getSystem());
            controller.getTourArbiter().setText(tournament.getArbiter());
            controller.getTourOrganizer().setText(tournament.getOrganizer());
            controller.getTourEmail().setText(tournament.getEmail());
            controller.getTourTB1().setValue(tournament.getTiebreak().getTiebreak1());
            controller.getTourTB2().setValue(tournament.getTiebreak().getTiebreak2());
            controller.getTourTB3().setValue(tournament.getTiebreak().getTiebreak3());
            controller.getTourTB4().setValue(tournament.getTiebreak().getTiebreak4());
            controller.getTourTB5().setValue(tournament.getTiebreak().getTiebreak5());
            controller.getPointsWin().setText(String.valueOf(tournament.getTiebreak().getWinPoints()));
            controller.getPointsDraw().setText(String.valueOf(tournament.getTiebreak().getDrawPoints()));
            controller.getPointsLose().setText(String.valueOf(tournament.getTiebreak().getLosePoints()));
            controller.getPointsForfeitWin().setText(String.valueOf(tournament.getTiebreak().getForfeitWinPoints()));
            controller.getPointsForfeitLose().setText(String.valueOf(tournament.getTiebreak().getForfeitLosePoints()));
            controller.getPointsBye().setText(String.valueOf(tournament.getTiebreak().getByePoints()));
            controller.getPointsHalfBye().setText(String.valueOf(tournament.getTiebreak().getHalfByePoints()));
            controller.getMinInitGames().setText(String.valueOf(tournament.getRating().getMinInitGames()));
            controller.getRatingFloor().setText(String.valueOf(tournament.getRating().getRatingFloor()));
            controller.getPZSzach43Cb().setSelected(tournament.getRating().getPZSzach43());
            controller.getPZSzach44Cb().setSelected(tournament.getRating().getPZSzach44());
            controller.getPZSzach45Cb().setSelected(tournament.getRating().getPZSzach45());
            controller.getPZSzach46Cb().setSelected(tournament.getRating().getPZSzach46());
            controller.getPZSzach47Cb().setSelected(tournament.getRating().getPZSzach47());
            controller.getMaxTitle().setValue(tournament.getRating().getMaxTitle());
            controller.getTwoOtherFeds().setSelected(tournament.getRating().getTwoOtherFederations());
            controller.getMinTitleGames().setText(String.valueOf(tournament.getRating().getMinTitleGames()));
            controller.getTournament().getPlayersObs().clear();
            controller.getTournament().getPlayersObs().addAll(tournament.getPlayers());
            controller.getTournament().getPlayers().setBye(tournament.getPlayers().getBye());
            controller.getTournament().getPlayers().setHalfbye(tournament.getPlayers().getHalfbye());
            controller.getTournament().getPlayers().setUnpaired(tournament.getPlayers().getUnpaired());
            controller.getTournament().getRoundsObs().clear();
            controller.getTournament().getRoundsObs().addAll(tournament.getRounds());
            controller.getTournament().getWithdrawsObs().clear();
            controller.getTournament().getWithdrawsObs().addAll(tournament.getWithdraws());
            controller.getTournament().getPredicatesObs().clear();
            controller.getTournament().getPredicatesObs().addAll(tournament.getPredicates());
            controller.getTournament().getScheduleElementsObs().clear();
            controller.getTournament().getScheduleElementsObs().addAll(tournament.getSchedule());
            controller.getTournament().getScheduleElementsObs().set(0, tournament.getSchedule().getBriefing());
            controller.getTournament().getSchedule().setBriefing(tournament.getSchedule().getBriefing());
            controller.getTournament().getSchedule().setClosing(tournament.getSchedule().getClosing());
            controller.getTournament().setRoundsNumber(tournament.getRoundsNumber());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria1().setValue(tournament.getPlayers().getComparator().getCriteria1());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria2().setValue(tournament.getPlayers().getComparator().getCriteria2());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria3().setValue(tournament.getPlayers().getComparator().getCriteria3());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria4().setValue(tournament.getPlayers().getComparator().getCriteria4());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria5().setValue(tournament.getPlayers().getComparator().getCriteria5());
            if (!tournament.getRounds().isEmpty()) {
                controller.getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().setValue(tournament.getRounds().size());
            } else {
                controller.getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().setValue(null);
            }
            controller.getHomeTabHelper().getTiebreakHelper().getTourFIDEMode().setSelected(tournament.getTiebreak().isFIDEMode());
        }

        Platform.runLater(() -> {
            try {
                assert controller != null;
                TieBreakServerWrapper.generateTiebreak(controller.getTournament(), controller.getTournament().calculateEndedRound());
            } catch (IOException | InterruptedException ignored) {
            }
        });

    }

    public static void saveAs(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(controller.getProgramName() + " files", "*." + controller.getProgramExtension()));
        File newFile = fileChooser.showSaveDialog(fileStage);

        if (newFile != null) {
            String filePath = newFile.getAbsolutePath();
            if (!filePath.endsWith("." + controller.getProgramExtension())) {
                filePath += "." + controller.getProgramExtension();
            }
            newFile = new File(filePath);
            controller.setFile(newFile);
            save(controller);
        }
    }

    public static void save(MainController controller) {
        File file = controller.getFile();
        if (file == null) {
            saveAs(controller);
        } else {
            try {
                exportTournament(controller.getTournament(), controller.getFile());
            } catch (IOException e) {
                DialogHelper.error("An error occured");
            }

        }
    }

    public static void open(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(controller.getProgramName() + " files", "*." + controller.getProgramExtension()));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(fileStage);
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                if (!controller.getFiles().contains(file)) {
                    controller.getFiles().add(file);
                }
            }
            importJson(selectedFiles.get(selectedFiles.size() - 1), controller);
            controller.getTournamentSelect().setValue(selectedFiles.get(selectedFiles.size() - 1));
        }

    }

    public static void exportTournament(Tournament tournament, File file) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Date.class, new DateAdapter())
                .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
                .create();

        String json = gson.toJson(tournament);
        String tournamentFileName = "tournament.json";
        String settingsFileName = "settings.toml";

        Path tempDir = Files.createTempDirectory("tempZip");

        boolean tournamentFileExists = false;

        if (file.exists()) {
            try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file))) {
                ZipEntry entry;
                while ((entry = zipIn.getNextEntry()) != null) {
                    Path filePath = tempDir.resolve(entry.getName());

                    if (entry.isDirectory()) {
                        Files.createDirectories(filePath);
                    } else {
                        Files.copy(zipIn, filePath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    if (entry.getName().equals(tournamentFileName)) {
                        tournamentFileExists = true;
                    }

                    zipIn.closeEntry();
                }
            }
        }

        Path tournamentFilePath = tempDir.resolve(tournamentFileName);
        Files.write(tournamentFilePath, json.getBytes());

        if (tournamentFileExists) {
            Files.copy(tournamentFilePath, tempDir.resolve(tournamentFileName), StandardCopyOption.REPLACE_EXISTING);
        }

        Path settingsFilePath = tempDir.resolve(settingsFileName);
        boolean settingsFileExists = Files.exists(settingsFilePath);

        if (!settingsFileExists) {
            String settingsTemplateContent = "";
            try {
                Path settingsTemplatePath = Paths.get("settings.toml");
                settingsTemplateContent = Files.readString(settingsTemplatePath);
            } catch (IOException e) {
                String url = "https://raw.githubusercontent.com/KulAndy/tournamentmanager/master/settings.toml";
                try (InputStream inputStream = new URL(url).openStream()) {
                    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                    settingsTemplateContent = scanner.hasNext() ? scanner.next() : "";
                } catch (IOException ignored) {
                }
                try (BufferedWriter ou = new BufferedWriter(new FileWriter("settings.toml"))
                ) {
                    ou.write(settingsTemplateContent);
                }

            } finally {
                Files.write(settingsFilePath, settingsTemplateContent.getBytes());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            Files.walk(tempDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(tempDir.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException ignored) {
                        }
                    });
        } finally {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    public static void importJson(File file, MainController controller) {
        String fileName = "tournament.json";

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zipIn = new ZipInputStream(bis)) {

            ZipEntry zipEntry;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                if (zipEntry.getName().equals(fileName)) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipIn.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    String content = outputStream.toString(StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .registerTypeAdapter(Schedule.class, new Schedule.ScheduleDeserializer())
                            .registerTypeAdapter(Date.class, new DateAdapter())
                            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
                            .create();
                    Type tournamentType = new TypeToken<Tournament>() {
                    }.getType();
                    Tournament tournament = gson.fromJson(content, tournamentType);
                    PlayerList players = tournament.getPlayers();
                    for (Player player : players) {
                        player.getRounds().clear();
                    }

                    for (ArrayList<Game> round : tournament.getRounds()) {
                        for (Game game : round) {
                            Player white = players.get(game.getWhiteUUID());
                            Player black = players.get(game.getBlackUUID());
                            game.setWhite(white);
                            game.setBlack(black);
                            white.addRound(game);
                            black.addRound(game);
                        }
                    }
                    TournamentOperation.loadTournament(tournament, controller);

                    break;
                }
            }
            controller.setFile(file);
        } catch (IOException ignored) {
        }
    }

    public static void importPgn(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import pgn");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pgn files", "*.pgn"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            LinkedList<PgnGame> pgnGames = new LinkedList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                StringBuilder builder = new StringBuilder();
                byte counter = 1;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                    if (line.trim().isEmpty()) {
                        counter++;
                        counter %= 2;
                        if (counter == 0) {
                            PgnGame game = new PgnGame(builder.toString());
                            if (game.getRound() == null || game.getRound() <= 0) {
                                game.setRound((byte) controller.getTournament().getRoundsObs().size());
                            }
                            pgnGames.add(game);
                            builder.setLength(0);
                        }
                    }

                }
                String buffered = builder.toString().trim();
                if (buffered.contains("White") && buffered.contains("Black")) {
                    PgnGame game = new PgnGame(builder.toString());
                    if (game.getRound() == null || game.getRound() <= 0) {
                        game.setRound((byte) controller.getTournament().getRoundsObs().size());
                    }
                    pgnGames.add(game);
                    builder.setLength(0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Set<Byte> editedRounds = new HashSet<>();
            for (PgnGame pgnGame : pgnGames) {
                while (controller.getTournament().getRoundsObs().size() < pgnGame.getRound()) {
                    controller.getTournament().getRoundsObs().add(new ArrayList<>());
                }
                Player white = controller.getTournament().getPlayers().get(pgnGame.getWhite());
                if (white == null) {
                    white = new Player(pgnGame.getWhite());
                    controller.getTournament().getPlayers().add(white);
                }
                Player black = controller.getTournament().getPlayers().get(pgnGame.getBlack());
                if (black == null) {
                    black = new Player(pgnGame.getBlack());
                    controller.getTournament().getPlayers().add(black);
                }
                Game game = getGame(pgnGame, white, black);
                white.addRound(game);
                black.addRound(game);
                editedRounds.add(pgnGame.getRound());
                controller.getTournament().getRoundsObs().get(pgnGame.getRound() - 1).add(game);
            }

            for (Byte round : editedRounds) {
                controller.getTournament().getRoundsObs().get(round - 1).sort(controller.getTournament().getPairingComparator());
            }
            controller.getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().setValue(Integer.valueOf(editedRounds.parallelStream().max(Byte::compare).orElse((byte) 0)));
            if (controller.getTournament().getName().isEmpty()) {
                controller.getHomeTabHelper().getBasicInfoHelper().getTourName().setText(pgnGames.get(0).getEvent());
                controller.getHomeTabHelper().getBasicInfoHelper().getTourPlace().setText(pgnGames.get(0).getSite());
            }
            if (controller.getTournament().getRoundsObs().size() > controller.getTournament().getRoundsNumber()) {
                controller.getHomeTabHelper().getBasicInfoHelper().getTourNoRounds().setText(String.valueOf(controller.getTournament().getRoundsObs().size()));
            }
            DialogHelper.info("Imported pgn successfully");

        } else {
            DialogHelper.warning("No file selected");
        }

    }

    private static Game getGame(PgnGame pgnGame, Player white, Player black) {
        Result whiteResult, blackResult;
        boolean forfeit;
        switch (pgnGame.getResultTag()) {
            case "1-0" -> {
                whiteResult = Result.WIN;
                blackResult = Result.LOSE;
                forfeit = false;
            }
            case "0-1" -> {
                whiteResult = Result.LOSE;
                blackResult = Result.WIN;
                forfeit = false;
            }
            case "1/2-1/2" -> {
                whiteResult = Result.DRAW;
                blackResult = Result.DRAW;
                forfeit = false;
            }
            case "0-0" -> {
                whiteResult = Result.LOSE;
                blackResult = Result.LOSE;
                forfeit = false;
            }
            case "+--" -> {
                whiteResult = Result.WIN;
                blackResult = Result.LOSE;
                forfeit = true;
            }
            case "--+" -> {
                whiteResult = Result.LOSE;
                blackResult = Result.WIN;
                forfeit = true;
            }
            default -> {
                whiteResult = null;
                blackResult = null;
                forfeit = true;
            }
        }

        return new Game(white, black, whiteResult, blackResult, forfeit);
    }

    public static void exportRoundPgn(ArrayList<Game> games, Tournament tournament) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to pgn");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pgn files", "*.pgn"));
        File pgnFile = fileChooser.showSaveDialog(fileStage);

        if (pgnFile != null) {
            String filePath = pgnFile.getAbsolutePath();
            if (!filePath.endsWith(".pgn")) {
                filePath += ".pgn";
            }
            pgnFile = new File(filePath);

            StringBuilder pgn = new StringBuilder();
            for (Game game : games) {
                PgnGame pgnGame = new PgnGame(game.getWhite(), game.getBlack(), new Date(), tournament);
                pgn.append(pgnGame.getPgn()).append("\n");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(pgnFile))) {
                bw.write(pgn.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void exportPgn(Tournament tournament) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to pgn");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pgn files", "*.pgn"));
        File pgnFile = fileChooser.showSaveDialog(fileStage);

        if (pgnFile != null) {
            String filePath = pgnFile.getAbsolutePath();
            if (!filePath.endsWith(".pgn")) {
                filePath += ".pgn";
                pgnFile = new File(filePath);
            }

            StringBuilder pgn = new StringBuilder();

            for (int i = 0; i < tournament.getRounds().size(); i++) {
                for (Game game : tournament.getRound(i)) {
                    PgnGame pgnGame = new PgnGame(game.getWhite(), game.getBlack(), new Date(), tournament);
                    pgn.append(pgnGame.getPgn()).append("\n");
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(pgnFile))) {
                bw.write(pgn.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}