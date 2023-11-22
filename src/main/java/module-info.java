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

    opens com.example.lolmanager to javafx.fxml, jakarta.xml.bind, com.google.gson;
    opens com.example.lolmanager.helper to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.lolmanager.adapter to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.lolmanager.calculation to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.lolmanager.operation to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.lolmanager.model to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.lolmanager.comparator to com.google.gson, jakarta.xml.bind, javafx.fxml;

    exports com.example.lolmanager;
    exports com.example.lolmanager.helper;
    exports com.example.lolmanager.adapter;
    exports com.example.lolmanager.calculation;
    exports com.example.lolmanager.operation;
    exports com.example.lolmanager.model;
    exports com.example.lolmanager.comparator;
    exports com.example.lolmanager.helper.home;
    opens com.example.lolmanager.helper.home to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.lolmanager.helper.players;
    opens com.example.lolmanager.helper.players to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.lolmanager.helper.round;
    opens com.example.lolmanager.helper.round to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.lolmanager.helper.tables;
    opens com.example.lolmanager.helper.tables to com.google.gson, jakarta.xml.bind, javafx.fxml;
}
