package com.example.tournamentmanager.helper;

import com.example.tournamentmanager.MainController;
import com.example.tournamentmanager.model.RemoteTournament;
import com.example.tournamentmanager.model.SwsxTournament;
import com.example.tournamentmanager.model.Tournament;
import com.example.tournamentmanager.operation.TournamentOperation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.example.tournamentmanager.operation.FileOperation.downloadZip;
import static com.example.tournamentmanager.operation.FileOperation.unzip;
import static com.example.tournamentmanager.operation.TournamentOperation.fileStage;
import static com.example.tournamentmanager.operation.TournamentOperation.importJson;

public class DialogHelper {
    public static void error(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void warning(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void info(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            int numberOfLines = content.split("\n").length;
            if (numberOfLines <= 15) {
                alert.setContentText(content);
            } else {
                VBox contentBox = new VBox();
                Label label = new Label(content);
                contentBox.getChildren().add(label);
                ScrollPane scrollPane = new ScrollPane(contentBox);
                scrollPane.setFitToWidth(true);
                scrollPane.setPrefViewportHeight(300);
                alert.getDialogPane().setContent(scrollPane);
            }
            alert.showAndWait();
        });
    }

    public static CompletableFuture<Boolean> confirm(String content) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText(content);
            ButtonType confirmButton = new ButtonType("Continue");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == confirmButton) {
                    future.complete(true);
                } else if (buttonType == cancelButton) {
                    future.complete(false);
                }
            });
        });

        return future;
    }

    public static CompletableFuture<String> threeOptionsDialog(String content, String optionA, String optionB) {
        CompletableFuture<String> future = new CompletableFuture<>();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Export pgn");
            alert.setHeaderText("Choose an option:");
            alert.setContentText(content);

            ButtonType buttonTypeA = new ButtonType(optionA);
            ButtonType buttonTypeB = new ButtonType(optionB);
            ButtonType buttonTypeCancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonTypeA, buttonTypeB, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == buttonTypeA) {
                    future.complete("A");
                } else if (buttonType == buttonTypeB) {
                    future.complete("B");
                } else {
                    future.complete("Cancel");
                }
            });
        });

        return future;
    }

    public static void showLoginPopup() {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                Toml toml = new Toml().read(new File("settings.toml"));
                String serverUrl = toml.getTable("remote").getString("api");

                URL url;
                try {
                    URI uri = new URI(serverUrl + toml.getTable("remote").getString("login"));
                    url = uri.toURL();
                } catch (URISyntaxException | MalformedURLException ex) {
                    error("Couldn't connect with server");
                    ex.printStackTrace();
                    return;
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                String postData = "login=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                        "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(postDataBytes);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode >= 200 && responseCode < 300) {
                    StringBuilder responseContent = new StringBuilder();
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            responseContent.append(line);
                        }
                    }

                    try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("auth.txt"))) {
                        out.write(responseContent.toString().getBytes());
                        info("Login successfully");
                    } catch (IOException e1) {
                        error("Couldn't save password hash");
                    }
                } else if (responseCode >= 400 && responseCode < 500) {
                    if (responseCode == 400) {
                        error("Wrong form data - no login, email or password");
                    } else if (responseCode == 403) {
                        error("Authentication failed");
                    } else {
                        error("Client status code: " + responseCode);
                    }
                } else {
                    warning("Unknown status code: " + responseCode);
                }

                connection.disconnect();

            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }

            loginStage.close();
        });

        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        Scene scene = new Scene(root, 300, 200);
        loginStage.setScene(scene);
        loginStage.setTitle("Login");

        loginStage.showAndWait();
    }

    public static void showRegisterPopup() {
        Stage registerStage = new Stage();
        registerStage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (Pattern.matches("\\b[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z_+])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9}\\b", email)) {
                Toml toml;
                String serverUrl;

                try {
                    toml = new Toml().read(new File("settings.toml"));
                    serverUrl = toml.getTable("remote").getString("api");
                } catch (Exception ex) {
                    try {
                        URL defaultTomlURL;
                        try {
                            URI uri = new URI("https://raw.githubusercontent.com/KulAndy/tournamentmanager/master/settings.toml");
                            defaultTomlURL = uri.toURL();
                        } catch (URISyntaxException | MalformedURLException ex1) {
                            error("Couldn't connect with server");
                            ex1.printStackTrace();
                            return;
                        }
                        byte[] defaultTomlBytes = Files.readAllBytes(Paths.get(defaultTomlURL.toURI()));
                        String defaultTomlContent = new String(defaultTomlBytes);

                        toml = new Toml().read(defaultTomlContent);
                        serverUrl = toml.getTable("remote").getString("api");
                    } catch (IOException | URISyntaxException ex1) {
                        error("Couldn't read server location");
                        return;
                    }
                }
                try {
                    URL url;
                    try {
                        URI uri = new URI(serverUrl + toml.getTable("remote").getString("register"));
                        url = uri.toURL();
                    } catch (URISyntaxException | MalformedURLException ex) {
                        error("Couldn't connect with server");
                        ex.printStackTrace();
                        return;
                    }

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    String postData = "login=" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8) + "&mail=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

                    try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                        byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
                        outputStream.write(postDataBytes);
                        outputStream.flush();
                    }

                    int statusCode = connection.getResponseCode();

                    if (statusCode >= 200 && statusCode < 300) {
                        info("Successfully registred, go to mail to active account");
                    } else if (statusCode >= 400 && statusCode < 500) {
                        if (statusCode == 400) {
                            error("Wrong form data - no login, email or password");
                        } else if (statusCode == 409) {
                            error("User with this name or email already exists");
                        } else {
                            error("Unknown user error. Status code: " + statusCode);
                        }
                    } else if (statusCode >= 500 && statusCode < 600) {
                        if (statusCode == 500) {
                            error("Internal server error");
                        } else if (statusCode == 520) {
                            error("Couldn't send email with active link");
                        } else {
                            error("Unknown server error. Status code: " + statusCode);
                        }
                    } else {
                        warning("Unknown status code: " + statusCode);
                    }

                    connection.disconnect();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                registerStage.close();
            } else {
                error("Invalid email");
            }
        });

        root.getChildren().addAll(usernameLabel, usernameField, emailLabel, emailField, passwordLabel, passwordField, registerButton);

        Scene scene = new Scene(root, 300, 250);
        registerStage.setScene(scene);
        registerStage.setTitle("Register");

        registerStage.showAndWait();
    }

    static public void showUserTournaments(MainController controller) {
        Toml toml;
        String serverUrl;

        try {
            toml = new Toml().read(new File("settings.toml"));
            serverUrl = toml.getTable("remote").getString("api");
        } catch (Exception ex) {
            try {
                URI uri = new URI("https://raw.githubusercontent.com/KulAndy/tournamentmanager/master/settings.toml");
                URL defaultTomlURL = uri.toURL();
                byte[] defaultTomlBytes = Files.readAllBytes(Paths.get(defaultTomlURL.toURI()));
                String defaultTomlContent = new String(defaultTomlBytes);

                toml = new Toml().read(defaultTomlContent);
                serverUrl = toml.getTable("remote").getString("api");
            } catch (IOException | URISyntaxException ex1) {
                error("Couldn't read server location");
                return;
            }
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost;
        ArrayList<String> lines;
        try {
            lines = (ArrayList<String>) Files.readAllLines(Paths.get("auth.txt"), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            error("You aren't log in or session expired");
            return;
        }


        if (lines.size() >= 1) {
            String token = lines.get(0);
            httpPost = new HttpPost(serverUrl + toml.getTable("remote").getString("logged") + "/" + token);
        } else {
            error("Corrupted auth file");
            return;
        }

        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                        StringBuilder result = new StringBuilder();
                        char[] buffer = new char[1024];
                        int length;
                        while ((length = reader.read(buffer)) != -1) {
                            result.append(buffer, 0, length);
                        }

                        ObservableList<RemoteTournament> remoteTournamentObservableList = FXCollections.observableArrayList();
                        JsonArray jsonArray = JsonParser.parseString(result.toString()).getAsJsonArray();
                        for (JsonElement jsonElement : jsonArray) {
                            try {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();
                                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                                RemoteTournament remoteTournament = new RemoteTournament(
                                        jsonObject.get("_id").getAsString(),
                                        dataObject.get("name").getAsString(),
                                        sdf.parse(dataObject.get("startDate").getAsString()),
                                        sdf.parse(dataObject.get("endDate").getAsString()),
                                        dataObject.get("place").getAsString(),
                                        dataObject.get("gameTime").getAsShort(),
                                        dataObject.get("increment").getAsShort(),
                                        dataObject.get("controlMove").getAsByte(),
                                        dataObject.get("controlAddition").getAsByte(),
                                        Tournament.Type.valueOf(dataObject.get("type").getAsString()),
                                        Tournament.TournamentSystem.valueOf(dataObject.get("system").getAsString())
                                );

                                remoteTournamentObservableList.add(remoteTournament);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }

                        Stage tournamentsStage = new Stage();
                        tournamentsStage.initModality(Modality.APPLICATION_MODAL);
                        VBox root = new VBox(10);
                        root.setPadding(new Insets(20));
                        TableView<RemoteTournament> tableView = new TableView<>();
                        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
                        TableColumn<RemoteTournament, String> name = new TableColumn<>("Name");
                        name.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            return new SimpleStringProperty(remoteTournament.name());
                        });
                        TableColumn<RemoteTournament, String> rate = new TableColumn<>("Rate");
                        rate.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            StringBuilder builder1 = new StringBuilder(remoteTournament.gameTime() + "'");
                            if (remoteTournament.controlMove() > 0 && remoteTournament.controlAddition() > 0) {
                                builder1.append("/")
                                        .append(remoteTournament.controlMove())
                                        .append(" ")
                                        .append(remoteTournament.controlAddition())
                                        .append("'");
                            }

                            if (remoteTournament.increment() > 0) {
                                builder1.append(" + ")
                                        .append(remoteTournament.increment())
                                        .append("''/move");
                            }
                            return new SimpleStringProperty(builder1.toString());
                        });
                        TableColumn<RemoteTournament, String> startDate = new TableColumn<>("Start");
                        startDate.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            return new SimpleStringProperty(dateFormat.format(remoteTournament.startDate()));
                        });
                        TableColumn<RemoteTournament, String> endDate = new TableColumn<>("End");
                        endDate.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            return new SimpleStringProperty(dateFormat.format(remoteTournament.endDate()));
                        });
                        TableColumn<RemoteTournament, String> type = new TableColumn<>("Type");
                        type.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            return new SimpleStringProperty(remoteTournament.type().toString());
                        });
                        TableColumn<RemoteTournament, String> system = new TableColumn<>("System");
                        system.setCellValueFactory(cellDate -> {
                            RemoteTournament remoteTournament = cellDate.getValue();
                            return new SimpleStringProperty(remoteTournament.system().toString());
                        });
                        tableView.getColumns().addAll(name, rate, startDate, endDate, type, system);
                        tableView.setItems(remoteTournamentObservableList);

                        Button download = new Button("Download");
                        String finalServerUrl = serverUrl;
                        Toml finalToml = toml;
                        download.setOnAction(e -> {
                            RemoteTournament remoteTournamentTmp = tableView.getSelectionModel().getSelectedItem();
                            if (remoteTournamentTmp != null) {
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
                                    try {
                                        URL url = new URL(finalServerUrl + finalToml.getTable("remote").getString("tournament") + "/" + remoteTournamentTmp._id());
                                        URLConnection connection = url.openConnection();

                                        try (InputStream inputStream2 = connection.getInputStream();
                                             BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2))) {

                                            StringBuilder content = new StringBuilder();
                                            String line;

                                            while ((line = reader2.readLine()) != null) {
                                                content.append(line).append("\n");
                                            }

                                            String fileContent = content.toString();

                                            JsonObject object = JsonParser.parseString(fileContent).getAsJsonObject();
                                            JsonObject tournamentData = object.get("data").getAsJsonObject();

                                            try {
                                                Path tempDir = Files.createTempDirectory("tempDir");

                                                Path tournamentJsonPath = tempDir.resolve("tournament.json");
                                                Files.write(tournamentJsonPath, tournamentData.toString().getBytes());

                                                Files.copy(Paths.get("settings.toml"), tempDir.resolve("settings.toml"), StandardCopyOption.REPLACE_EXISTING);

                                                String tournamentId = object.get("_id").getAsString();
                                                Files.write(tempDir.resolve("settings.toml"), ("\ntournamentId = \"" + tournamentId + "\"").getBytes(), StandardOpenOption.APPEND);

                                                Path zipFilePath = Paths.get(newFile.getAbsolutePath());
                                                try (OutputStream fos = Files.newOutputStream(zipFilePath);
                                                     ZipOutputStream zipOut = new ZipOutputStream(fos)) {
                                                    Files.walk(tempDir)
                                                            .filter(path -> !Files.isDirectory(path))
                                                            .forEach(path -> {
                                                                ZipEntry zipEntry = new ZipEntry(tempDir.relativize(path).toString());
                                                                try {
                                                                    zipOut.putNextEntry(zipEntry);
                                                                    Files.copy(path, zipOut);
                                                                    zipOut.closeEntry();
                                                                } catch (IOException ioException) {
                                                                    ioException.printStackTrace();
                                                                }
                                                            });
                                                }

                                                importJson(newFile, controller);
                                                info("File downloaded successfully");

                                                Files.walk(tempDir)
                                                        .sorted(Comparator.reverseOrder())
                                                        .map(Path::toFile)
                                                        .forEach(File::delete);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }
                        });
                        Button close = new Button("Close");
                        close.setOnAction(e -> tournamentsStage.close());

                        HBox hBox = new HBox(25);
                        hBox.getChildren().addAll(download, close);

                        root.getChildren().addAll(tableView, hBox);
                        Scene scene = new Scene(root, 1280, 720);
                        tournamentsStage.setScene(scene);
                        tournamentsStage.setTitle("My tournaments");

                        tournamentsStage.showAndWait();

                    }
                } else {
                    error("Error  - no tournament ID returned");
                }
            } else if (statusCode >= 400 && statusCode < 500) {
                error("Corrupted file - couldn't save on server");
            } else if (statusCode >= 500 && statusCode < 600) {
                error("Internal server error");
            } else {
                warning("Unknown status code: " + statusCode);
            }
        } catch (SSLPeerUnverifiedException ex) {
            error("Couldn't connect - insecure connection");
            ex.printStackTrace();
        } catch (IOException ex) {
            error("Connection error");
            ex.printStackTrace();
        }

    }

    public static void showRemoteChessarbiter(MainController controller) {
        Stage tournamentsStage = new Stage();
        tournamentsStage.initModality(Modality.WINDOW_MODAL);
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        HBox hBox = new HBox(25);
        Label label = new Label("Owner");
        TextField field = new TextField();

        hBox.getChildren().addAll(label, field);
        Button close = new Button("Close");
        close.setOnAction(e -> tournamentsStage.close());

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webView.getEngine().locationProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.contains("www.chessarbiter.com/skrcap/capro_get_turniej.php?pobierz=")) {
                Pattern pattern = Pattern.compile("pobierz=(\\d{4})/ti_(\\d+)");
                Matcher matcher = pattern.matcher(newValue);

                if (matcher.find()) {
                    String year = matcher.group(1);
                    String id = matcher.group(2);

                    String filename =  year + "_ti_" + id + ".zip";
                    String remoteDir = "https://www.chessarbiter.com/turnieje/" + year + "/ti_" + id + "/";
                    boolean exists = remoteFileExists(remoteDir + filename);
                    if (!exists){
                        filename =  year + "_ti_" + id + ".swsx.zip";
                        exists = remoteFileExists(remoteDir + filename);
                    }

                    if (!exists){
                        error("File not exists or isn't swsx file");
                    }else {
                        try{
                            downloadZip(remoteDir + filename, filename);
                            String swsxFile = year + "_ti_" + id + ".swsx";
                            SwsxTournament swsxTournament = new SwsxTournament(new File(swsxFile));
                            Tournament tournament = new Tournament(swsxTournament);
                            TournamentOperation.loadTournament(tournament, controller);
                            Files.delete(Path.of(swsxFile));
                            DialogHelper.info("Imported successfully");
                        } catch (IOException | URISyntaxException e) {
                            error("Couldn't download or import file");
                            e.printStackTrace();
                        }
                    }
                }
                String encodedName = URLEncoder.encode(field.getText(), StandardCharsets.UTF_8);
                String url = "https://www.chessarbiter.com/skrcap/capro_get_turnieje.php" +
                        "?expr_wlasciciel=" + encodedName +
                        "&kod=0";
                webEngine.load(url);
            }
        });

        field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String encodedName = URLEncoder.encode(newValue, StandardCharsets.UTF_8);
            String url = "https://www.chessarbiter.com/skrcap/capro_get_turnieje.php" +
                    "?expr_wlasciciel=" + encodedName +
                    "&kod=0";
            webEngine.load(url);
        });

        Button back = new Button("⬅");
        back.setOnAction(e->{
            WebHistory history = webEngine.getHistory();
            if (history.getCurrentIndex() > 1){
                history.go(-1);
            }
        });
        root.getChildren().addAll(hBox,back, webView, close);
        Scene scene = new Scene(root, 1280, 720);
        tournamentsStage.setScene(scene);
        tournamentsStage.setTitle("My tournaments");

        tournamentsStage.showAndWait();
    }

    public static boolean remoteFileExists(String urlString) {
        try {
            URI url = new URI(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.toURL().openConnection();
            httpURLConnection.setRequestMethod("HEAD");

            int responseCode = httpURLConnection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);

        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }

    public static class ProgressMessageBox {
        private final Stage stage;
        private final ProgressBar progressBar;
        private final Label label;

        public ProgressMessageBox(String title) {
            stage = new Stage();
            stage.setTitle(title);

            progressBar = new ProgressBar();
            progressBar.setMinWidth(250);
            progressBar.setProgress(0);

            label = new Label("0%");
            label.setPadding(new Insets(0, 0, 0, 15));

            VBox root = new VBox(20);
            root.setPadding(new javafx.geometry.Insets(20));
            root.getChildren().addAll(progressBar, label);

            Scene scene = new Scene(root, 300, 100);
            stage.setScene(scene);
        }

        public static String convertToTitleCase(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }

            String[] words = input.split("\\s+");
            StringBuilder titleCase = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    String formattedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                    titleCase.append(formattedWord).append(" ");
                }
            }

            return titleCase.toString().trim();
        }

        public void show() {
            stage.show();
        }

        public void setValue(double value) {
            if (value >= 0 && value <= 1.0) {
                progressBar.setProgress(value);
                label.setText("%.2f %%".formatted(value * 100));
                if (value >= 1.0) {
                    stage.close();
                }
            }
        }
    }
}
