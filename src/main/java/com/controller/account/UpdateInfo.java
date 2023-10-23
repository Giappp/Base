package com.controller.account;

import com.controller.AlertMessages;
import com.controller.data;
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
    private Button acceptNewInfoBtn;

    @FXML
    private TextArea taNewDetail;

    @FXML
    private TextField tfNewEmail;

    @FXML
    private TextField tfNewPhone;

    @FXML
    private TextField tfNewUsername;

    AlertMessages alert = new AlertMessages();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        acceptNewInfoBtn.setOnAction(event -> {
            if (taNewDetail.getText().isEmpty() || tfNewEmail.getText().isEmpty()
                    || tfNewPhone.getText().isEmpty() || tfNewUsername.getText().isEmpty())
                alert.errorMessage("Please fill all blank fields!");
            else {
                String updateInfo = "Update users SET username, email, phone, detail WHEN username = '" + data.username + "'";
                try (Connection con = JDBCConnect.getJDBCConnection();
                     PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateInfo)) {
                    ps.setString(1, tfNewUsername.getText());
                    ps.setString(2, tfNewEmail.getText());
                    ps.setString(3, tfNewPhone.getText());
                    ps.setString(4, taNewDetail.getText());
                    ps.executeUpdate();
                    alert.successMessage("Update Information Successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
