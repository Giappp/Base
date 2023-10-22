package com.controller.logSign;

import com.controller.feature.allfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DBController {
    private static Parent root;
    public static void changeScene(ActionEvent event, String fxmlFile){
        try{
            root = FXMLLoader.load(Objects.requireNonNull(DBController.class.getResource(fxmlFile)));
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,650,500);
        Screen screen = Screen.getPrimary();
        double centerX = screen.getVisualBounds().getWidth() / 2;
        double centerY = screen.getVisualBounds().getHeight() / 2;

        // Set the scene's position to the center
        stage.setX(centerX - (scene.getWidth() / 2));
        stage.setY(centerY - (scene.getHeight() / 2));
        stage.setScene(scene);
        stage.show();
    }
    public static void showDashboardScene(ActionEvent event,String username){
        try{
            FXMLLoader loader = new FXMLLoader(DBController.class.getResource("/controller/client/client.fxml"));
            root = loader.load();

            allfile dashBoardController = loader.getController();
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1200,700);
        Screen screen = Screen.getPrimary();
        double centerX = screen.getVisualBounds().getWidth() / 2;
        double centerY = screen.getVisualBounds().getHeight() / 2;

        // Set the scene's position to the center
        stage.setX(centerX - (scene.getWidth() / 2));
        stage.setY(centerY - (scene.getHeight() / 2));
        stage.setScene(scene);
        stage.show();
    }
}
