package com.controller.account;

import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class AccountController {

    Parent root;

    Scene fxmlFile;

    Stage window;

    @FXML
    private Label displayUsername;

    @FXML
    private Label displayEmail;

    @FXML
    private Label displayPhone;

    @FXML
    private Label displayPass;

    @FXML
    private Button updateAccountInfo;

    @FXML
    private Button changePassBtn;

    @FXML
    private Label displayDetail;

    public void viewProfile () {
        String viewAccountSql = "SELECT username, email, phone, details, password FROM users";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(viewAccountSql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                displayUsername.setText(rs.getString("username"));
                displayEmail.setText(rs.getString("email"));
                displayPhone.setText(rs.getString("phone"));
                displayPass.setText(rs.getString("password"));
                displayDetail.setText(rs.getString("details"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModalWindow (String resource, String title) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

    public void initialize() {

        viewProfile();

        changePassBtn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/change-pass.fxml", "Change Password");
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewProfile();
        });

        updateAccountInfo.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/update-info.fxml", "Update User Information");
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewProfile();
        });
    }
}
