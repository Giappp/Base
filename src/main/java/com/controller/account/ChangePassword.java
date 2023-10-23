package com.controller.account;

import com.controller.AlertMessages;
import com.controller.data;
import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangePassword implements Initializable {
    @FXML
    private Button acceptNewPassBtn;

    @FXML
    private PasswordField pfChangeNewPass;

    @FXML
    private PasswordField pfConfirmNewPass;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        acceptNewPassBtn.setOnAction(event -> {
            if (pfChangeNewPass.getText().isEmpty() || pfConfirmNewPass.getText().isEmpty()) {
                alert.errorMessage("Please fill all blank fields!");
            } else if (!pfChangeNewPass.getText().equals(pfConfirmNewPass.getText())) {
                alert.errorMessage("Password does not match!");
            } else {
                String updateData = "UPDATE users SET password = ?, WHERE username = '" + data.username + "'";
                try (Connection con = JDBCConnect.getJDBCConnection();
                     PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateData)) {
                    ps.setString(1, pfChangeNewPass.getText());
                    ps.executeUpdate();
                    alert.successMessage("Change Password Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
