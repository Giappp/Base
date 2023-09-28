package com.controller.client;

import com.controller.logSign.DBController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    public Button storage_btn1;
    public Text title_text111;
    @FXML
    private Button account_btn;

    @FXML
    private AnchorPane dashboard_home;

    @FXML
    private AnchorPane dashboard_order;

    @FXML
    private AnchorPane dashboard_storage;

    @FXML
    private AnchorPane dashboardscene;

    @FXML
    private Button earning_info_btn;

    @FXML
    private Text earning_text;

    @FXML
    private Button home_btn;

    @FXML
    private Button message_btn;

    @FXML
    private Button messages_btn;

    @FXML
    private Button notify_btn;

    @FXML
    private Button orders_btn;

    @FXML
    private TableColumn<?, ?> product_col_amount;

    @FXML
    private TableColumn<?, ?> product_col_brand;

    @FXML
    private TableColumn<?, ?> product_col_date;

    @FXML
    private TableColumn<?, ?> product_col_id;

    @FXML
    private TableColumn<?, ?> product_col_name;

    @FXML
    private TableColumn<?, ?> product_col_price;

    @FXML
    private TableColumn<?, ?> product_col_status;

    @FXML
    private TableColumn<?, ?> product_col_type;

    @FXML
    private TextField product_field_search;

    @FXML
    private Button product_sold_btn;

    @FXML
    private Text product_sold_text;

    @FXML
    private Button setting_btn;

    @FXML
    private Button sign_out_btn;

    @FXML
    private Button storage_btn;

    @FXML
    private Text title_text;

    @FXML
    private Text title_text1;

    @FXML
    private Text title_text11;

    @FXML
    private Button total_delivery_btn;

    @FXML
    private Text total_delivery_text;

    @FXML
    private Button total_order_btn;

    @FXML
    private Text total_order_text;

    @FXML
    private Label username_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sign_out_btn.setOnAction(event -> {
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to logout?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    DBController.changeScene(event,"/controller/logSign/log-in.fxml");
                }else return;
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        home_btn.setOnAction(event -> {
            dashboard_home.setVisible(true);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);
        });
        storage_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(true);
        });
        orders_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_order.setVisible(true);
            dashboard_storage.setVisible(false);
        });
    }
    public void switchForm(){

    }
}
