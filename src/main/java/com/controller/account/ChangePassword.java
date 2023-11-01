package com.controller.account;

import com.controller.AlertMessages;
import com.controller.data;
import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangePassword implements Initializable {

    Stage closeWin;

    @FXML
    private Label tfId;

    @FXML
    private Button acceptNewPassBtn;

    @FXML
    private PasswordField pfChangeNewPass;

    @FXML
    private PasswordField pfConfirmNewPass;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        viewId();
        acceptNewPassBtn.setOnAction(event -> changePass());
    }

    public void viewId() {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ps.setString(1, data.username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tfId.setText(rs.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePass() {
        if (pfChangeNewPass.getText().isEmpty() || pfConfirmNewPass.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields!");
        } else if (!pfChangeNewPass.getText().equals(pfConfirmNewPass.getText())) {
            alert.errorMessage("Password does not match!");
        } else {
            String updateData = "UPDATE users SET password = ? WHERE id = ?";
            try (Connection con = JDBCConnect.getJDBCConnection();
                 PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateData)) {
                ps.setString(1, pfChangeNewPass.getText());
                ps.setInt(2, Integer.parseInt(tfId.getText())); // Use id to change pass
                ps.executeUpdate();

                // Automatically close the interface after successfully changing the password
                alert.successMessage("Change Password Successfully!");

                // Initialize closeWin with the current stage
                closeWin = (Stage) acceptNewPassBtn.getScene().getWindow();
                closeWin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
