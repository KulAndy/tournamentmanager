package com.example.lolmanager.helper;

import com.example.lolmanager.operation.FileOperation;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

import static com.example.lolmanager.MainController.quit;

public class ShortcutsHelper {
    private Scene scene;
    private FileOperation fileOperation;

    public ShortcutsHelper(Scene scene, FileOperation fileOperation) {
        setScene(scene);
        setFileOperation(fileOperation);
        addShortcuts();
    }

    private void addShortcuts() {
        scene.setOnKeyPressed((KeyEvent e) -> {
            controlShortcuts(e);
            System.out.println("kod: " + e.getCode());
            System.out.println("ctrl: " + e.isControlDown());
            System.out.println("alt: " + e.isAltDown());
            System.out.println("shift: " + e.isShiftDown());
            System.out.println("meta: " + e.isMetaDown());
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

}
