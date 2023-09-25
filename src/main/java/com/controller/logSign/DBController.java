package com.controller.logSign;

import com.db.dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DBController {
    public static void changeScene(ActionEvent event, String fxmlFile){
        Parent root = null;
        try{
            root = FXMLLoader.load(Objects.requireNonNull(DBController.class.getResource(fxmlFile)));
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
