module Base {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.entities to javafx.base;
    opens com.controller.logSign to javafx.fxml;
    exports com.controller.logSign;
    opens com.controller.client to javafx.fxml;
    exports com.controller.client;
    exports com;
    opens com to javafx.fxml;
}
