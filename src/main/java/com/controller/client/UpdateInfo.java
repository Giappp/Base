package com.controller.client;

import com.controller.AlertMessages;
import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateInfo implements Initializable {
    @FXML
    private Button accept_new_info_btn;

    @FXML
    private TextArea ta_new_detail;

    @FXML
    private TextField tf_new_email;

    @FXML
    private TextField tf_new_phone;

    @FXML
    private TextField tf_new_username;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        accept_new_info_btn.setOnAction(event -> {
            if (ta_new_detail.getText().isEmpty() || tf_new_email.getText().isEmpty()
                    || tf_new_phone.getText().isEmpty() || tf_new_username.getText().isEmpty())
                alert.errorMessage("Please fill all blank fields!");
            else {
                String updateInfo = "Update users SET username, email, phone, detail WHEN username = '" + data.username + "'";
                try (Connection con = JDBCConnect.getJDBCConnection();
                     PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateInfo)) {
                    ps.setString(1, tf_new_username.getText());
                    ps.setString(2, tf_new_email.getText());
                    ps.setString(3, tf_new_phone.getText());
                    ps.setString(4, ta_new_detail.getText());
                    ps.executeUpdate();
                    alert.successMessage("Update Information Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
