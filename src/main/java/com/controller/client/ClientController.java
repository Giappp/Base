package com.controller.client;

import com.controller.data;
import com.controller.logSign.DBController;
import com.entities.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class ClientController {

    @FXML
    private Button checkbtn;

    @FXML
    private BorderPane fullBorderPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button homeBtn;

    @FXML
    private Button accountBtn;

    @FXML
    private Button customerBtn;

    @FXML
    private Button storageBtn;

    @FXML
    private Button productBtn;

    @FXML
    private Button ordersBtn;

    @FXML
    private Button eventBtn;

    @FXML
    private Button signOutBtn;

    private Button activeButton; // variable to store the active status of a button

    private void loadPage(String page) {
        Users.setUsername(data.username);
        usernameLabel.setText(Users.getUsername());
        Parent root = null;
        try {
            // Create a file object for the FXML file
            File fxmlFile = new File("src/main/resources/controller/client/" + page + ".fxml");

            // Get the absolute path of the FXML file
            String absolutePath = fxmlFile.getAbsolutePath();

            root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot navigation page");
        }
        fullBorderPane.setCenter(root);
    }

    private void setActiveButton (Button button){
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: transparent;");
        }
        button.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
        activeButton = button;
    }

    private double xOffset = 0;
    private double yOffSet = 0;

    public void initialize() {

        loadPage("dashboard_view");
        setActiveButton(homeBtn);
        usernameLabel.setText(data.username);

        homeBtn.setOnAction(event -> {
            loadPage("dashboard_view");
            setActiveButton(homeBtn);
        });

//        checkbtn.setOnAction(event -> {
//            loadPage("best-sellingOfMonth");
//            setActiveButton(checkbtn);
//        });

        accountBtn.setOnAction(event -> {
            loadPage("account_view");
            setActiveButton(accountBtn);
        });

        customerBtn.setOnAction(event -> {
            loadPage("customer_view");
            setActiveButton(customerBtn);
        });

        storageBtn.setOnAction(event -> {
            loadPage("storage_view");
            setActiveButton(storageBtn);
        });

        productBtn.setOnAction(event -> {
            loadPage("product_view");
            setActiveButton(productBtn);
        });

        ordersBtn.setOnAction(event -> {
            loadPage("order_view");
            setActiveButton(ordersBtn);
        });

        eventBtn.setOnAction(event -> {
            loadPage("event_view");
            setActiveButton(eventBtn);
        });

        signOutBtn.setOnAction(event -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to logout?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    DBController.changeScene(event, "/controller/logSign/log-in.fxml");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fullBorderPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffSet = event.getSceneY();
        });

        fullBorderPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) fullBorderPane.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffSet);
        });
    }
}
