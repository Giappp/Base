package com.controller.logSign;

import db.dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_signup;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logInUser(event, tf_username.getText(), pf_password.getText());
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBController.changeScene(event, "sign-up.fxml");
            }
        });
    }
    public static void logInUser(ActionEvent event, String username, String password) {
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = con.prepareStatement("SELECT password FROM users WHERE username = ?");
             ){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("User not found in database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (rs.next()) {
                    String retrievedPass = rs.getString("password");
                    if (retrievedPass.equals(password)) {
                        DBController.changeScene(event,"dashboard.fxml");
                    } else {
                        System.out.println("Password incorrect!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

