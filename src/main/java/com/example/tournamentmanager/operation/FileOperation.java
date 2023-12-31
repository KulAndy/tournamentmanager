package com.example.tournamentmanager.operation;

import com.example.tournamentmanager.helper.GeneralHelper;
import com.example.tournamentmanager.model.Federation;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileOperation {
    private static final int BATCH_SIZE = 10000;

    public FileOperation() {
    }

    public static File selectSwsx() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("swsx files", "*.swsx"));
        return fileChooser.showOpenDialog(new Stage());
    }

    public static String[] searchProvince(Federation countryCode) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + FileOperation.class.getResource("provinces.db");
            connection = DriverManager.getConnection(url);
            String query = "SELECT postal FROM provinces WHERE adm0_a3 like ? ORDER BY postal";
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(countryCode));
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> provinces = new ArrayList<>();
            while (resultSet.next()) {
                String postal = resultSet.getString("postal");

                provinces.add(postal);
            }

            resultSet.close();
            statement.close();
            connection.close();
            return provinces.toArray(new String[provinces.size()]);

        } catch (ClassNotFoundException | SQLException e) {
            return new String[]{};
        }
    }

    public static void unzipFile(String zipFilePath, String destDirectory) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    new File(filePath).getParentFile().mkdirs();
                    extractFile(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static String extractFileToString(ZipInputStream zipIn) throws IOException {
        StringBuilder extractedContent = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = zipIn.read(buffer)) != -1) {
            extractedContent.append(new String(buffer, 0, bytesRead));
        }
        return extractedContent.toString();
    }

    private static String removeLinesWithPattern(String input) {
        return input.replaceAll("<(foa_title|o_title|w_title|games|rapid_games|blitz_games|flag)>.*</(foa_title|o_title|w_title|games|rapid_games|blitz_games|flag)>\n", "");
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
            String extractedContent = extractFileToString(zipIn);
            String contentWithoutPattern = removeLinesWithPattern(extractedContent);
            out.write(contentWithoutPattern.getBytes());
        }
    }

    public static void convertXMLToSQLite(String xmlPath, String dbPath) {
        Connection connection = null;
        File file = new File(dbPath);
        if (file.exists()) {
            file.delete();
        }

        try {
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new BufferedInputStream(new FileInputStream(xmlFile)));

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            connection.setAutoCommit(false);

            createTableFide(connection);
            insertDataFromXml(connection, document);
            connection.commit();

        } catch (ParserConfigurationException | SAXException | FileNotFoundException | SQLException e) {
            rollbackTransaction(connection);
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

    private static void createTableFide(Connection connection) throws SQLException {
        String tableName = "players";
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "fideid TEXT, " +
                "name TEXT, " +
                "country TEXT, " +
                "sex TEXT, " +
                "title TEXT, " +
                "rating INTEGER, " +
                "k INTEGER, " +
                "birthday TEXT" +
                ");";

        String truncateTableQuery = "DELETE FROM " + tableName + ";";
        String createIndex = "CREATE INDEX idx_name ON players(name)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            statement.execute(createIndex);
            statement.execute(truncateTableQuery);
        }
    }

    public static void convertCsvToSqlite(String csvPath, String dbPath) {
        Connection connection = null;
        File file = new File(dbPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTablePol(connection);
            insertDataFromCsv(connection, csvPath);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            rollbackTransaction(connection);
            throw new RuntimeException();
        } finally {
            closeConnection(connection);
        }
    }

    private static void createTablePol(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                "id INT PRIMARY KEY, " +
                "NAZWISKO_IMIE TEXT, " +
                "PLEC TEXT, " +
                "DATA_URODZENIA DATE, " +
                "NR_LICENCJI_ZAW TEXT, " +
                "WZSZACH TEXT, " +
                "ID_FIDE INT, " +
                "ELO INT, " +
                "TYTUL TEXT, " +
                "KLUB TEXT, " +
                "ZN_FOTO TEXT, " +
                "OBYWATELSTWO TEXT, " +
                "_KOL_CZL_R_FIDE_SZ INT, " +
                "_KOL_CZL_R_FIDE_BL INT, " +
                "formatted TEXT" +
                ")";
        connection.createStatement().executeUpdate(createTableSQL);
        String createIndex = "CREATE INDEX idx_formatted ON players(formatted)";
        String truncateTableQuery = "DELETE FROM players;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            statement.execute(createIndex);
            statement.execute(truncateTableQuery);
        }
    }

    private static void insertDataFromCsv(Connection connection, String csvFilePath) throws SQLException {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFilePath)))) {
            br.readLine(); // Skip header line
            String insertSQL = "INSERT INTO players VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            int batchSizeLimit = 1000; // Number of records per batch
            int batchSize = 0;

            connection.setAutoCommit(false); // Start a transaction

            while ((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    String formattedNAZWISKO_IMIE = unidecode(data[1]);

                    preparedStatement.setInt(1, Integer.parseInt(data[0]));
                    preparedStatement.setString(2, data[1].isBlank() ? null : data[1]);
                    preparedStatement.setString(3, data[2].isBlank() ? null : data[2]);
                    preparedStatement.setString(4, data[3].isBlank() ? null : data[3]);
                    preparedStatement.setString(5, data[4].isBlank() ? null : data[4]);
                    preparedStatement.setString(6, data[5].isBlank() ? null : data[5]);
                    preparedStatement.setObject(7, data[6].isBlank() ? null : Integer.parseInt(data[6]));
                    preparedStatement.setObject(8, data[7].isBlank() ? null : Integer.parseInt(data[7]));
                    preparedStatement.setString(9, data[8].isBlank() ? null : data[8]);
                    preparedStatement.setString(10, data[9].isBlank() ? null : data[9]);
                    preparedStatement.setString(11, data[10].isBlank() ? null : data[10]);
                    preparedStatement.setString(12, data[11].isBlank() ? null : data[11]);
                    preparedStatement.setObject(13, data[12].isBlank() ? null : Integer.parseInt(data[12]));
                    preparedStatement.setObject(14, data[13].isBlank() ? null : Integer.parseInt(data[13]));
                    preparedStatement.setString(15, formattedNAZWISKO_IMIE);

                    preparedStatement.addBatch();
                    batchSize++;

                    if (batchSize % batchSizeLimit == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                        batchSize = 0;
                    }
                } catch (SQLException | NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }

            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void insertDataFromXml(Connection connection, Document document) throws SQLException {
        NodeList playerList = document.getElementsByTagName("player");
        String insertQuery = "INSERT INTO players (fideid, name, country, sex, title, rating, k, birthday) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            int count = 0;

            for (int i = 0; i < playerList.getLength(); i++) {
                Element playerElement = (Element) playerList.item(i);

                String fideid = getElementValue(playerElement, "fideid");
                String name = getElementValue(playerElement, "name");
                String country = getElementValue(playerElement, "country");
                String sex = getElementValue(playerElement, "sex");
                String title = getElementValue(playerElement, "title");
                String rating = getElementValue(playerElement, "rating");
                String k = getElementValue(playerElement, "k");
                String birthday = getElementValue(playerElement, "birthday");

                statement.setString(1, fideid);
                statement.setString(2, name);
                statement.setString(3, country);
                statement.setString(4, sex);
                statement.setString(5, title);
                statement.setInt(6, Integer.parseInt(rating));
                statement.setInt(7, Integer.parseInt(k));
                statement.setString(8, birthday);

                statement.addBatch();
                count++;

                if (count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }

            statement.executeBatch();
        }
    }

    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private static void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downloadPolList() {
        String server = "51.68.11.200";
        String username = "crpzszac-rnk";
        String password = "EgccmwerW";
        String remoteFilePath = "/rejestr_czlonkow.csv";
        String localFilePath = "./rejestr_czlonkow.csv";

        String ftpUrl = String.format("ftp://%s:%s@%s%s", username, password, server, remoteFilePath);

        Set<String> uniqueLines = new HashSet<>(); // Set to store unique lines

        try {
            URL url = new URL(ftpUrl);
            URLConnection connection = url.openConnection();

            try (InputStream in = new BufferedInputStream(connection.getInputStream());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("ISO-8859-2")));
                 FileWriter writer = new FileWriter(localFilePath);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer)
            ) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (uniqueLines.add(line)) { // add returns true for new lines
                        bufferedWriter.write(line + "\n");
                    }
                }
            } catch (IOException e) {
                GeneralHelper.error("An error occurred while downloading");
                e.printStackTrace();
            }
        } catch (IOException e) {
            GeneralHelper.error("An error occurred");
            e.printStackTrace();
            return;
        }

        try {
            convertCsvToSqlite("rejestr_czlonkow.csv", "rejestr_czlonkow.db");
            GeneralHelper.info("Pl list downloaded successfully");
        } catch (RuntimeException e) {
            GeneralHelper.error("An error occurred during conversion to SQLite");
            e.printStackTrace();
        }
    }

    public static String unidecode(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            String normalized = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
            String asciiChar = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            switch (asciiChar) {
                case "ą":
                    sb.append("a");
                    break;
                case "ć":
                    sb.append("c");
                    break;
                case "ę":
                    sb.append("e");
                    break;
                case "ł":
                    sb.append("l");
                    break;
                case "ń":
                    sb.append("n");
                    break;
                case "ó":
                    sb.append("o");
                    break;
                case "ś":
                    sb.append("s");
                    break;
                case "ź":
                    sb.append("z");
                    break;
                case "ż":
                    sb.append("z");
                    break;
                case "Ą":
                    sb.append("A");
                    break;
                case "Ć":
                    sb.append("C");
                    break;
                case "Ę":
                    sb.append("E");
                    break;
                case "Ł":
                    sb.append("L");
                    break;
                case "Ń":
                    sb.append("N");
                    break;
                case "Ó":
                    sb.append("O");
                    break;
                case "Ś":
                    sb.append("S");
                    break;
                case "Ź", "Ż":
                    sb.append("Z");
                    break;
                default:
                    sb.append(asciiChar);
                    break;
            }
        }

        return sb.toString();
    }
}