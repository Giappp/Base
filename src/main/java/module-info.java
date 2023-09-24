module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.controller.logSign to javafx.fxml;
    exports com.controller.logSign;
    opens com.controller.client to javafx.fxml;
    exports com.controller.client;
    exports com.models to javafx.fxml;
    opens com.models;
    exports com.views;
    opens com.views;
    exports com;
    opens com to javafx.fxml;
}
