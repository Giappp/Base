package com.controller.account;

import com.controller.AlertMessages;
import com.controller.data;
import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateInfo implements Initializable {

    @FXML
    private Label tfId;

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

    Connection con = JDBCConnect.getJDBCConnection();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        viewInfo();
        acceptNewInfoBtn.setOnAction(event -> updateProfile());
    }

    public void viewInfo() {
        String sql = "SELECT id, username, email, phone, details FROM users WHERE username = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, data.username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tfId.setText(rs.getString("id"));
                tfNewUsername.setText(rs.getString("username"));
                tfNewEmail.setText(rs.getString("email"));
                tfNewPhone.setText(rs.getString("phone"));
                taNewDetail.setText(rs.getString("details"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile() {
        if (taNewDetail.getText().isEmpty() || tfNewEmail.getText().isEmpty()
                || tfNewPhone.getText().isEmpty() || tfNewUsername.getText().isEmpty())
            alert.errorMessage("Please fill all blank fields!");
        else {
            String updateInfo = "Update users SET username = ?, email = ?, phone = ?, details = ? WHERE id = ?";

            try (PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(updateInfo)) {
                ps.setString(1, tfNewUsername.getText());
                ps.setString(2, tfNewEmail.getText());
                ps.setString(3, tfNewPhone.getText());
                ps.setString(4, taNewDetail.getText());
                ps.setInt(5, Integer.parseInt(tfId.getText()));
                ps.executeUpdate();
                alert.successMessage("Update Information Successfully!");

                data.username = tfNewUsername.getText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
