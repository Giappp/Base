module Base {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.entities to javafx.base;
    exports com.controller.feature to javafx.fxml;
    opens com.controller.logSign to javafx.fxml;
    exports com.controller.logSign;
    opens com.controller.feature to javafx.fxml;
    exports com.view;
    opens com.view to javafx.fxml;
    exports com.entities;
    exports com.controller.feature.account to javafx.fxml;
    opens com.controller.feature.account to javafx.fxml;
    exports com.controller.feature.storage to javafx.fxml;
    opens com.controller.feature.storage to javafx.fxml;
    exports com.controller.feature.event to javafx.fxml;
    opens com.controller.feature.event to javafx.fxml;
    exports com.controller.feature.product to javafx.fxml;
    opens com.controller.feature.product to javafx.fxml;
    exports com.controller.feature.dashboard to javafx.fxml;
    opens com.controller.feature.dashboard to javafx.fxml;
    exports com.controller.feature.client to javafx.fxml;
    opens com.controller.feature.client to javafx.fxml;
    exports com.controller.feature.customer to javafx.fxml;
    opens com.controller.feature.customer to javafx.fxml;
}
