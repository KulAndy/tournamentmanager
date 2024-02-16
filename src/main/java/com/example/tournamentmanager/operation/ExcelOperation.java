package com.example.tournamentmanager.operation;

import com.example.tournamentmanager.model.Title;
import com.example.tournamentmanager.model.Tournament;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.tournamentmanager.helper.DialogHelper.error;

public class ExcelOperation {
    private static final Stage fileStage = new Stage();

    public static void createApplication(Tournament tournament, String programName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create New File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("register files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(fileStage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            file = new File(filePath);

            try (
                    Workbook workbook = new XSSFWorkbook()
            ) {
                int rowsNum = 0;
                Sheet sheet = workbook.createSheet("Sheet1");
                addRow(sheet, rowsNum++, "Event Name", tournament.getName());
                addRow(sheet, rowsNum++, "City", tournament.getPlace());
                addRow(sheet, rowsNum++, "FIDE Laws to be followed ", true);
                addRow(sheet, rowsNum++, "National Championship 1.43a", false);
                addRow(sheet, rowsNum++, "FIDE Calendar", false);
                addRow(sheet, rowsNum++, "Tournament report", "All rounds in 1 report");
                addRow(sheet, rowsNum++, "Expected number of players", 50);
                addRow(sheet, rowsNum++, "Tournament system", tournament.getSystem());
                addRow(sheet, rowsNum++, "Number of Rounds reported", tournament.getRoundsNumber());
                addRow(sheet, rowsNum++, "Number of multiple round days", 1);
                addRow(sheet, rowsNum++, "Female players only", false);
                addRow(sheet, rowsNum++, "Start Date (YYYY-MM-DD)", tournament.getStartDate());
                addRow(sheet, rowsNum++, "End Date (YYYY-MM-DD)", tournament.getEndDate());
                addRow(sheet, rowsNum++, "Title Norms available",
                        tournament.getRating().getMaxTitle() == Title.GM
                                || tournament.getRating().getMaxTitle() == Title.IM
                                || tournament.getRating().getMaxTitle() == Title.WGM
                                || tournament.getRating().getMaxTitle() == Title.WIM
                );
                addRow(sheet, rowsNum++, "GM/WGM Norms avaliable",
                        tournament.getRating().getMaxTitle() == Title.GM
                                || tournament.getRating().getMaxTitle() == Title.WGM
                );
                addRow(sheet, rowsNum++, "Chief Arbiter", tournament.getArbiter());
                addRow(sheet, rowsNum++, "Chief Organizer", tournament.getOrganizer());
                addRow(sheet, rowsNum++, "Time Control", tournament.getType());
                StringBuilder controlTime = new StringBuilder();
                controlTime.append(tournament.getGameTime()).append(" min");
                if (tournament.getControlMove() > 0) {
                    controlTime.append("/").append(tournament.getControlMove()).append(" moves")
                            .append(" + ").append(tournament.getControlAddition()).append("/end");
                }
                if (tournament.getIncrement() > 0) {
                    controlTime.append(" + ").append(tournament.getIncrement()).append("sec increment per move starting from move 1");
                }
                addRow(sheet, rowsNum++, "Time Control Description", controlTime.toString());

                addRow(sheet, rowsNum++, "Max rating /empty value - means no maximum/ ", "");
                addRow(sheet, rowsNum++, "Age limit", "NONE");
                addRow(sheet, rowsNum++, "All digital clocks", true);
                addRow(sheet, rowsNum++, "Internet transmission", false);
                Tournament.Tiebreak.TbMethod tbMethod;
                if (tournament.getTiebreak().getTiebreak1() == Tournament.Tiebreak.TbMethod.POINTS) {
                    if (tournament.getTiebreak().getTiebreak2() == Tournament.Tiebreak.TbMethod.POINTS) {
                        if (tournament.getTiebreak().getTiebreak3() == Tournament.Tiebreak.TbMethod.POINTS) {
                            if (tournament.getTiebreak().getTiebreak4() == Tournament.Tiebreak.TbMethod.POINTS) {
                                if (tournament.getTiebreak().getTiebreak5() == Tournament.Tiebreak.TbMethod.POINTS) {
                                    tbMethod = Tournament.Tiebreak.TbMethod.BUCHOLZ;
                                } else {
                                    tbMethod = tournament.getTiebreak().getTiebreak5();
                                }
                            } else {
                                tbMethod = tournament.getTiebreak().getTiebreak4();
                            }
                        } else {
                            tbMethod = tournament.getTiebreak().getTiebreak3();
                        }
                    } else {
                        tbMethod = tournament.getTiebreak().getTiebreak2();
                    }
                } else {
                    tbMethod = tournament.getTiebreak().getTiebreak1();
                }
                addRow(sheet, rowsNum++, "Tiebreak method", tbMethod.toString());
                addRow(sheet, rowsNum++, "Software", "OTHER");
                addRow(sheet, rowsNum++, "", programName);
                addRow(sheet, rowsNum++, "Contact Email", tournament.getEmail());
                addRow(sheet, rowsNum++, "Internet homepage", "");
                addRow(sheet, rowsNum++, "Prize Fund", "");
                addRow(sheet, rowsNum, "Remarks", "");

                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                try (
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos)
                ) {
                    workbook.write(bos);
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                error("Nie można utworzyć dokumentu");
            }
        }
    }

    public static void addRow(Sheet sheet, int rowNo, String key, Tournament.Type value) {
        addRow(sheet, rowNo, key, value.toString());
    }

    public static void addRow(Sheet sheet, int rowNo, String key, Tournament.TournamentSystem value) {
        addRow(sheet, rowNo, key, value.toString());
    }

    public static void addRow(Sheet sheet, int rowNo, String key, boolean value) {
        addRow(sheet, rowNo, key, value ? "YES" : "NO");
    }

    public static void addRow(Sheet sheet, int rowNo, String key, int value) {
        addRow(sheet, rowNo, key, String.valueOf(value));
    }

    public static void addRow(Sheet sheet, int rowNo, String key, Date value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        addRow(sheet, rowNo, key, sdf.format(value));
    }

    public static void addRow(Sheet sheet, int rowNo, String key, String value) {
        Row row = sheet.createRow(rowNo);
        Cell keyCell = row.createCell(0);
        keyCell.setCellValue(key);
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
    }

}
