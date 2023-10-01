package com.controller.client;

import com.controller.logSign.DBController;
import com.db.dao.JDBCConnect;
import com.entities.Product;
import com.model.ProductModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    Scene fxmlFile;
    Parent root;
    Stage window;
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
    private AnchorPane main_form;
    @FXML
    private AnchorPane dashboard_home;

    @FXML
    private AnchorPane dashboard_order;

    @FXML
    private AnchorPane dashboard_storage;

    @FXML
    private Button home_btn;

    @FXML
    private Button messages_btn;

    @FXML
    private Button orders_btn;

    @FXML
    private TableView<Product> tblv_productView;

    @FXML
    private TableColumn<Product, Integer> product_col_amount;

    @FXML
    private TableColumn<Product, String> product_col_brand;

    @FXML
    private TableColumn<Product, ImageView> product_col_image;

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
    private Button setting_btn;

    @FXML
    private Button sign_out_btn;

    @FXML
    private Button storage_btn;

    @FXML
    private Button addProduct_btn;
    @FXML
    private Button importProduct_btn;
    @FXML
    private Text title_text;
    @FXML
    private Text title_text1;
    @FXML
    private Text title_text11;
    @FXML
    private Text total_order_text;
    @FXML
    private Label username_label;

    private ObservableList<Product> observableList;
    @FXML
    private BarChart<?, ?> sale_revenue_chart;

    public void chart() throws Exception {
        String sql = "SELECT total_price, date_recorded FROM invoice GROUP BY date_recorded ORDER BY TIMESTAMP(date_recorded) ASC LIMIT 8";

        try (Connection con = JDBCConnect.getJDBCConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            XYChart.Series chartData = new XYChart.Series<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                chartData.getData().add(new XYChart.Data<>(rs.getDouble(1), rs.getDate(2)));
            }

            sale_revenue_chart.getData().add(chartData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            chart();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        addProductShowListData();
        home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
        sign_out_btn.setOnAction(event -> {
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
        });
        home_btn.setOnAction(event -> {
            dashboard_home.setVisible(true);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);

            home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
            storage_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
        });
        storage_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(true);
            addProductShowListData();

            storage_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            home_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
        });
        orders_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_order.setVisible(true);
            dashboard_storage.setVisible(false);

            orders_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            home_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
        });
        observableList.addListener((InvalidationListener) observable -> addProductShowListData());

        addProduct_btn.setOnAction(event -> {
            try{
                openModalWindow("/controller/client/addProduct.fxml","add Product");
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void addProductShowListData(){
        observableList = new ProductModel().getProductList();
        product_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        product_col_image.setCellValueFactory(param -> {
            // Create an imageView based on the image URL in the data
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            String imageUrl = param.getValue().getImage();
            Image image = new Image(imageUrl);
            imageView.setImage(image);
            return new SimpleObjectProperty<>(imageView);
        });
        product_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblv_productView.setItems(observableList);
    }
    private void openModalWindow(String resource, String title) throws IOException{
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
//        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(title);
        window.showAndWait();
    }
}
