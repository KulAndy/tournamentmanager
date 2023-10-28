package com.example.lolmanager.helper;

import com.example.lolmanager.operation.FileOperation;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

import static com.example.lolmanager.MainController.quit;

public class ShortcutsHelper {
    private Scene scene;
    private FileOperation fileOperation;
    private RoundsHelper roundsHelper;
    private Tab roundsTab;
    private Tab enterResultsTab;

    public ShortcutsHelper(Scene scene, FileOperation fileOperation, RoundsHelper roundsHelper, Tab roundsTab, Tab enterResultsTab) {
        setScene(scene);
        setFileOperation(fileOperation);
        setRoundsHelper(roundsHelper);
        setRoundsTab(roundsTab);
        setEnterResultsTab(enterResultsTab);
        addShortcuts();
    }

    private void addShortcuts() {
        scene.setOnKeyPressed((KeyEvent e) -> {
            controlShortcuts(e);
            controlShiftShortcuts(e);
            resultEnterShortcuts(e);
//            System.out.println("kod: " + e.getCode());
//            System.out.println("ctrl: " + e.isControlDown());
//            System.out.println("alt: " + e.isAltDown());
//            System.out.println("shift: " + e.isShiftDown());
//            System.out.println("meta: " + e.isMetaDown());
        });
    }

    private void controlShortcuts(KeyEvent e) {
        if (e.isControlDown()) {
            switch (e.getCode()) {
                case S -> fileOperation.save();
                case O -> fileOperation.open();
                case Q -> quit();
            }
        }
    }

    private void controlShiftShortcuts(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown()) {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.S) {
                fileOperation.saveAs();
            }
        }
    }

    private void resultEnterShortcuts(KeyEvent e) {
        if (enterResultsTab != null && roundsTab != null && enterResultsTab.isSelected() && roundsTab.isSelected()) {
            switch (e.getCode()) {
                case Z -> getRoundsHelper().getResultEnterHelper().enterResult("1", "0");
                case X -> getRoundsHelper().getResultEnterHelper().enterResult("0.5", "0.5");
                case C -> getRoundsHelper().getResultEnterHelper().enterResult("0", "1");
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public FileOperation getFileOperation() {
        return fileOperation;
    }

    public void setFileOperation(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }

    public RoundsHelper getRoundsHelper() {
        return roundsHelper;
    }

    public void setRoundsHelper(RoundsHelper roundsHelper) {
        this.roundsHelper = roundsHelper;
    }

    public Tab getRoundsTab() {
        return roundsTab;
    }

    public void setRoundsTab(Tab roundsTab) {
        this.roundsTab = roundsTab;
    }

    public Tab getEnterResultsTab() {
        return enterResultsTab;
    }

    public void setEnterResultsTab(Tab enterResultsTab) {
        this.enterResultsTab = enterResultsTab;
    }

}
