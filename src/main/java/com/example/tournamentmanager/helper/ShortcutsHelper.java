package com.example.tournamentmanager.helper;

import com.example.tournamentmanager.MainController;
import com.example.tournamentmanager.model.Game;
import com.example.tournamentmanager.model.Player;
import com.example.tournamentmanager.model.Result;
import com.example.tournamentmanager.model.Tournament;
import com.example.tournamentmanager.operation.FileOperation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.tournamentmanager.MainController.quit;
import static com.example.tournamentmanager.helper.GeneralHelper.ProgressMessageBox.convertToTitleCase;
import static com.example.tournamentmanager.helper.GeneralHelper.*;
import static com.example.tournamentmanager.operation.TournamentOperation.*;

public class ShortcutsHelper {
    private final MainController controller;
    private Scene scene;
    private FileOperation fileOperation;
    private RoundsHelper roundsHelper;

    public ShortcutsHelper(Scene scene, FileOperation fileOperation, RoundsHelper roundsHelper, MainController controller) {
        setScene(scene);
        setFileOperation(fileOperation);
        setRoundsHelper(roundsHelper);
        this.controller = controller;
        addShortcuts();
    }

    public static void printPDF(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (PDDocument document = PDDocument.load(file)) {
                PrinterJob job = PrinterJob.getPrinterJob();

                PrintService printService = job.getPrintService();
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

                if (printService == null) {
                    warning("No printer selected.");
                } else {
                    attributes.add(new Copies(1));
                    attributes.add(MediaSizeName.ISO_A4);
                    job.setPrintService(printService);
                    job.setPageable(new PDFPageable(document));
                    job.setPrintable(new PDFPrintable(document));

                    if (job.printDialog(attributes)) {
                        job.print(attributes);
                        info("Printed successfully!");
                    } else {
                        warning("Printing canceled by user.");
                    }
                }
            } catch (IOException | PrinterException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException ignored) {
        }
    }

    private void addShortcuts() {
        scene.setOnKeyPressed((KeyEvent e) -> {
            controlShortcuts(e);
            controlShiftShortcuts(e);
            resultEnterShortcuts(e);
        });
    }

    private void controlShortcuts(KeyEvent e) {
        if (e.isControlDown() && !e.isShiftDown()) {
            switch (e.getCode()) {
                case N -> {
                        try {
                            save(controller);
                            loadTournament(new Tournament(), controller);
                            controller.getTournamentSelect().setValue(null);
                        } catch (IOException ex) {
                            error("Couldn't save tournament");
                        }
                }
                case S -> {
                    try {
                        save(controller);
                    } catch (IOException ex) {
                        error("Couldn't save tournament");
                    }
                }
                case O -> open(controller);
                case Q -> quit();
                case P -> print();
            }
        }
    }

    public void print() {
        if (controller.getPlayersTab() != null && controller.getPlayersTab().isSelected() && controller.getStartListTab() != null && controller.getStartListTab().isSelected()) {
            ArrayList<Player> players = new ArrayList<>(controller.getPlayersHelper().getStartListHelper().getPlayersListTable().getItems());
            Platform.runLater(() -> printPDF(playersList2pdf(players, PrintMode.START_LIST)));
        } else if (controller.getTablesTab() != null && controller.getTablesTab().isSelected() && controller.getResultsTab() != null && controller.getResultsTab().isSelected()) {
            ArrayList<Player> players = new ArrayList<>(controller.getTablesHelper().getResultTableHelper().getResultsTable().getItems());
            Platform.runLater(() -> printPDF(playersList2pdf(players, PrintMode.RESULTS)));
        } else if (controller.getEnterResultsTab() != null && controller.getRoundsTab() != null && controller.getEnterResultsTab().isSelected() && controller.getRoundsTab().isSelected()) {
            ArrayList<Game> games = new ArrayList<>(controller.getRoundsHelper().getResultEnterHelper().getGamesView().getItems());
            int round = controller.getRoundsHelper().getResultEnterHelper().getRoundsViewSelect().getValue();
            Platform.runLater(() -> printPDF(gamesList2pdf(games, round)));
        } else {
            warning("This page isn't printable");
        }
    }

    private void controlShiftShortcuts(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown()) {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.S) {
                try {
                    saveAs(controller);
                } catch (IOException ex) {
                    error("Couldn't save tournament");
                }
            }
        }
    }

    private void resultEnterShortcuts(KeyEvent e) {
        if (controller.getEnterResultsTab() != null && controller.getRoundsTab() != null && controller.getEnterResultsTab().isSelected() && controller.getRoundsTab().isSelected()) {
            switch (e.getCode()) {
                case Z -> getRoundsHelper().getResultEnterHelper().enterResult('z');
                case X -> getRoundsHelper().getResultEnterHelper().enterResult('x');
                case C -> getRoundsHelper().getResultEnterHelper().enterResult('c');
            }
        }
    }

    public String gamesList2pdf(ArrayList<Game> games, int round) {
        String filename = "print.pdf";
        try {
            Document document = new Document();
            document.setMargins(5, 5, 5, 5);
            document.setPageSize(PageSize.A4);
            OutputStream outputStream = new FileOutputStream(filename);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph header = new Paragraph(controller.getTournament().getName(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph paragraph = new Paragraph("Round " + round, normalFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(new Paragraph("\n"));
            PdfPTable pdfPTable = new PdfPTable(9);
            pdfPTable.setWidths(new int[]{10, 10, 10, 30, 15, 30, 10, 10, 10});
            PdfPHeaderCell headerLeftNo = new PdfPHeaderCell();
            headerLeftNo.addElement(new Phrase("#"));
            headerLeftNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerLeftNo.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerWhitePoints = new PdfPHeaderCell();
            headerWhitePoints.addElement(new Phrase("Pt"));
            headerWhitePoints.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerWhitePoints.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerWhiteRating = new PdfPHeaderCell();
            headerWhiteRating.addElement(new Phrase("Rating"));
            headerWhiteRating.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerWhiteRating.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerWhite = new PdfPHeaderCell();
            headerWhite.addElement(new Phrase("White"));
            headerWhite.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerWhite.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerResult = new PdfPHeaderCell();
            headerResult.addElement(new Phrase("Result"));
            headerResult.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerResult.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerBlack = new PdfPHeaderCell();
            headerBlack.addElement(new Phrase("Black"));
            headerBlack.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerBlack.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerBlackRating = new PdfPHeaderCell();
            headerBlackRating.addElement(new Phrase("Rating"));
            headerBlackRating.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerBlackRating.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerBlackPoints = new PdfPHeaderCell();
            headerBlackPoints.addElement(new Phrase("Pt"));
            headerBlackPoints.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerBlackPoints.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPHeaderCell headerRightNo = new PdfPHeaderCell();
            headerRightNo.addElement(new Phrase("#"));
            headerRightNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerRightNo.setVerticalAlignment(Element.ALIGN_CENTER);

            pdfPTable.addCell(headerLeftNo);
            pdfPTable.addCell(headerWhitePoints);
            pdfPTable.addCell(headerWhiteRating);
            pdfPTable.addCell(headerWhite);
            pdfPTable.addCell(headerResult);
            pdfPTable.addCell(headerBlack);
            pdfPTable.addCell(headerBlackRating);
            pdfPTable.addCell(headerBlackPoints);
            pdfPTable.addCell(headerRightNo);

            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                pdfPTable.addCell(String.valueOf(i + 1));
                pdfPTable.addCell(String.valueOf(game.getWhite().getPointInRound(round - 1)));
                pdfPTable.addCell(String.valueOf(game.getWhite().getFideRating()));
                pdfPTable.addCell(game.getWhite().getName());
                PdfPCell cell = new PdfPCell(new Phrase(Result.getResultString(game.getWhiteResult(), game.isForfeit()) + " - " + Result.getResultString(game.getBlackResult(), game.isForfeit())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(cell);
                pdfPTable.addCell(game.getBlack().getName());
                pdfPTable.addCell(String.valueOf(game.getBlack().getFideRating()));
                pdfPTable.addCell(String.valueOf(game.getBlack().getPointInRound(round - 1)));
                pdfPTable.addCell(String.valueOf(i + 1));

            }

            pdfPTable.setComplete(true);
            pdfPTable.completeRow();
            pdfPTable.setSpacingBefore(0);
            pdfPTable.setSpacingAfter(0);
            pdfPTable.setWidthPercentage(100);
            document.add(pdfPTable);
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String playersList2pdf(ArrayList<Player> players, PrintMode printMode) {
        String filename = "print.pdf";
        try {
            Document document = new Document();
            document.setMargins(5, 5, 5, 5);
            document.setPageSize(PageSize.A4);
            OutputStream outputStream = new FileOutputStream(filename);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph header = new Paragraph(controller.getTournament().getName(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("\n"));
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph paragraph = new Paragraph(printMode.toString(), normalFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(new Paragraph("\n"));
            PdfPTable pdfPTable = new PdfPTable(10);
            switch (printMode) {
                case RESULTS -> {
                    pdfPTable = new PdfPTable(12);
                    pdfPTable.setWidths(new int[]{10, 10, 10, 30, 15, 15, 15, 15, 15, 15, 15, 15});
                    PdfPHeaderCell headerPlace = new PdfPHeaderCell();
                    headerPlace.addElement(new Phrase("#"));
                    headerPlace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerPlace.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerSno = new PdfPHeaderCell();
                    headerSno.addElement(new Phrase("S.No"));
                    headerSno.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerSno.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTitle = new PdfPHeaderCell();
                    headerTitle.addElement(new Phrase("Title"));
                    headerTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTitle.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerName = new PdfPHeaderCell();
                    headerName.addElement(new Phrase("Name"));
                    headerName.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerName.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerFide = new PdfPHeaderCell();
                    headerFide.addElement(new Phrase("Fide"));
                    headerFide.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerFide.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerLocal = new PdfPHeaderCell();
                    headerLocal.addElement(new Phrase("Local"));
                    headerLocal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerLocal.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerFED = new PdfPHeaderCell();
                    headerFED.addElement(new Phrase("FED"));
                    headerFED.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerFED.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTb1 = new PdfPHeaderCell();
                    headerTb1.addElement(new Phrase(controller.getTournament().getResultsComparator().getCriteria1().prettyText()));
                    headerTb1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTb1.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTb2 = new PdfPHeaderCell();
                    headerTb2.addElement(new Phrase(controller.getTournament().getResultsComparator().getCriteria2().prettyText()));
                    headerTb2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTb2.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTb3 = new PdfPHeaderCell();
                    headerTb3.addElement(new Phrase(controller.getTournament().getResultsComparator().getCriteria3().prettyText()));
                    headerTb3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTb3.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTb4 = new PdfPHeaderCell();
                    headerTb4.addElement(new Phrase(controller.getTournament().getResultsComparator().getCriteria4().prettyText()));
                    headerTb4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTb4.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTb5 = new PdfPHeaderCell();
                    headerTb5.addElement(new Phrase(controller.getTournament().getResultsComparator().getCriteria5().prettyText()));
                    headerTb5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTb5.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(headerPlace);
                    pdfPTable.addCell(headerSno);
                    pdfPTable.addCell(headerTitle);
                    pdfPTable.addCell(headerName);
                    pdfPTable.addCell(headerFide);
                    pdfPTable.addCell(headerLocal);
                    pdfPTable.addCell(headerFED);
                    pdfPTable.addCell(headerTb1);
                    pdfPTable.addCell(headerTb2);
                    pdfPTable.addCell(headerTb3);
                    pdfPTable.addCell(headerTb4);
                    pdfPTable.addCell(headerTb5);
                    for (Player player : players) {
                        pdfPTable.addCell(String.valueOf(players.indexOf(player) + 1));
                        pdfPTable.addCell(String.valueOf(controller.getTournament().getPlayers().indexOf(player) + 1));
                        pdfPTable.addCell(String.valueOf(player.getTitle()));
                        pdfPTable.addCell(player.getName());
                        pdfPTable.addCell(String.valueOf(player.getFideRating()));
                        pdfPTable.addCell(String.valueOf(player.getLocalRating()));
                        pdfPTable.addCell(String.valueOf(player.getFederation()));
                        pdfPTable.addCell(player.getTiebreak(controller.getTournament().getResultsComparator().getCriteria1()).toString());
                        pdfPTable.addCell(player.getTiebreak(controller.getTournament().getResultsComparator().getCriteria2()).toString());
                        pdfPTable.addCell(player.getTiebreak(controller.getTournament().getResultsComparator().getCriteria3()).toString());
                        pdfPTable.addCell(player.getTiebreak(controller.getTournament().getResultsComparator().getCriteria4()).toString());
                        pdfPTable.addCell(player.getTiebreak(controller.getTournament().getResultsComparator().getCriteria5()).toString());
                    }
                }
                case START_LIST -> {
                    pdfPTable = new PdfPTable(7);
                    pdfPTable.setWidths(new int[]{10, 10, 35, 10, 15, 15, 50});
                    PdfPHeaderCell headerSno = new PdfPHeaderCell();
                    headerSno.addElement(new Phrase("S.No"));
                    headerSno.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerSno.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerTitle = new PdfPHeaderCell();
                    headerTitle.addElement(new Phrase("Title"));
                    headerTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerTitle.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerName = new PdfPHeaderCell();
                    headerName.addElement(new Phrase("Name"));
                    headerName.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerName.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerFED = new PdfPHeaderCell();
                    headerFED.addElement(new Phrase("FED"));
                    headerFED.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerFED.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerFide = new PdfPHeaderCell();
                    headerFide.addElement(new Phrase("Fide"));
                    headerFide.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerFide.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerLocal = new PdfPHeaderCell();
                    headerLocal.addElement(new Phrase("Local"));
                    headerLocal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerLocal.setVerticalAlignment(Element.ALIGN_CENTER);
                    PdfPHeaderCell headerClub = new PdfPHeaderCell();
                    headerClub.addElement(new Phrase("Club"));
                    headerClub.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerClub.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(headerSno);
                    pdfPTable.addCell(headerTitle);
                    pdfPTable.addCell(headerName);
                    pdfPTable.addCell(headerFED);
                    pdfPTable.addCell(headerFide);
                    pdfPTable.addCell(headerLocal);
                    pdfPTable.addCell(headerClub);
                    for (Player player : players) {
                        pdfPTable.addCell(String.valueOf(players.indexOf(player) + 1));
                        pdfPTable.addCell(String.valueOf(player.getTitle()));
                        pdfPTable.addCell(player.getName());
                        pdfPTable.addCell(String.valueOf(player.getFederation()));
                        pdfPTable.addCell(String.valueOf(player.getFideRating()));
                        pdfPTable.addCell(String.valueOf(player.getLocalRating()));
                        pdfPTable.addCell(player.getClub());
                    }
                }
            }
            pdfPTable.setComplete(true);
            pdfPTable.completeRow();
            pdfPTable.setSpacingBefore(0);
            pdfPTable.setSpacingAfter(0);
            pdfPTable.setWidthPercentage(100);
            document.add(pdfPTable);
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
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

    public enum PrintMode {
        START_LIST,
        RESULTS;

        @Override
        public String toString() {
            return convertToTitleCase(super.toString().replace('_', ' '));
        }
    }
}
