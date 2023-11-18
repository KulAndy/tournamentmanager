package com.example.lolmanager.operation;

import com.example.lolmanager.MainController;
import com.example.lolmanager.adapter.LocalDateAdapter;
import com.example.lolmanager.model.Game;
import com.example.lolmanager.model.Player;
import com.example.lolmanager.model.PlayerList;
import com.example.lolmanager.model.Tournament;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.example.lolmanager.helper.GeneralHelper.error;
import static com.example.lolmanager.helper.GeneralHelper.warning;

public class TournamentOperation {
    private static final Stage fileStage = new Stage();

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
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria1().setValue(tournament.getPlayers().getComparator().getCriteria1());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria2().setValue(tournament.getPlayers().getComparator().getCriteria2());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria3().setValue(tournament.getPlayers().getComparator().getCriteria3());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria4().setValue(tournament.getPlayers().getComparator().getCriteria4());
            controller.getPlayersHelper().getPlayersSortHelper().getCriteria5().setValue(tournament.getPlayers().getComparator().getCriteria5());
        }
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
                error("An error occured");
            }

        }
    }

    public static void open(MainController controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(controller.getProgramName() + " files", "*." + controller.getProgramExtension()));
        File selectedFile = fileChooser.showOpenDialog(fileStage);

        if (selectedFile != null) {
            importJson(selectedFile, controller);
        } else {
            warning("No file selected");
        }

    }

    private static void exportTournament(Tournament tournament, File file) throws IOException {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        String json = gson.toJson(tournament);
        String fileName = "tournament.json";
        String fileContent = json;

        try (FileOutputStream fos = new FileOutputStream(file);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);

            byte[] contentBytes = fileContent.getBytes();
            zipOut.write(contentBytes, 0, contentBytes.length);
            zipOut.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
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
                    Gson gson = new Gson();
                    Type tournamentType = new TypeToken<Tournament>() {
                    }.getType();
                    Tournament tournament = gson.fromJson(content, tournamentType);
                    PlayerList players = tournament.getPlayers();
                    for (Player player : players) {
                        player.getRounds().clear();
                    }

                    for (ArrayList<Game> round : tournament.getRounds()) {
                        for (Game game : round) {
                            Player white = players.get(game.getWhiteUUDI());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
