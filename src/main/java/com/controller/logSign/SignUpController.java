package com.controller.logSign;

import com.controller.AlertMessages;
import com.db.dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.application.Platform;
import java.util.regex.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button btn_signup;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirm_password;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_phone;
    @FXML
    private Label lbl_error_pass;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        btn_login.setOnAction(event -> DBController.changeScene(event, "/controller/logSign/log-in.fxml"));


        btn_signup.setOnAction(event -> {
            if (!tf_username.getText().trim().isEmpty()
                    && validatePasswords(pf_password.getText(), pf_confirm_password.getText(), lbl_error_pass)
                    && validateEmail(tf_email.getText()) && validatePhone(tf_phone.getText())) {
                signUpUser(event, tf_username.getText(), pf_password.getText(), pf_confirm_password.getText(),
                        tf_email.getText(), tf_phone.getText());
            } else {
                System.out.println("Please fill all information");
                alert.errorMessage("Please fill all information to sign up.");
            }
        });
        // Add listener on focus action
        pf_confirm_password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validatePasswords(pf_password.getText(), pf_confirm_password.getText(), lbl_error_pass);
            }
        });
        pf_password.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validatePasswords(Objects.requireNonNull(pf_password.getText()), Objects.requireNonNull(pf_confirm_password.getText()), lbl_error_pass);
            }
        });
    }

    private boolean validateEmail(String email){
        String emailCheck = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(email.isBlank()){
            return false;
        }
        else return Pattern.matches(emailCheck, email);
    }

    private boolean validatePhone(String phone){
        String phoneCheck = "(84|0[35789])+([0-9]{8})\\b";
        if(phone.isBlank()){
            return false;
        }
        else return Pattern.matches(phoneCheck, phone);
    }

    //
    private boolean validatePasswords(String password, String confirmPassword, Label lbl_error_pass) {
        // Check if confirmed password matches with password
        if (!password.isBlank() && !confirmPassword.isBlank() && !password.equals(confirmPassword)) {
            Platform.runLater(() -> lbl_error_pass.setText("Confirm password does not match"));
            return false;
        }
        // Check if password is blank
        else if (password.isBlank() || confirmPassword.isBlank()) {
            lbl_error_pass.setText("");
            return false;
        }
        //
        else {
            lbl_error_pass.setText("");
            return true;
        }
    }
    public void signUpUser(ActionEvent event, String username, String password, String confirmPass,
                                  String email,String phone) {
        try(Connection con = JDBCConnect.getJDBCConnection();
            PreparedStatement ps = Objects.requireNonNull(con).prepareStatement("SELECT * FROM users WHERE username = ?")
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                System.out.println("Username already exist!");
                alert.errorMessage("You can't use this username. Please try again.");
            } else {
                PreparedStatement ps2 = con.prepareStatement("INSERT INTO users (username, password, email,phone) VALUES (?, ?, ?,?)");
                ps2.setString(1, username);
                ps2.setString(2, password);
                ps2.setString(3, email);
                ps2.setString(4,phone);
                ps2.executeUpdate();
                DBController.changeScene(event,"controller/logSign/log-in.fxml");
                alert.successMessage("Registered Successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

