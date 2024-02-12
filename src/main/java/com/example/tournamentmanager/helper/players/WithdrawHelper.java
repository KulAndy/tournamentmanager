package com.example.tournamentmanager.helper.players;

import com.example.tournamentmanager.comparator.StartListComparator;
import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Tournament;
import com.example.tournamentmanager.model.Withdraw;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.example.tournamentmanager.helper.GeneralHelper.setupComboBox;

public class WithdrawHelper {
    private Tournament tournament;
    private ComboBox<Player> withdrawPlayerSelect;
    private ComboBox<Withdraw.WithdrawType> withdrawTypeSelect;
    private TextField withdrawRound;
    private Button acceptWithdrawButton;
    private TableView<Withdraw> withdrawTable;
    private TableColumn<Withdraw, Integer> withdrawNoCol;
    private TableColumn<Withdraw, String> withdrawNameCol;
    private TableColumn<Withdraw, Withdraw.WithdrawType> withdrawTypeCol;
    private TableColumn<Withdraw, Byte> withdrawRoundCol;
    private TableColumn<Withdraw, Void> withdrawBackCol;

    public WithdrawHelper(
            Tournament tournament,
            ComboBox<Player> withdrawPlayerSelect, ComboBox<Withdraw.WithdrawType> withdrawTypeSelect,
            TextField withdrawRound, Button acceptWithdrawButton, TableView<Withdraw> withdrawTable,
            TableColumn<Withdraw, Integer> withdrawNoCol, TableColumn<Withdraw, String> withdrawNameCol,
            TableColumn<Withdraw, Withdraw.WithdrawType> withdrawTypeCol, TableColumn<Withdraw, Byte> withdrawRoundCol,
            TableColumn<Withdraw, Void> withdrawBackCol
    ) {
        setTournament(tournament);
        setWithdrawPlayerSelect(withdrawPlayerSelect);
        setWithdrawTypeSelect(withdrawTypeSelect);
        setWithdrawRound(withdrawRound);
        setAcceptWithdrawButton(acceptWithdrawButton);
        setWithdrawTable(withdrawTable);
        setWithdrawNoCol(withdrawNoCol);
        setWithdrawNameCol(withdrawNameCol);
        setWithdrawTypeCol(withdrawTypeCol);
        setWithdrawRoundCol(withdrawRoundCol);
        setWithdrawBackCol(withdrawBackCol);

        getWithdrawPlayerSelect().setCellFactory(param -> new ListCell<>() {
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
        getWithdrawPlayerSelect().setButtonCell(new ListCell<>() {
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

        getAcceptWithdrawButton().setOnAction(e -> {
            Player player = getWithdrawPlayerSelect().getValue();
            Byte roundNo = null;
            try {
                roundNo = Byte.parseByte(getWithdrawRound().getText());
            } catch (NumberFormatException ignored) {
            }
            if (roundNo != null && roundNo <= 0) {
                roundNo = null;
            }
            if (player != null) {
                if (getWithdrawTypeSelect().getValue() != Withdraw.WithdrawType.TOURNAMENT || !getTournament().isTournamentWithdraw(player)) {
                    getTournament().getWithdrawsObs().add(
                            new Withdraw(
                                    player,
                                    getWithdrawTypeSelect().getValue(),
                                    roundNo
                            )
                    );
                }
            }
        });

        getWithdrawTypeSelect().setOnAction(e -> {
            if (getWithdrawTypeSelect().getValue() == Withdraw.WithdrawType.TOURNAMENT) {
                if (!(getWithdrawRound().isDisable())) {
                    getWithdrawRound().setText("");
                    getWithdrawRound().setDisable(true);
                }
            } else {
                if (getWithdrawRound().isDisable()) {
                    getWithdrawRound().setText(String.valueOf(tournament.getRounds().size() + 1));
                    getWithdrawRound().setDisable(false);
                }
            }
        });

        getWithdrawNoCol().setCellValueFactory(cellData -> {
            Withdraw withdraw = cellData.getValue();
            int rowIndex = getWithdrawTable().getItems().indexOf(withdraw) + 1;
            return new SimpleIntegerProperty(rowIndex).asObject();
        });
        getWithdrawNameCol().setCellValueFactory(cellData -> {
            Withdraw withdraw = cellData.getValue();
            return new SimpleStringProperty(withdraw.getPlayer().getName());
        });
        getWithdrawTypeCol().setCellValueFactory(new PropertyValueFactory<>("type"));
        getWithdrawRoundCol().setCellValueFactory(new PropertyValueFactory<>("roundNo"));

        getWithdrawBackCol().setCellFactory(param -> new TableCell<>() {
            private final Button backButton = new Button("Bring back");

            {
                backButton.setOnAction(event -> {
                    Withdraw withdraw = getTableRow().getItem();
                    if (withdraw != null) {
                        getTournament().getWithdrawsObs().remove(withdraw);
                        getTournament().getWithdraws().remove(withdraw);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(backButton);
                } else {
                    setGraphic(null);
                }
            }
        });


        SortedList<Player> sortedList = new SortedList<>(getTournament().getPlayersObs());
        StartListComparator comparator = new StartListComparator();
        comparator.setCriteria1(StartListComparator.SortCriteria.ALPHABETIC);
        sortedList.setComparator(comparator);
        getWithdrawPlayerSelect().setItems(sortedList);
        setupComboBox(getWithdrawTypeSelect(), Withdraw.WithdrawType.values());
        getWithdrawTable().setItems(getTournament().getWithdrawsObs());

    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public ComboBox<Player> getWithdrawPlayerSelect() {
        return withdrawPlayerSelect;
    }

    public void setWithdrawPlayerSelect(ComboBox<Player> withdrawPlayerSelect) {
        this.withdrawPlayerSelect = withdrawPlayerSelect;
    }

    public ComboBox<Withdraw.WithdrawType> getWithdrawTypeSelect() {
        return withdrawTypeSelect;
    }

    public void setWithdrawTypeSelect(ComboBox<Withdraw.WithdrawType> withdrawTypeSelect) {
        this.withdrawTypeSelect = withdrawTypeSelect;
    }

    public TextField getWithdrawRound() {
        return withdrawRound;
    }

    public void setWithdrawRound(TextField withdrawRound) {
        this.withdrawRound = withdrawRound;
    }

    public Button getAcceptWithdrawButton() {
        return acceptWithdrawButton;
    }

    public void setAcceptWithdrawButton(Button acceptWithdrawButton) {
        this.acceptWithdrawButton = acceptWithdrawButton;
    }

    public TableView<Withdraw> getWithdrawTable() {
        return withdrawTable;
    }

    public void setWithdrawTable(TableView<Withdraw> withdrawTable) {
        this.withdrawTable = withdrawTable;
    }

    public TableColumn<Withdraw, Integer> getWithdrawNoCol() {
        return withdrawNoCol;
    }

    public void setWithdrawNoCol(TableColumn<Withdraw, Integer> withdrawNoCol) {
        this.withdrawNoCol = withdrawNoCol;
    }

    public TableColumn<Withdraw, String> getWithdrawNameCol() {
        return withdrawNameCol;
    }

    public void setWithdrawNameCol(TableColumn<Withdraw, String> withdrawNameCol) {
        this.withdrawNameCol = withdrawNameCol;
    }

    public TableColumn<Withdraw, Withdraw.WithdrawType> getWithdrawTypeCol() {
        return withdrawTypeCol;
    }

    public void setWithdrawTypeCol(TableColumn<Withdraw, Withdraw.WithdrawType> withdrawTypeCol) {
        this.withdrawTypeCol = withdrawTypeCol;
    }

    public TableColumn<Withdraw, Byte> getWithdrawRoundCol() {
        return withdrawRoundCol;
    }

    public void setWithdrawRoundCol(TableColumn<Withdraw, Byte> withdrawRoundCol) {
        this.withdrawRoundCol = withdrawRoundCol;
    }

    public TableColumn<Withdraw, Void> getWithdrawBackCol() {
        return withdrawBackCol;
    }

    public void setWithdrawBackCol(TableColumn<Withdraw, Void> withdrawBackCol) {
        this.withdrawBackCol = withdrawBackCol;
    }
}
