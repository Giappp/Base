module Base {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.mail;

    opens com.entities to javafx.base;
    exports com.controller to javafx.fxml;
    opens com.controller.logSign to javafx.fxml;
    exports com.controller.logSign;
    opens com.controller to javafx.fxml;
    exports com.view;
    opens com.view to javafx.fxml;
    exports com.entities;
    exports  com.model;
    exports com.controller.account to javafx.fxml;
    opens com.controller.account to javafx.fxml;
    exports com.controller.storage to javafx.fxml;
    opens com.controller.storage to javafx.fxml;
    exports com.controller.event to javafx.fxml;
    opens com.controller.event to javafx.fxml;
    exports com.controller.product to javafx.fxml;

    exports com.controller.order to javafx.fxml;
    opens com.controller.order to javafx.fxml;

    opens com.controller.product to javafx.fxml;
    exports com.controller.dashboard to javafx.fxml;
    opens com.controller.dashboard to javafx.fxml;
    exports com.controller.client to javafx.fxml;
    opens com.controller.client to javafx.fxml;
    exports com.controller.customer to javafx.fxml;
    opens com.controller.customer to javafx.fxml;
}
