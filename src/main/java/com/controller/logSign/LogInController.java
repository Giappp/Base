package com.controller.logSign;

import com.controller.AlertMessages;
import com.controller.feature.data;
import com.db.dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(event -> logInUser(event, tfUsername.getText(), pfPassword.getText()));

        btnSignup.setOnAction(event -> DBController.changeScene(event, "/controller/logSign/sign-up.fxml"));
    }
    public void logInUser(ActionEvent event, String username, String password) {
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("SELECT password FROM users WHERE username = ?")
             ){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("Invalid username or password!");
                alert.errorMessage("Provided credentials are incorrect!");
            } else {
                while (rs.next()) {

                    data.username = tfUsername.getText();

                    String retrievedPass = rs.getString("password");
                    if (retrievedPass.equals(password)) {
                        alert.successMessage("Login Successfully!");
                        DBController.showDashboardScene(event,username);
                    } else {
                        System.out.println("Password incorrect!");
                        alert.errorMessage("Provided credentials are incorrect!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

