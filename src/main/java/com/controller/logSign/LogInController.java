package com.controller.logSign;

import com.controller.client.data;
import com.db.dao.JDBCConnect;
import javafx.event.ActionEvent;
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
import java.util.Objects;
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
        btn_login.setOnAction(event -> logInUser(event, tf_username.getText(), pf_password.getText()));

        btn_signup.setOnAction(event -> DBController.changeScene(event, "/controller/logSign/sign-up.fxml"));
    }
    public void logInUser(ActionEvent event, String username, String password) {
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("SELECT password FROM users WHERE username = ?")
             ){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("Invalid username or password!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (rs.next()) {

                    data.username = tf_username.getText();

                    String retrievedPass = rs.getString("password");
                    if (retrievedPass.equals(password)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Login Successfully!");
                        alert.showAndWait();
                        DBController.showDashboardScene(event,username);
                    } else {
                        System.out.println("Password incorrect!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect!");
                        alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

