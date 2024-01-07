package com.example.tournamentmanager.helper.players;

import com.example.tournamentmanager.calculation.PZSzachCalculation;
import com.example.tournamentmanager.model.*;
import com.example.tournamentmanager.operation.FIDEOperation;
import com.example.tournamentmanager.operation.FileOperation;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

import static com.example.tournamentmanager.helper.GeneralHelper.*;

public class NewPlayerHelper {
    private Tournament tournament;
    private ComboBox<Player> playerSelect;
    private ComboBox<Federation> fedSelect;
    private ComboBox<String> stateSelect;
    private TextField playerNameField;
    private ComboBox<Title> playerTitleSelect;
    private TextField localRtgField;
    private TextField FIDERtgField;
    private TextField clubField;
    private TextField dayOfBirth;
    private TextField monthOfBirth;
    private TextField yearOfBirth;
    private ComboBox<Player.Sex> sexSelect;
    private TextField mailField;
    private ComboBox<Short> phonePrefixSelect;
    private TextField phoneNumber;
    private TextField localIDField;
    private TextField FIDEIDField;
    private TextField remarksField;
    private Button addPlayerButton;

    private Button updatePlayerBth;
    private Button clearPlayerButton;
    private Button addClearPlayerButton;
    private Button insertFromList;
    private ListView<Player> newPlayerHint;

    public NewPlayerHelper(
            Tournament tournament,
            ComboBox<Player> playerSelect,
            ComboBox<Federation> fedSelect, ComboBox<String> stateSelect, TextField playerNameField,
            ComboBox<Title> playerTitleSelect, TextField localRtgField, TextField FIDERtgField,
            TextField clubField, TextField dayOfBirth, TextField monthOfBirth, TextField yearOfBirth,
            ComboBox<Player.Sex> sexSelect, TextField mailField, ComboBox<Short> phonePrefixSelect,
            TextField phoneNumber, TextField localIDField, TextField FIDEIDField, TextField remarksField,
            Button addPlayerButton, Button updatePlayerBth, Button clearPlayerButton, Button addClearPlayerButton,
            Button insertFromList, ListView<Player> newPlayerHint
    ) {
        setTournament(tournament);
        setPlayerSelect(playerSelect);
        setFedSelect(fedSelect);
        setStateSelect(stateSelect);
        setPlayerNameField(playerNameField);
        setPlayerTitleSelect(playerTitleSelect);
        setLocalRtgField(localRtgField);
        setFIDERtgField(FIDERtgField);
        setClubField(clubField);
        setDayOfBirth(dayOfBirth);
        setMonthOfBirth(monthOfBirth);
        setYearOfBirth(yearOfBirth);
        setSexSelect(sexSelect);
        setMailField(mailField);
        setPhonePrefixSelect(phonePrefixSelect);
        setPhoneNumber(phoneNumber);
        setLocalIDField(localIDField);
        setFIDEIDField(FIDEIDField);
        setRemarksField(remarksField);
        setAddPlayerButton(addPlayerButton);
        setUpdatePlayerBth(updatePlayerBth);
        setClearPlayerButton(clearPlayerButton);
        setAddClearPlayerButton(addClearPlayerButton);
        setInsertFromList(insertFromList);
        setNewPlayerHint(newPlayerHint);

        getPlayerSelect().setItems(getTournament().getPlayersObs());
        getPlayerSelect().setOnAction(e -> {
            Player player = getPlayerSelect().getValue();
            if (player != null) {
                getFedSelect().setValue(player.getFederation());
                String state = player.getState();
                getStateSelect().setValue(state.trim().length() == 0 ? null : state);
                getPlayerNameField().setText(player.getName());
                getPlayerTitleSelect().setValue(player.getTitle());
                getLocalRtgField().setText(String.valueOf(player.getLocalRating()));
                getFIDERtgField().setText(String.valueOf(player.getFideRating()));
                getClubField().setText(player.getClub());
                getDayOfBirth().setText(String.valueOf(player.getDayOfBirth()));
                getMonthOfBirth().setText(String.valueOf(player.getMonthOfBirth()));
                getYearOfBirth().setText(String.valueOf(player.getYearOfBirth()));
                getSexSelect().setValue(player.getSex());
                getMailField().setText(player.getEMail());
                getPhonePrefixSelect().setValue(player.getPhonePrefix());
                String phone = String.valueOf(player.getPhoneNumber());
                getPhoneNumber().setText(phone == null ? "" : phone);
                getLocalIDField().setText(String.valueOf(player.getLocalId()));
                getFIDEIDField().setText(String.valueOf(player.getFideId()));
                getRemarksField().setText(player.getRemarks());
            }
        });
        getPlayerSelect().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        getPlayerSelect().setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        getNewPlayerHint().setCellFactory(new Callback<>() {
            @Override
            public ListCell<Player> call(ListView<Player> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Player item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            String text =
                                    "%-4s\t".formatted(item.getFederation() != null ? item.getFederation() : "")
                                            + "%-75s\t".formatted(item.getName() != null ? item.getName() : "")
                                            + "%-9d\t".formatted(item.getLocalId() != null ? item.getLocalId() : 0)
                                            + "%-9d\t".formatted(item.getFideId() != null ? item.getFideId() : 0)
                                            + "%-3s\t".formatted(item.getTitle() != null ? item.getTitle() : "")
                                            + "%-4d\t".formatted(item.getFideRating() != null ? item.getFideRating() : 0)
                                            + "%-10s\t".formatted(item.getDateOfBirth() != null ? item.getDateOfBirth() : "0000-00-00")
                                            + "%s\t".formatted(item.getSex() != null ? item.getSex() : "");
                            setText(text);
                        }
                    }
                };
            }
        });

        setupComboBox(getFedSelect(), Federation.values());
        getFedSelect().valueProperty().addListener((ObservableValue<? extends Federation> observable, Federation oldValue, Federation newValue) -> {
            if (newValue != null) {
                String[] provinces = FileOperation.searchProvince(newValue);
                getStateSelect().getItems().clear();
                setupComboBox(getStateSelect(), provinces);
                getStateSelect().setValue(null);
            }
        });
        getFedSelect().setValue(Federation.POL);

        setupComboBox(getPlayerTitleSelect(), Title.values());
        getPlayerTitleSelect().valueProperty().addListener((ObservableValue<? extends Title> observable, Title oldValue, Title newValue) -> {
            if (newValue != null) {
                localRtgField.setText(String.valueOf(PZSzachCalculation.getTitleValue(newValue, sexSelect.getValue())));
            }
        });
        getPlayerTitleSelect().setValue(Title.bk);


        validateTextFieldInt(getLocalRtgField());
        validateTextFieldInt(getFIDERtgField());
        validateTextFieldInt(getDayOfBirth());
        validateTextFieldInt(getMonthOfBirth());
        validateTextFieldInt(getYearOfBirth());


        setupComboBox(getSexSelect(), Player.Sex.values());
        getSexSelect().setValue(null);

        setupComboBox(getPhonePrefixSelect(), Player.getPhonePrefixesList());
        getPhonePrefixSelect().setValue((short) 48);

        validateTextFieldInt(getLocalIDField());
        validateTextFieldInt(getFIDEIDField());

        getNewPlayerHint().setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");

        getPlayerNameField().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                CompletableFuture.supplyAsync(() -> FIDEOperation.searchPlayer(newValue.trim(), tournament.getType()))
                        .thenAcceptAsync(players -> Platform.runLater(() -> {
                            ObservableList<Player> options = FXCollections.observableArrayList(players);
                            options.sort(Comparator.comparing(player -> player.getName().replaceAll("[\\s,]", ""), String.CASE_INSENSITIVE_ORDER));
                            newPlayerHint.setItems(options);
                        }))
                        .exceptionallyAsync(ex -> {
                            Platform.runLater(() -> {
                                ex.printStackTrace();
                                System.out.println(ex.getMessage());
                            });
                            return null;
                        });
            } catch (Exception ignored) {
            }
        });

        getInsertFromList().setOnAction(e -> {
            if (newPlayerHint.getSelectionModel().getSelectedItem() != null) {
                Player selected = newPlayerHint.getSelectionModel().getSelectedItem();
                Federation federation = (selected.getFederation() == null) ? Federation.FIDE : selected.getFederation();
                String name = selected.getName();
                String fideid = String.valueOf(selected.getFideId());
                Title title = (selected.getTitle() == null) ? Title.bk : selected.getTitle();
                String rating = String.valueOf(selected.getFideRating());
                String club = selected.getClub();
                String birthdayYear = "";
                String birthdayMonth = "";
                String birthdayDay = "";
                String dateOfBirth = selected.getDateOfBirth();
                if (dateOfBirth != null) {
                    birthdayYear = dateOfBirth.substring(0, 4);
                    birthdayMonth = dateOfBirth.substring(5, 7);
                    birthdayDay = dateOfBirth.substring(8);
                }

                Player.Sex sex = selected.getSex();
                String localID = "";
                try {
                    localID = String.valueOf(selected.getLocalId());
                } catch (Exception ignored) {
                }

                getFedSelect().setValue(federation);
                getPlayerNameField().setText(name);
                getFIDEIDField().setText(fideid);
                if (title != Title.bk) {
                    getPlayerTitleSelect().setValue(title);
                }
                getFIDERtgField().setText(rating);
                getClubField().setText(club);
                if (!birthdayYear.isEmpty()) {
                    getYearOfBirth().setText(birthdayYear);
                }
                if (!birthdayMonth.isEmpty() && !(Integer.parseInt(birthdayMonth) == 0)) {
                    getMonthOfBirth().setText(birthdayMonth);
                }
                if (!birthdayDay.isEmpty() && !(Integer.parseInt(birthdayDay) == 0)) {
                    getDayOfBirth().setText(birthdayDay);
                }
                getSexSelect().setValue(sex);
                if (!localID.isEmpty()) {
                    getLocalIDField().setText(localID);
                }
            }
        });

        getAddPlayerButton().setOnAction(event -> addPlayer());
        getUpdatePlayerBth().setOnAction(event -> {
            Player player = getPlayerSelect().getValue();
            if (player == null) {
                error("No player selected");
            } else {
                int localRtg;
                try {
                    localRtg = Integer.parseInt(getLocalRtgField().getText());
                } catch (Exception e) {
                    localRtg = 0;
                }
                int fideRtg;
                try {
                    fideRtg = Integer.parseInt(getFIDERtgField().getText());
                } catch (Exception e) {
                    fideRtg = 0;
                }
                Integer phone;
                try {
                    phone = Integer.parseInt(getPhoneNumber().getText());
                } catch (Exception e) {
                    phone = null;
                }
                String date;
                if (getYearOfBirth().getText().isEmpty()) {
                    date = null;
                } else {
                    date = getYearOfBirth().getText() + "-" + getMonthOfBirth().getText() + "-" + getDayOfBirth().getText();
                }
                Integer localID;
                try {
                    localID = Integer.parseInt(getLocalIDField().getText());
                } catch (Exception e) {
                    localID = null;
                }
                Integer fideID;
                try {
                    fideID = Integer.parseInt(getFIDEIDField().getText());
                } catch (Exception e) {
                    fideID = null;
                }

                String name = getPlayerNameField().getText();

                int counter = 1;
                Player found = getTournament().getPlayers().get(name);
                while (found != null && found != player) {
                    name = getPlayerNameField().getText() + " " + ++counter;
                    found = getTournament().getPlayers().get(name);
                }
                getPlayerNameField().setText(name);

                player.setFederation(getFedSelect().getValue());
                player.setState(getStateSelect().getValue());
                player.setName(getPlayerNameField().getText());
                player.setTitle(getPlayerTitleSelect().getValue());
                player.setLocalRating(localRtg);
                player.setFideRating(fideRtg);
                player.setClub(getClubField().getText());
                player.setDateOfBirth(date);
                player.setSex(getSexSelect().getValue());
                player.setEMail(getMailField().getText());
                player.setPhonePrefix(getPhonePrefixSelect().getValue());
                player.setPhoneNumber(phone);
                player.setLocalId(localID);
                player.setFideId(fideID);
                player.setRemarks(getRemarksField().getText());
            }
        });

        getClearPlayerButton().setOnAction(event -> resetForm());
        getAddClearPlayerButton().setOnAction(event -> {
            addPlayer();
            resetForm();
        });

    }

    public void resetForm() {
        getPlayerSelect().setValue(null);
        getFedSelect().setValue(Federation.POL);
        getStateSelect().setValue(null);
        getPlayerNameField().setText("");
        getPlayerTitleSelect().setValue(Title.bk);
        getLocalRtgField().setText("");
        getFIDERtgField().setText("");
        getClubField().setText("");
        getDayOfBirth().setText("");
        getMonthOfBirth().setText("");
        getYearOfBirth().setText("");
        getSexSelect().setValue(null);
        getMailField().setText("");
        getPhonePrefixSelect().setValue((short) 48);
        getPhoneNumber().setText("");
        getLocalIDField().setText("");
        getFIDEIDField().setText("");
        getRemarksField().setText("");
    }

    private void addPlayer() {
        int localRtg;
        try {
            localRtg = Integer.parseInt(getLocalRtgField().getText());
        } catch (Exception e) {
            localRtg = 0;
        }
        int fideRtg;
        try {
            fideRtg = Integer.parseInt(getFIDERtgField().getText());
        } catch (Exception e) {
            fideRtg = 0;
        }
        Integer phone;
        try {
            phone = Integer.parseInt(getPhoneNumber().getText());
        } catch (Exception e) {
            phone = null;
        }
        String date;
        if (getYearOfBirth().getText().isEmpty()) {
            date = null;
        } else {
            date = getYearOfBirth().getText() + "-" + getMonthOfBirth().getText() + "-" + getDayOfBirth().getText();
        }
        Integer localID;
        try {
            localID = Integer.parseInt(getLocalIDField().getText());
        } catch (Exception e) {
            localID = null;
        }
        Integer fideID;
        try {
            fideID = Integer.parseInt(getFIDEIDField().getText());
        } catch (Exception e) {
            fideID = null;
        }

        String name = getPlayerNameField().getText();

        int counter = 1;
        Player found = getTournament().getPlayers().get(name);
        while (found != null) {
            name = getPlayerNameField().getText() + " " + ++counter;
            found = getTournament().getPlayers().get(name);
        }

        Player player = new Player(
                getFedSelect().getValue(),
                getStateSelect().getValue() == null ? "" : getStateSelect().getValue(),
                name,
                getPlayerTitleSelect().getValue(),
                localRtg >= 1000 ? localRtg : PZSzachCalculation.getTitleValue(getPlayerTitleSelect().getValue(), getSexSelect().getValue()),
                Math.max(fideRtg, 1000),
                getClubField().getText(),
                date,
                getSexSelect().getValue(),
                getMailField().getText(),
                (phone != null && phone > 0) ? getPhonePrefixSelect().getValue() : null,
                phone,
                localID,
                fideID,
                getRemarksField().getText()
        );
        for (int i = 0; i < getTournament().getRoundsObs().size(); i++) {
            player.addRound(new Game(player, getTournament().getPlayers().getUnpaired()));
        }
        getTournament().getPlayersObs().add(player);
        if (counter > 1) {
            info("player with that name already exists - was added as " + name);
        }

    }


    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public ComboBox<Federation> getFedSelect() {
        return fedSelect;
    }

    public void setFedSelect(ComboBox<Federation> fedSelect) {
        this.fedSelect = fedSelect;
    }

    public ComboBox<String> getStateSelect() {
        return stateSelect;
    }

    public void setStateSelect(ComboBox<String> stateSelect) {
        this.stateSelect = stateSelect;
    }

    public TextField getPlayerNameField() {
        return playerNameField;
    }

    public void setPlayerNameField(TextField playerNameField) {
        this.playerNameField = playerNameField;
    }

    public ComboBox<Title> getPlayerTitleSelect() {
        return playerTitleSelect;
    }

    public void setPlayerTitleSelect(ComboBox<Title> playerTitleSelect) {
        this.playerTitleSelect = playerTitleSelect;
    }

    public TextField getLocalRtgField() {
        return localRtgField;
    }

    public void setLocalRtgField(TextField localRtgField) {
        this.localRtgField = localRtgField;
    }

    public TextField getFIDERtgField() {
        return FIDERtgField;
    }

    public void setFIDERtgField(TextField FIDERtgField) {
        this.FIDERtgField = FIDERtgField;
    }

    public TextField getClubField() {
        return clubField;
    }

    public void setClubField(TextField clubField) {
        this.clubField = clubField;
    }

    public TextField getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(TextField dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public TextField getMonthOfBirth() {
        return monthOfBirth;
    }

    public void setMonthOfBirth(TextField monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public TextField getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(TextField yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public ComboBox<Player.Sex> getSexSelect() {
        return sexSelect;
    }

    public void setSexSelect(ComboBox<Player.Sex> sexSelect) {
        this.sexSelect = sexSelect;
    }

    public TextField getMailField() {
        return mailField;
    }

    public void setMailField(TextField mailField) {
        this.mailField = mailField;
    }

    public ComboBox<Short> getPhonePrefixSelect() {
        return phonePrefixSelect;
    }

    public void setPhonePrefixSelect(ComboBox<Short> phonePrefixSelect) {
        this.phonePrefixSelect = phonePrefixSelect;
    }

    public TextField getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(TextField phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public TextField getLocalIDField() {
        return localIDField;
    }

    public void setLocalIDField(TextField localIDField) {
        this.localIDField = localIDField;
    }

    public TextField getFIDEIDField() {
        return FIDEIDField;
    }

    public void setFIDEIDField(TextField FIDEIDField) {
        this.FIDEIDField = FIDEIDField;
    }

    public TextField getRemarksField() {
        return remarksField;
    }

    public void setRemarksField(TextField remarksField) {
        this.remarksField = remarksField;
    }

    public Button getAddPlayerButton() {
        return addPlayerButton;
    }

    public void setAddPlayerButton(Button addPlayerButton) {
        this.addPlayerButton = addPlayerButton;
    }

    public Button getClearPlayerButton() {
        return clearPlayerButton;
    }

    public void setClearPlayerButton(Button clearPlayerButton) {
        this.clearPlayerButton = clearPlayerButton;
    }

    public Button getAddClearPlayerButton() {
        return addClearPlayerButton;
    }

    public void setAddClearPlayerButton(Button addClearPlayerButton) {
        this.addClearPlayerButton = addClearPlayerButton;
    }

    public Button getInsertFromList() {
        return insertFromList;
    }

    public void setInsertFromList(Button insertFromList) {
        this.insertFromList = insertFromList;
    }

    public ListView<Player> getNewPlayerHint() {
        return newPlayerHint;
    }

    public void setNewPlayerHint(ListView<Player> newPlayerHint) {
        this.newPlayerHint = newPlayerHint;
    }

    public ComboBox<Player> getPlayerSelect() {
        return playerSelect;
    }

    public void setPlayerSelect(ComboBox<Player> playerSelect) {
        this.playerSelect = playerSelect;
    }

    public Button getUpdatePlayerBth() {
        return updatePlayerBth;
    }

    public void setUpdatePlayerBth(Button updatePlayerBth) {
        this.updatePlayerBth = updatePlayerBth;
    }

}
