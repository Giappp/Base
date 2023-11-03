package com.controller.logSign;

import com.controller.AlertMessages;
import com.db.dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Platform;

import java.util.Objects;
import java.util.regex.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private PasswordField pfConfirmPassword;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfPhone;

    @FXML
    private Label lblErrorPass;

    private final AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        btnLogin.setOnAction(event -> DBController.changeScene(event, "/controller/logSign/log-in.fxml"));

        btnSignup.setOnAction(event -> {
            String username = tfUsername.getText();
            String password = pfPassword.getText();
            String confirmPassword = pfConfirmPassword.getText();
            String email = tfEmail.getText();
            String phone = tfPhone.getText();

            if (!username.trim().isEmpty() && !password.trim().isEmpty()
                    && !confirmPassword.trim().isEmpty() && !email.trim().isEmpty()
                    && !phone.trim().isEmpty() && validatePasswords(password, confirmPassword)) {
                if (validateEmail(email)) {
                    System.out.println("Email is invalid");
                    alert.errorMessage("Email is invalid. Please try again.");
                } else if (validatePhone(phone)) {
                    System.out.println("Phone number is invalid");
                    alert.errorMessage("Phone number is invalid. Please try again.");
                } else {
                    signUpUser(event, username, password, email, phone);
                }
            } else {
                System.out.println("Please fill all information");
                alert.errorMessage("Please fill all information to sign up.");
            }
        });

        // Add listeners to validate passwords
        pfConfirmPassword.focusedProperty().addListener((observable, oldValue, newValue)
                -> validatePasswords(pfPassword.getText(), pfConfirmPassword.getText()));

        pfPassword.focusedProperty().addListener((observable, oldValue, newValue)
                -> validatePasswords(pfPassword.getText(), pfConfirmPassword.getText()));
    }

    private boolean validatePasswords(String password, String confirmPassword) {
        // Check if confirmed password matches with password
        if (!password.isBlank() && !confirmPassword.isBlank() && !password.equals(confirmPassword)) {
            Platform.runLater(() -> lblErrorPass.setText("Confirm password does not match"));
            return false;
        } else {
            lblErrorPass.setText("");
            return true;
        }
    }

    private boolean validateEmail(String email) {
        String emailCheck = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return !email.isBlank() && Pattern.matches(emailCheck, email);
    }

    private boolean validatePhone(String phone) {
        String phoneCheck = "(84|0[35789])+([0-9]{8})\\b";
        return !phone.isBlank() && Pattern.matches(phoneCheck, phone);
    }

    private void signUpUser(ActionEvent event, String username, String password, String email, String phone) {
        String sql1 = "SELECT * FROM users WHERE username = ?";
        String sql2 = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps1 = Objects.requireNonNull(con).prepareStatement(sql1)) {

            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();

            if (rs.isBeforeFirst()) {
                System.out.println("Username already exists!");
                alert.errorMessage("You can't use this username. Please try again.");
            } else {
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setString(1, username);
                ps2.setString(2, password);
                ps2.setString(3, email);
                ps2.setString(4, phone);
                ps2.executeUpdate();

                DBController.changeScene(event, "/controller/logSign/log-in.fxml");
                alert.successMessage("Registered Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
