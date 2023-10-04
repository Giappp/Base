package com.controller.client;

import com.controller.logSign.DBController;
import com.db.dao.JDBCConnect;
import com.entities.Product;
import com.model.ProductModel;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.KeyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private TextField tf_id_choice;
    Scene fxmlFile;

    Parent root;

    Stage window;

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    private ObservableList<Product> observableList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Product, Integer> order_col_id;

    @FXML
    private TableColumn<Product, String> order_col_type;

    @FXML
    private TableColumn<Product, String> order_col_brand;

    @FXML
    private TableColumn<Product, String> order_col_name;

    @FXML
    private TableColumn<Product, Integer> order_col_amount;

    @FXML
    private TableColumn<Product, Double> order_col_price;

    @FXML
    private TableColumn<Product, Integer> order_col_status;

    @FXML
    private Spinner<?> sp_choice_amount;

    @FXML
    private Text display_price_all;

    @FXML
    private Button accept_order_btn;

    @FXML
    private Button start_order_btn;

    @FXML
    private Label display_username;

    @FXML
    private Label display_email;

    @FXML
    private Label display_phone;

    @FXML
    private Label display_pass;

    @FXML
    private TextField tf_type_choice;

    @FXML
    private TextField tf_choice_brand;

    @FXML
    private TextField tf_choice_name;

    @FXML
    private Button earning_info_btn;

    @FXML
    private javafx.scene.text.Text earning_text;

    @FXML
    private Button message_btn;

    @FXML
    private Button notify_btn;

    @FXML
    private Button product_sold_btn;

    @FXML
    private javafx.scene.text.Text product_sold_text;

    @FXML
    private Button total_delivery_btn;

    @FXML
    private javafx.scene.text.Text total_delivery_text;

    @FXML
    private Button total_order_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane dashboard_home;

    @FXML
    private AnchorPane dashboard_account;

    @FXML
    private AnchorPane dashboard_order;

    @FXML
    private AnchorPane dashboard_storage;

    @FXML
    private Button home_btn;

    @FXML
    private Button product_btn;

    @FXML
    private Button orders_btn;

    @FXML
    private TableView<Product> tblv_productView;

    @FXML
    private TableView<Product> tblv_orderView;

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
    private TableColumn<Double, Double> product_col_value;

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
    private Text total_order_text;

    @FXML
    private Label username_label;

    @FXML
    private BarChart<?, ?> sale_revenue_chart;

    @FXML
    private Button change_pass_btn;

    @FXML
    private Button update_account_info;

    @FXML
    private Button account_btn;

    public void chart() throws Exception {
        String sql = "SELECT SUM(total_price), date_recorded FROM invoice GROUP BY date_recorded ORDER BY TIMESTAMP(date_recorded) ASC LIMIT 8";

        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            XYChart.Series<Object, Object> chartData = new XYChart.Series<>();
            rs = ps.executeQuery();
            while (rs.next()) {
                chartData.getData().add(new XYChart.Data<>(rs.getDouble(1), rs.getDate(2)));
            }
            sale_revenue_chart.getData();
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
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);

            home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
        });
        account_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(true);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);

            home_btn.setStyle("-fx-background-color: transparent");
            account_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
            storage_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
        });
        storage_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(true);
            addProductShowListData();

            storage_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
        });
        orders_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(true);
            dashboard_storage.setVisible(false);

            orders_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
        });
        observableList.addListener((InvalidationListener) observable -> addProductShowListData());

        addProduct_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/addProduct.fxml", "Add Product");
                System.out.println("Add Product on progress...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("reset table");
            addProductShowListData();
        });
        importProduct_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/importProduct.fxml", "Import Product");
                System.out.println("Import Product on progress...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("reset table");
            addProductShowListData();
        });

        tblv_productView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });

        change_pass_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/change-pass.fxml", "Change Password");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        order_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        order_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        order_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_col_type.setCellValueFactory(new PropertyValueFactory<>("productTypeId"));
        order_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        order_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        order_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblv_orderView.setItems(observableList);
    }

    public void addProductShowListData() {
        observableList = new ProductModel().getProductList();
        product_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        product_col_image.setCellValueFactory(param -> {
            // Create an imageView based on the image URL in the data
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            String imageUrl = param.getValue().getImage();
            if(imageUrl != null){
                Image image = new Image(imageUrl);
                imageView.setImage(image);
                return new SimpleObjectProperty<>(imageView);
            }
            return null;
        });
        product_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        product_col_value.setCellValueFactory(new PropertyValueFactory<>("importedPriceValue"));
        tblv_productView.setItems(observableList);
    }

    private void openModalWindow(String resource, String title) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
//        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(title);
        window.showAndWait();
    }

    public void viewAccountDetail() throws Exception {
        String viewAccountSql = "SELECT username, email, phone, detail FROM users";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(viewAccountSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

            }
        }
    }

    public void handleScanProduct(KeyEvent event) throws Exception {
        ps = con.prepareStatement("SELECT * FROM Product WHERE id = ?");
        ps.setString(1, tf_id_choice.getText());
        rs = ps.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String productType = rs.getString(3);
            String supplierName = rs.getString(5);
            tf_choice_name.setText(name);
            tf_choice_brand.setText(supplierName);
            tf_type_choice.setText(productType);
            sp_choice_amount.requestFocus();
        }
        rs.close();
    }

    public void handleOrder(ActionEvent event) {
        int quantityInStock = Integer.parseInt(sp_choice_amount.getPromptText());
        if (quantityInStock != 0) {
            observableList.add(new Product());
            tblv_orderView.setItems(observableList);
        }
    }

    private void clearText() {
        tf_id_choice.clear();
        tf_type_choice.clear();
        tf_choice_brand.clear();
        tf_choice_name.clear();
    }
}
