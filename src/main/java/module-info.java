module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens ui.logSign to javafx.fxml;
    exports ui.logSign;

    opens ui.dashboard to javafx.fxml;
    exports ui.dashboard;
}
