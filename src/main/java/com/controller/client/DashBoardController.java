package com.controller.client;

import com.controller.logSign.DBController;
import com.entities.Product;
import com.model.ProductModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
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
    private TableView<Product> tblv_productView;
    @FXML
    private TableColumn<Product, Integer> product_col_amount;

    @FXML
    private TableColumn<Product, String> product_col_brand;

    @FXML
    private TableColumn<Product, Date> product_col_date;

    @FXML
    private TableColumn<Product, Integer> product_col_id;

    @FXML
    private TableColumn<Product, String> product_col_name;

    @FXML
    private TableColumn<Product, Double> product_col_price;

    @FXML
    private TableColumn<Product, String> product_col_status;

    @FXML
    private TableColumn<Product, String> product_col_type;

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
    private ObservableList<Product> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductShowListData();
        sign_out_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure want to logout?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        DBController.changeScene(event, "/controller/logSign/log-in.fxml");
                    } else return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        home_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard_home.setVisible(true);
                dashboard_order.setVisible(false);
                dashboard_storage.setVisible(false);
            }
        });
        storage_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard_home.setVisible(false);
                dashboard_order.setVisible(false);
                dashboard_storage.setVisible(true);
                addProductShowListData();
            }
        });
        orders_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard_home.setVisible(false);
                dashboard_order.setVisible(true);
                dashboard_storage.setVisible(false);
            }
        });
        tblv_productView.setItems(observableList);
        observableList.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                addProductShowListData();
            }
        });
    }
    public void addProductShowListData(){
        observableList = new ProductModel().getProductList();
        product_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        product_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        product_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
}
