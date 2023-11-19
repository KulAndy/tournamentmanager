module com.example.lolmanager {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires jakarta.xml.bind;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires sqlite.jdbc;

    opens com.example.andchess to javafx.fxml, jakarta.xml.bind, com.google.gson;
    opens com.example.andchess.helper to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.andchess.adapter to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.andchess.calculation to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.andchess.operation to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.andchess.model to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.andchess.comparator to com.google.gson, jakarta.xml.bind, javafx.fxml;

    exports com.example.andchess;
    exports com.example.andchess.helper;
    exports com.example.andchess.adapter;
    exports com.example.andchess.calculation;
    exports com.example.andchess.operation;
    exports com.example.andchess.model;
    exports com.example.andchess.comparator;
    exports com.example.andchess.helper.home;
    opens com.example.andchess.helper.home to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.andchess.helper.players;
    opens com.example.andchess.helper.players to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.andchess.helper.round;
    opens com.example.andchess.helper.round to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.andchess.helper.tables;
    opens com.example.andchess.helper.tables to com.google.gson, jakarta.xml.bind, javafx.fxml;
}
