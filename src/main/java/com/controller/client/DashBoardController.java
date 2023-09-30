package com.controller.client;

import com.controller.logSign.DBController;
import com.entities.Product;
import com.entities.Supplier;
import com.model.ProductModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.util.*;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    public Button earning_info_btn;

    public javafx.scene.text.Text earning_text;

    public Button message_btn;

    public Button notify_btn;

    public Button product_sold_btn;

    public javafx.scene.text.Text product_sold_text;

    public Button total_delivery_btn;

    public javafx.scene.text.Text total_delivery_text;

    public Button total_order_btn;

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
    @FXML
    private ComboBox<?> cb_listproduct;

    @FXML
    private ComboBox<?> cb_listproducttype;

    @FXML
    private ComboBox<?> cb_listsupplier;
    @FXML
    private Spinner<?> sp_quantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductShowListData();
        home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
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

                home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
                storage_btn.setStyle("-fx-background-color: transparent");
                orders_btn.setStyle("-fx-background-color: transparent");
            }
        });
        storage_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard_home.setVisible(false);
                dashboard_order.setVisible(false);
                dashboard_storage.setVisible(true);
                addProductShowListData();

                storage_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
                home_btn.setStyle("-fx-background-color: transparent");
                orders_btn.setStyle("-fx-background-color: transparent");
            }
        });
        orders_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dashboard_home.setVisible(false);
                dashboard_order.setVisible(true);
                dashboard_storage.setVisible(false);

                orders_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
                home_btn.setStyle("-fx-background-color: transparent");
                storage_btn.setStyle("-fx-background-color: transparent");
            }
        });
        addProductShowListData();
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
        tblv_productView.setItems(observableList);
    }

    public void getBrandName(){
        List<String> listT = new ArrayList<>();
    }
}
