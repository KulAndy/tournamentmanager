package com.example.tournamentmanager.helper;

import com.example.tournamentmanager.model.Tournament;
import com.moandjiezana.toml.Toml;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class GeneralHelper {
    public static void bindTextFieldStringProperty(TextField tf, Object obj, String attr) {
        tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null) {
                try {
                    Method setter = getSetter(obj, attr, String.class);
                    setter.invoke(obj, newValue);
                    logTournamentValue(obj, attr);
                } catch (Exception ignored) {
                }
            }
        });
    }

    public static void bindTextFieldInt(TextField tf, Object obj, String attr, String type) {
        try {
            Method setter = getSetter(obj, attr, typeToClass(type));

            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    if (newValue.isEmpty()) {
                        setter.invoke(obj, convertToType("0", type));
                    } else {
                        int valueInt = Integer.parseInt(newValue);
                        if (valueInt <= getMaxValue(type)) {
                            tf.setText(String.valueOf(valueInt));
                            setter.invoke(obj, convertToType(String.valueOf(valueInt), type));
                        } else {
                            tf.setText(String.valueOf(getMaxValue(type)));
                            setter.invoke(obj, convertToType(String.valueOf(getMaxValue(type)), type));
                        }
                    }
                    logTournamentValue(obj, attr);
                } catch (Exception ignored) {
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static void bindTextFieldFloat(TextField tf, Object obj, String attr) {
        try {
            Method setter = getSetter(obj, attr, Float.class);
            tf.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    if (newValue.isEmpty()) {
                        setter.invoke(obj, 0F);
                    } else {
                        float valueF;
                        try {
                            valueF = Float.parseFloat(newValue);
                        } catch (Exception e) {
                            valueF = Float.parseFloat(newValue.replace(',', '.'));
                        }
                        if (valueF < 0) {
                            tf.setText("0");
                            setter.invoke(obj, valueF);
                        } else {
                            setter.invoke(obj, valueF);
                        }
                    }
                    logTournamentValue(obj, attr);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void bindDatePicker(DatePicker dp, Object obj, String attr) {
        dp.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                try {
                    Method setter = getSetter(obj, attr, LocalDate.class);
                    setter.invoke(obj, newValue);
                    logTournamentValue(obj, attr);
                } catch (Exception ignored) {
                }
            }
        });

    }

    public static <T extends Enum<T>> void bindComboBox(ComboBox<T> cb, Object obj, String attr, Class<T> enumClass) {
        cb.valueProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
            if (newValue != null) {
                try {
                    Method setter = getSetter(obj, attr, enumClass);
                    setter.invoke(obj, newValue);
                    logTournamentValue(obj, attr);
                } catch (Exception ignored) {
                }

            }
        });
    }

    public static void bindCheckBoxRated(CheckBox cb, Tournament tournament, String attr) {
        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue != null) {
                try {
                    Method setter = getSetter(tournament.getRating(), attr, Boolean.class);
                    setter.invoke(tournament.getRating(), newValue);
                    logTournamentValue(tournament.getRating(), attr);
                } catch (Exception ignored) {
                }
            }
        });
    }

    public static void validateTextFieldInt(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && (newText.isEmpty() || Integer.parseInt(newText) >= 0)) {
                return change;
            }
            return null;
        }));


    }

    public static void validateTextFieldFloat(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*[.,]?\\d*") && (newText.isEmpty() || Float.parseFloat(newText.replace(',', '.')) >= 0)) {
                return change;
            }
            return null;
        }));
    }

    public static <T extends Enum<T>> void setupComboBox(ComboBox<T> cb, T[] enumValues) {
        cb.getItems().addAll(enumValues);
        if (enumValues.length > 0) {
            cb.setValue(enumValues[0]);
        }
    }

    public static <T> void setupComboBox(ComboBox<T> cb, T[] enumValues) {
        cb.getItems().addAll(enumValues);
        if (enumValues.length > 0) {
            cb.setValue(enumValues[0]);
        }
    }


    private static Class<?> typeToClass(String type) {
        return switch (type) {
            case "byte" -> byte.class;
            case "short" -> short.class;
            default -> int.class;
        };
    }

    private static Object convertToType(String value, String type) {
        return switch (type) {
            case "byte" -> Byte.parseByte(value);
            case "short" -> Short.parseShort(value);
            default -> Integer.parseInt(value);
        };
    }

    private static int getMaxValue(String type) {
        return switch (type) {
            case "byte" -> Byte.MAX_VALUE;
            case "short" -> Short.MAX_VALUE;
            default -> Integer.MAX_VALUE;
        };
    }

    static void logTournamentValue(Object obj, String attr) {
        Method getter;
        try {
            getter = obj.getClass().getMethod("get" + Character.toUpperCase(attr.charAt(0)) + attr.substring(1));
            System.out.printf("tournament %s: %s\n", attr, getter.invoke(obj));
        } catch (NoSuchMethodException e) {
            try {
                getter = obj.getClass().getMethod("is" + Character.toUpperCase(attr.charAt(0)) + attr.substring(1));
                System.out.printf("tournament %s: %s\n", attr, getter.invoke(obj));
            } catch (Exception ignored) {
            }
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    public static Method getSetter(Object obj, String attr, Class<?> c) throws NoSuchMethodException {
        return obj.getClass().getMethod("set" + Character.toUpperCase(attr.charAt(0)) + attr.substring(1), c);
    }

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
                    URI uri = new URI(serverUrl + "login");
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
                        out.write((username + "\n").getBytes());
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
                        URI uri = new URI(serverUrl + "register");
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
