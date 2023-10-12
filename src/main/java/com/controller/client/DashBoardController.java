package com.controller.client;

import com.controller.logSign.DBController;
import com.db.dao.JDBCConnect;
import com.entities.GoodsImport;
import com.entities.Product;
import com.model.GoodsImportModel;
import com.model.ProductCategoryModel;
import com.model.ProductModel;
import com.model.SupplierModel;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.beans.binding.Bindings.format;

public class DashBoardController implements Initializable {
    @FXML
    private Label display_detail;

    @FXML
    private AnchorPane dashboard_product;

    @FXML
    private AnchorPane updateScene;
    @FXML
    private TableView<Product> tblv_product;
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
    private TableView<Product> tbv_goods;

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
    @FXML
    private Button addProduct_addBtn;

    @FXML
    private Button cancel_btn;

    @FXML
    private ComboBox<String> addProduct_brand_cb;

    @FXML
    private TextField addProduct_name_tf;

    @FXML
    private Button addProduct_newBrand_btn;

    @FXML
    private Button addProduct_newType_btn;

    @FXML
    private TextField addProduct_salesprice_tf;

    @FXML
    private ComboBox<String> addProduct_type_cb;
    @FXML
    private ComboBox<String> cb_status;
    @FXML
    private ImageView addproduct_imageview;
    @FXML
    private TextField addProduct_importedprice_tf;
    @FXML
    private Button addProduct_updatebtn;
    @FXML
    private Button productDelete_btn;
    @FXML
    private TextField addProduct_id;
    @FXML
    private Text txt_product_id;
    @FXML
    private TableColumn<Product, String> good_col_supplier;

    @FXML
    private TableColumn<Product, Integer> goods_col_amount;

    @FXML
    private TableColumn<Product, Integer> goods_col_id;

    @FXML
    private TableColumn<Product, String> goods_col_name;

    @FXML
    private TableColumn<Product, Double> goods_col_price;

    @FXML
    private TableColumn<Product, String> goods_col_status;

    @FXML
    private TableColumn<Product, Double> goods_col_total;

    @FXML
    private TableColumn<Product, String> goods_col_type;
    @FXML
    private Button history_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // All the essentials initialization begin here

//        try {
//            chart();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        displayUsername();
//        displaySalesInfo();
        viewProfile();
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

        // Home Controller begin here
        home_btn.setOnAction(event -> {
            dashboard_home.setVisible(true);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);
            dashboard_product.setVisible(false);

            home_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
            product_btn.setStyle("-fx-background-color: transparent");
        });


        // Account controller begin here
        account_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(true);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);
            dashboard_product.setVisible(false);

            home_btn.setStyle("-fx-background-color: transparent");
            account_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill:#ADEFD1FF");
            storage_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
            product_btn.setStyle("-fx-background-color: transparent");
        });


        // Orders Controller begin here
        orders_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(true);
            dashboard_storage.setVisible(false);
            dashboard_product.setVisible(false);

            orders_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
            product_btn.setStyle("-fx-background-color: transparent");
        });


        // Product Controller begin here
        product_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(false);
            dashboard_product.setVisible(true);

            orders_btn.setStyle("-fx-background-color: transparent");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            storage_btn.setStyle("-fx-background-color: transparent");
            product_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
            addProduct_brand_cb.setItems(listBrands);
            List<String> status = Arrays.asList("Available", "Unavailable");
            cb_status.setItems(FXCollections.observableList(status));
            ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
            addProduct_type_cb.setItems(listCategory);
        });

        addProduct_newBrand_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    openModalWindow("/controller/client/newSupplier.fxml", "Supplier Management");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
                addProduct_brand_cb.setItems(listBrands);
                List<String> status = Arrays.asList("Available", "Unavailable");
                cb_status.setItems(FXCollections.observableList(status));
                ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
                addProduct_type_cb.setItems(listCategory);
            }
        });

        addProduct_addBtn.setOnAction(event -> {
            String imageUrl = null;
            if (addproduct_imageview != null && addproduct_imageview.getImage() != null) {
                imageUrl = addproduct_imageview.getImage().getUrl();
            } else {
                String currentPath = System.getProperty("user.dir");
                imageUrl = currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg";
            }
            String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
            String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
            String name = addProduct_name_tf.getText();
            String price = addProduct_salesprice_tf.getText();
            String importedPrice = addProduct_importedprice_tf.getText();
            String status = cb_status.getSelectionModel().getSelectedItem();
            if (brand != null && type != null && name != null && price != null
                     && status != null) {
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Add " + name + " To Product lists?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        Product product = new Product(name, brandId, typeId, 0, Double.parseDouble(price), Double.parseDouble(importedPrice), imageUrl, status);
                        DBAdd(product);
                        addProductShowListData();
                        addProduct_addBtn.setDisable(false);
                        addProduct_updatebtn.setDisable(true);
                        productDelete_btn.setDisable(true);
                        clearTextFields(dashboard_product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        CancelAction(cancel_btn);
        tblv_product.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
                addProduct_brand_cb.setItems(listBrands);
                List<String> status = Arrays.asList("Available", "Unavailable");
                cb_status.setItems(FXCollections.observableList(status));
                ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
                addProduct_type_cb.setItems(listCategory);

                addProduct_addBtn.setDisable(true);
                addProduct_updatebtn.setDisable(false);
                productDelete_btn.setDisable(false);

                if (checkImageUrl(newValue.getImage())) {

                    //Image img = new Image(newValue.getImage());
                    //addproduct_imageview.setImage(img);
                    String currentPath = System.getProperty("user.dir");
                    addproduct_imageview.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                } else if(newValue.getImage() == null) {
                    String currentPath = System.getProperty("user.dir");
                    addproduct_imageview.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                }
                addProduct_type_cb.getSelectionModel().select(newValue.getProductType());
                addProduct_brand_cb.getSelectionModel().select(newValue.getSupplierName());
                addProduct_name_tf.setText(newValue.getName());
                addProduct_salesprice_tf.setText(String.valueOf(newValue.getSalePrice()));
                cb_status.getSelectionModel().select(newValue.getStatus());
                addProduct_id.setText(String.valueOf(newValue.getId()));
                addProduct_importedprice_tf.setText(String.valueOf(newValue.getImportedPrice()));
            }
            else{
                addProduct_addBtn.setDisable(false);
                addProduct_updatebtn.setDisable(true);
                productDelete_btn.setDisable(true);
                tblv_product.getSelectionModel().clearSelection();
                clearTextFields(dashboard_product);
            }
        });

        addProduct_updatebtn.setOnAction(event -> {
            Integer id = Integer.valueOf(addProduct_id.getText());
            String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
            String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
            String name = addProduct_name_tf.getText();
            String price = addProduct_salesprice_tf.getText();
            String status = cb_status.getSelectionModel().getSelectedItem();
            String importedPrice = addProduct_importedprice_tf.getText();
            String imageUrl = addproduct_imageview.getImage().getUrl();
            if (brand != null && type != null && name != null && price != null
                    && imageUrl != null && status != null) {
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Update " + name + " with id " + id);
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        Product product = new Product(id, name, brand, brandId, type, typeId, Double.parseDouble(price), status, imageUrl,Double.parseDouble(importedPrice));
                        DBUpdate(product);
                        addProduct_addBtn.setDisable(false);
                        addProduct_updatebtn.setDisable(true);
                        productDelete_btn.setDisable(true);
                        addProductShowListData();
                        clearTextFields(dashboard_product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        productDelete_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer id = Integer.valueOf(addProduct_id.getText());
                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
                String name = addProduct_name_tf.getText();
                String price = addProduct_salesprice_tf.getText();
                String status = cb_status.getSelectionModel().getSelectedItem();
                String imageUrl = addproduct_imageview.getImage().getUrl();
                String importedPrice = addProduct_importedprice_tf.getText();
                if(brand != null && type != null && name != null && price != null
                        && imageUrl != null && status != null){
                    int brandId = new SupplierModel().getIdSupplier(brand);
                    int typeId = new ProductCategoryModel().getProductCategoryId(type);
                    try {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure want to delete " + name + "\nId: " + id);
                        Optional<ButtonType> option = alert.showAndWait();
                        if(option.get().equals(ButtonType.OK)){
                            Product product = new Product(id, name, brand, brandId, type, typeId, Double.parseDouble(price), status, imageUrl,Double.parseDouble(importedPrice));
                            DBDelete(product);
                            addProduct_addBtn.setDisable(false);
                            addProduct_updatebtn.setDisable(true);
                            productDelete_btn.setDisable(true);
                            addProductShowListData();
                            clearTextFields(dashboard_product);
                        }else return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        observableList.addListener((InvalidationListener) observable -> addProductShowListData());


        // Storage Controller begin here
        storage_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(true);
            dashboard_product.setVisible(false);

            storageList();

            storage_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
            product_btn.setStyle("-fx-background-color: transparent");
        });

        importProduct_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/importProduct.fxml", "Import Product");
                System.out.println("Import Product on progress...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("reset table");
            storageList();
        });

        history_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/importHistory.fxml", "Import History");
                System.out.println("Viewing History");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("reset table");
            storageList();
        });




        //

        change_pass_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/change-pass.fxml", "Change Password");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        update_account_info.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/update-info.fxml", "Update User Information");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


        // General Controller functions

        void CancelAction (Button cancelBtn){
            cancelBtn.setOnAction(event -> {

                // Get a reference to the cancel button's stage (window)
                addProduct_addBtn.setDisable(false);
                addProduct_updatebtn.setDisable(true);
                productDelete_btn.setDisable(true);
                tblv_product.getSelectionModel().clearSelection();
                clearTextFields(dashboard_product);
            });
        }

    static void DBAdd(Product product) {
        boolean check = new ProductModel().addProduct(product);
        System.out.println(check);

        if (check) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Add product successfully");
            success.showAndWait();
        } else {
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }
    static void DBUpdate(Product product) {
        boolean check = new ProductModel().updateProduct(product);
        System.out.println("Update Product: " + product.getName());
        System.out.println("Id: " + product.getId());
        System.out.println("Status: " + check);
        if(check){
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Update product successfully");
            success.showAndWait();
        }else{
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }
    static void DBDelete(Product product){
        boolean check = new ProductModel().deleteProduct(product);
        if(check){
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Update product successfully");
            success.showAndWait();
        }else{
            Alert failed = new Alert(Alert.AlertType.INFORMATION);
            failed.setContentText("Something went wrong. Please try again");
            failed.showAndWait();
        }
    }

    public void chart() {
        sale_revenue_chart.getData().clear();

        String sql = "SELECT SUM(total_price), date_recorded FROM invoice GROUP BY date_recorded ORDER BY TIMESTAMP(date_recorded) ASC LIMIT 8";

        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {

            XYChart.Series<String, Double> chartData = new XYChart.Series<>();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                chartData.getData().add(new XYChart.Data<>(rs.getString(2), rs.getDouble(1)));
            }

            XYChart<String, Double> saleRevenueChart = (XYChart<String, Double>) sale_revenue_chart;
            saleRevenueChart.getData().add(chartData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username_label.setText(user);
    }

//    public void displaySalesInfo() {
//        String sql = "SELECT total_paid, COUNT(o.id) FROM invoice i INNER JOIN order o ON i.order_id = o.id";
//        try (Connection con = JDBCConnect.getJDBCConnection();
//        PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                earning_text.setText(rs.getString("total_paid"));
//
//                total_order_text.setText(rs.getString("o.id"));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void addProductShowListData() {
        observableList = new ProductModel().getProductList();
        product_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        product_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblv_product.setItems(observableList);
    }
    public void storageList(){
        ObservableList<Product> storageList = new ProductModel().getProductList();
        goods_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        goods_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        good_col_supplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        goods_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        goods_col_price.setCellValueFactory(new PropertyValueFactory<>("importedPrice"));
        goods_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        goods_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        goods_col_total.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            DoubleBinding totalBinding = Bindings.createDoubleBinding(() ->
                            product.getImportedPrice() * product.getQuantityInStock(),
                    product.unitPriceProperty(),
                    product.getQuantityInStockProperty()
            );
            return totalBinding.asObject();
        });
        tbv_goods.setItems(storageList);
    }

    private void openModalWindow(String resource, String title) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

    public void viewProfile() {
        String viewAccountSql = "SELECT username, email, phone, details, password FROM users";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(viewAccountSql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                display_username.setText(rs.getString("username"));
                display_email.setText(rs.getString("email"));
                display_phone.setText(rs.getString("phone"));
                display_pass.setText(rs.getString("password"));
                display_detail.setText(rs.getString("details"));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    public void addProductImportImage() {
        Image image = null;
        String currentPath = System.getProperty("user.dir");
        FileChooser open = new FileChooser();
        open.setTitle("Open image file");
        open.setInitialDirectory(new File(currentPath + "\\src\\main\\resources\\controller\\images"));
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(dashboard_product.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString());
            addproduct_imageview.setImage(image);
        }
        return;
    }

    private void clearTextFields(Parent parent) {
        if (parent == null) return;

        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.clear();
            } else if (node instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) node;
                comboBox.getSelectionModel().clearSelection();
            } else if (node instanceof Parent) {
                clearTextFields((Parent) node); // Recursively clear TextFields in child nodes
            }
            else if (node instanceof ImageView){
                ImageView imageView = (ImageView) node;
                imageView.setImage(null);
            }
        }
    }
    private boolean checkImageUrl(String url){
        String regex
                = "(\\S+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (url == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(url);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }
}

