package com.example.tournamentmanager.operation;

import com.example.tournamentmanager.helper.DialogHelper;
import com.example.tournamentmanager.model.Federation;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


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
        Connection connection;
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

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
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
            } catch (SQLException ignored) {
            }
        }
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void downloadZip(String url, String filename) throws IOException, URISyntaxException {
        String savePath = "./";

        URI uri = new URI(url);
        URL downloadUrl = uri.toURL();
        InputStream in = new BufferedInputStream(downloadUrl.openStream());
        Path archive = Path.of(savePath + filename);
        Files.copy(in, archive, StandardCopyOption.REPLACE_EXISTING);
        unzipFile(savePath + filename, savePath);
        Files.delete(archive);
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
                DialogHelper.error("An error occurred while downloading");
            }
        } catch (IOException e) {
            DialogHelper.error("An error occurred");
            return;
        }

        try {
            convertCsvToSqlite("rejestr_czlonkow.csv", "rejestr_czlonkow.db");
            DialogHelper.info("Pl list downloaded successfully");
        } catch (RuntimeException e) {
            DialogHelper.error("An error occurred during conversion to SQLite");
        }
    }

    public static String unidecode(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            String normalized = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
            String asciiChar = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            switch (asciiChar) {
                case "ą" -> sb.append("a");
                case "ć" -> sb.append("c");
                case "ę" -> sb.append("e");
                case "ł" -> sb.append("l");
                case "ń" -> sb.append("n");
                case "ó" -> sb.append("o");
                case "ś" -> sb.append("s");
                case "ź", "ż" -> sb.append("z");
                case "Ą" -> sb.append("A");
                case "Ć" -> sb.append("C");
                case "Ę" -> sb.append("E");
                case "Ł" -> sb.append("L");
                case "Ń" -> sb.append("N");
                case "Ó" -> sb.append("O");
                case "Ś" -> sb.append("S");
                case "Ź", "Ż" -> sb.append("Z");
                default -> sb.append(asciiChar);
            }
        }

        return sb.toString();
    }

    public static void updateTomlInZip(File zipFile, String section, String key, String value) {
        try {
            String tempDirPath = "tempDir";
            unzip(zipFile, tempDirPath);

            String settingsTomlPath = tempDirPath + "/settings.toml";
            File tomlFile = new File(settingsTomlPath);

            if (!tomlFile.exists()) {
                File templateFile = new File("settings.toml");
                if (templateFile.exists()) {
                    Files.copy(templateFile.toPath(), tomlFile.toPath());
                } else {
                    String remoteSettingsURL = "https://raw.githubusercontent.com/KulAndy/tournamentmanager/master/settings.toml";
                    try (
                            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(remoteSettingsURL).openStream(), StandardCharsets.UTF_8))
                    ) {
                        StringBuilder content = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine).append("\n");
                        }
                        try (FileWriter fileWriter = new FileWriter(settingsTomlPath);
                             BufferedWriter ou = new BufferedWriter(new FileWriter("settings.toml"))
                        ) {
                            fileWriter.write(content.toString());
                            ou.write(content.toString());
                        }
                    } catch (IOException e) {
                        try (FileWriter fileWriter = new FileWriter(settingsTomlPath)) {
                            fileWriter.write("");
                        }
                    }
                }
            }

            Toml toml = new Toml().read(tomlFile);
            Map<String, Object> tomlMap = toml.toMap();

            Map<String, Object> remoteSection = (Map<String, Object>) tomlMap.get(section);
            if (remoteSection != null) {
                remoteSection.put(key, value);
            } else {
                throw new IllegalArgumentException("Section '" + section + "' not found in settings.toml.");
            }

            TomlWriter writer = new TomlWriter();
            String updatedToml = writer.write(tomlMap);

            FileWriter fileWriter = new FileWriter(settingsTomlPath);
            fileWriter.write(updatedToml);
            fileWriter.close();

            zip(tempDirPath, zipFile);
            deleteDirectory(new File(tempDirPath));
        } catch (IOException ignored) {
        }
    }

    public static void unzip(File zipFile, String destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static void zip(String sourceDirPath, File zipFile) throws IOException {
        Path sourcePath = Paths.get(sourceDirPath);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    public static File newFile(String destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destFile.getCanonicalPath();
        String rootDirPath = new File(destinationDir).getCanonicalPath();

        if (!destDirPath.startsWith(rootDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] entries = directory.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        if (!directory.delete()) {
            System.err.println("Failed to delete " + directory);
        }
    }
}
