module com.example.tournamentmanager {
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
    requires itextpdf;
    requires kernel;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpmime;
    requires toml4j;
    requires org.mongodb.bson;
    requires org.jetbrains.annotations;

    opens com.example.tournamentmanager to javafx.fxml, jakarta.xml.bind, com.google.gson;
    opens com.example.tournamentmanager.helper to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.tournamentmanager.adapter to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.tournamentmanager.calculation to com.google.gson, jakarta.xml.bind, javafx.fxml;
    opens com.example.tournamentmanager.operation to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.tournamentmanager.model to com.google.gson, jakarta.xml.bind, javafx.fxml, javafx.base;
    opens com.example.tournamentmanager.comparator to com.google.gson, jakarta.xml.bind, javafx.fxml;

    exports com.example.tournamentmanager;
    exports com.example.tournamentmanager.helper;
    exports com.example.tournamentmanager.adapter;
    exports com.example.tournamentmanager.calculation;
    exports com.example.tournamentmanager.operation;
    exports com.example.tournamentmanager.model;
    exports com.example.tournamentmanager.comparator;
    exports com.example.tournamentmanager.helper.home;
    opens com.example.tournamentmanager.helper.home to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.tournamentmanager.helper.players;
    opens com.example.tournamentmanager.helper.players to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.tournamentmanager.helper.round;
    opens com.example.tournamentmanager.helper.round to com.google.gson, jakarta.xml.bind, javafx.fxml;
    exports com.example.tournamentmanager.helper.tables;
    opens com.example.tournamentmanager.helper.tables to com.google.gson, jakarta.xml.bind, javafx.fxml;
}
