package com.example.andchess.helper.round;

import com.example.andchess.model.Game;
import com.example.andchess.model.Player;
import com.example.andchess.model.Tournament;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class AllRoundsViewerHelper {
    private ScrollPane allRoundsScroll;
    private Tournament tournament;
    private VBox container = new VBox();

    public AllRoundsViewerHelper(Tournament tournament, ScrollPane allRoundsScroll) {
        setAllRoundsScroll(allRoundsScroll);
        setTournament(tournament);

        getContainer().prefWidthProperty().bind(getAllRoundsScroll().widthProperty());

        getAllRoundsScroll().setContent(getContainer());

        getTournament().getRoundsObs().addListener((ListChangeListener<? super ArrayList<Game>>) change -> {
            getContainer().getChildren().clear();
            for (int i = 0; i < getTournament().getRoundsObs().size(); i++) {
                Text text = new Text("Round " + (i + 1));
                text.setTextAlignment(TextAlignment.CENTER);
                HBox textContainer = new HBox(text);
                textContainer.setAlignment(Pos.CENTER);
                getContainer().getChildren().add(textContainer);
                ArrayList<Game> round = getTournament().getRoundsObs().get(i);
                TableView<Game> table = new TableView<>(FXCollections.observableArrayList(round));
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn<Game, Integer> leftBoardNo = new TableColumn<>("#");
                centerAlignColumnInt(leftBoardNo);
                leftBoardNo.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    int rowIndex = table.getItems().indexOf(game) + 1;
                    return new SimpleIntegerProperty(rowIndex).asObject();
                });
                int finalI = i;
                TableColumn<Game, Float> whitePoints = new TableColumn<>("Points");
                centerAlignColumnFloat(whitePoints);
                whitePoints.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player white = game.getWhite();
                    return new SimpleFloatProperty(white.getPointInRound(finalI)).asObject();
                });

                TableColumn<Game, Integer> whiteRating = new TableColumn<>("Rating");
                centerAlignColumnInt(whiteRating);
                whiteRating.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player white = game.getWhite();
                    return new SimpleIntegerProperty(white.getFideRating()).asObject();
                });

                TableColumn<Game, String> whitePlayer = new TableColumn<>("White");
                whitePlayer.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player white = game.getWhite();
                    return new SimpleStringProperty(white.getName());
                });

                TableColumn<Game, String> result = new TableColumn<>("Result");
                centerAlignColumnStr(result);
                result.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    StringBuilder resultString = new StringBuilder();
                    if (game.getWhiteResult() != null) {
                        switch (game.getWhiteResult()) {
                            case WIN -> {
                                if (game.isForfeit()) {
                                    resultString.append("+");
                                } else {
                                    resultString.append("1");
                                }
                            }
                            case DRAW -> resultString.append("0.5");
                            case LOSE -> {
                                if (game.isForfeit()) {
                                    resultString.append("-");
                                } else {
                                    resultString.append("0");
                                }
                            }
                        }
                    }
                    resultString.append(" - ");
                    if (game.getBlackResult() != null) {
                        switch (game.getBlackResult()) {
                            case WIN -> {
                                if (game.isForfeit()) {
                                    resultString.append("+");
                                } else {
                                    resultString.append("1");
                                }
                            }
                            case DRAW -> resultString.append("0.5");
                            case LOSE -> {
                                if (game.isForfeit()) {
                                    resultString.append("-");
                                } else {
                                    resultString.append("0");
                                }
                            }
                        }
                    }
                    return new SimpleStringProperty(resultString.toString());
                });

                TableColumn<Game, String> blackPlayer = new TableColumn<>("White");
                blackPlayer.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player black = game.getBlack();
                    return new SimpleStringProperty(black.getName());
                });

                TableColumn<Game, Integer> blackRating = new TableColumn<>("Rating");
                centerAlignColumnInt(blackRating);
                blackRating.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player black = game.getBlack();
                    return new SimpleIntegerProperty(black.getFideRating()).asObject();
                });

                TableColumn<Game, Float> blackPoints = new TableColumn<>("Points");
                centerAlignColumnFloat(blackPoints);
                int finalI1 = i;
                blackPoints.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    Player black = game.getBlack();
                    return new SimpleFloatProperty(black.getPointInRound(finalI1)).asObject();
                });


                TableColumn<Game, Integer> rightBoardNo = new TableColumn<>("#");
                centerAlignColumnInt(rightBoardNo);
                rightBoardNo.setCellValueFactory(cellData -> {
                    Game game = cellData.getValue();
                    int rowIndex = table.getItems().indexOf(game) + 1;
                    return new SimpleIntegerProperty(rowIndex).asObject();
                });

                table.getColumns().addAll(leftBoardNo, whitePoints, whiteRating, whitePlayer, result, blackPlayer, blackRating, blackPoints, rightBoardNo);
                getContainer().getChildren().add(table);
            }
        });

    }

    private void centerAlignColumnInt(TableColumn<Game, Integer> column) {
        column.setCellFactory(tc -> new CenterAlignedTableCell<>());
    }

    private void centerAlignColumnFloat(TableColumn<Game, Float> column) {
        column.setCellFactory(tc -> new CenterAlignedTableCell<>());
    }

    private void centerAlignColumnStr(TableColumn<Game, String> column) {
        column.setCellFactory(tc -> new CenterAlignedTableCell<>());
    }

    public ScrollPane getAllRoundsScroll() {
        return allRoundsScroll;
    }

    public void setAllRoundsScroll(ScrollPane allRoundsScroll) {
        this.allRoundsScroll = allRoundsScroll;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public VBox getContainer() {
        return container;
    }

    public void setContainer(VBox container) {
        this.container = container;
    }

    private static class CenterAlignedTableCell<S, T> extends TableCell<S, T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.toString());
                setAlignment(Pos.CENTER);
            }
        }
    }

}
