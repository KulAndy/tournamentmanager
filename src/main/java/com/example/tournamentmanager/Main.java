package com.example.tournamentmanager;

import com.example.tournamentmanager.model.Tournament;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.TimeZone;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("en", "US"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        Tournament tournament = new Tournament();

        mainController.setTournament(tournament);

        Scene scene = new Scene(root, 1280, 720);
        mainController.init(scene, getProgramName(), getProgramExtension());
        stage.setTitle(getProgramName());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public String getProgramName() {
        return "andchess";
    }

    public String getProgramExtension() {
        return "and";
    }
}
