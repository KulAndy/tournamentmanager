package com.example.andchess.helper;

import com.example.andchess.model.Tournament;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
            alert.setContentText(content);
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

    public static class ProgressMessageBox {
        private final Stage stage;
        private final ProgressBar progressBar;
        private final Label label;

        public ProgressMessageBox(String title, double maxValue) {
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
}
