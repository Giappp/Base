package com.controller.client;

import com.controller.AlertMessages;
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
    private Button accept_new_pass_btn;

    @FXML
    private PasswordField pf_change_new_pass;

    @FXML
    private PasswordField pf_confirm_new_pass;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        accept_new_pass_btn.setOnAction(event -> {
            if (pf_change_new_pass.getText().isEmpty() || pf_confirm_new_pass.getText().isEmpty()) {
                alert.errorMessage("Please fill all blank fields!");
            } else if (!pf_change_new_pass.getText().equals(pf_confirm_new_pass.getText())) {
                alert.errorMessage("Password does not match!");
            } else {
                String updateData = "UPDATE users SET password = ?, WHERE username = '" + data.username + "'";
                try (Connection con = JDBCConnect.getJDBCConnection();
                     PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateData)) {
                    ps.setString(1, pf_change_new_pass.getText());
                    ps.executeUpdate();
                    alert.successMessage("Change Password Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
