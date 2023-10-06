package com.controller.client;

import com.controller.logSign.DBController;
import com.db.dao.JDBCConnect;
import com.entities.Product;
import com.model.ProductCategoryModel;
import com.model.ProductModel;
import com.model.SupplierModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.KeyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashBoardController implements Initializable {
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
    private AnchorPane dashboard_product;

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
    private Button addProduct_updatebtn;
    @FXML
    private TextField addProduct_id;
    @FXML
    private Text txt_product_id;

    private String imageUrl;

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

        // All the essentials initialization begin here

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


        // Storage Controller begin here
        storage_btn.setOnAction(event -> {
            dashboard_home.setVisible(false);
            dashboard_account.setVisible(false);
            dashboard_order.setVisible(false);
            dashboard_storage.setVisible(true);
            dashboard_product.setVisible(false);
            addProductShowListData();

            storage_btn.setStyle("-fx-background-color: #00203FFF;-fx-text-fill: #ADEFD1FF");
            account_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
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

        addProduct_addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
                String name = addProduct_name_tf.getText();
                String price = addProduct_salesprice_tf.getText();
                String status = cb_status.getSelectionModel().getSelectedItem();

                if (brand != null && type != null && name != null && price != null
                        && imageUrl != null && status != null) {
                    int brandId = new SupplierModel().getIdSupplier(brand);
                    int typeId = new ProductCategoryModel().getProductCategoryId(type);
                    try {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Add " + name + " To Product lists?");
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get().equals(ButtonType.OK)) {
                            Product product = new Product(name, brandId, typeId, 0, Double.parseDouble(price), 0.0, imageUrl, status);
                            DBAdd(product);
                            addProductShowListData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert inValid = new Alert(Alert.AlertType.ERROR);
                    inValid.setContentText("Please fill product information correctly");
                    inValid.showAndWait();
                }
            }
        });

        CancelAction(cancel_btn);
        tblv_product.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
                addProduct_brand_cb.setItems(listBrands);
                List<String> status = Arrays.asList("Available", "Unavailable");
                cb_status.setItems(FXCollections.observableList(status));
                ObservableList<String> listCategory = FXCollections.observableArrayList(new ProductCategoryModel().getType());
                addProduct_type_cb.setItems(listCategory);

                addProduct_addBtn.setVisible(false);
                addProduct_updatebtn.setVisible(true);
                if(checkImageUrl(newValue.getImage())){
                    Image img = new Image(newValue.getImage());
                    addproduct_imageview.setImage(img);
                }else{
                    String currentPath = System.getProperty("user.dir");
                    addproduct_imageview.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                }
                addProduct_type_cb.getSelectionModel().select(newValue.getProductType());
                addProduct_brand_cb.getSelectionModel().select(newValue.getSupplierName());
                addProduct_name_tf.setText(newValue.getName());
                addProduct_salesprice_tf.setText(String.valueOf(newValue.getSalePrice()));
                cb_status.getSelectionModel().select(newValue.getStatus());
                addProduct_id.setText(String.valueOf(newValue.getId()));
                addProduct_id.setVisible(true);
                txt_product_id.setVisible(true);
            } else {
                addProduct_addBtn.setVisible(true);
                addProduct_updatebtn.setVisible(false);
                addProduct_id.setVisible(false);
                txt_product_id.setVisible(false);
                tblv_product.getSelectionModel().clearSelection();
            }
        }));
        tblv_product.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Check if the click was in an empty area of the table
                if (tblv_product.getSelectionModel().isEmpty()) {
                    // Handle the click on an empty row here
                    System.out.println("Clicked on an empty row.");
                    // Perform any actions you want for clicking on an empty row
                }
            }
        });

        addProduct_updatebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer id = Integer.valueOf(addProduct_id.getText());
                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
                String name = addProduct_name_tf.getText();
                String price = addProduct_salesprice_tf.getText();
                String status = cb_status.getSelectionModel().getSelectedItem();
                imageUrl = addproduct_imageview.getImage().getUrl();
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
                            Product product = new Product(id,name,brand, brandId, type,typeId, Double.parseDouble(price), status,imageUrl);
                            DBUpdate(product);
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
            }
        });

        observableList.addListener((InvalidationListener) observable -> addProductShowListData());


        // Storage controller
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


        //

        change_pass_btn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/change-pass.fxml", "Change Password");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        order_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
//        order_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
//        order_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        order_col_type.setCellValueFactory(new PropertyValueFactory<>("productTypeId"));
//        order_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
//        order_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
//        order_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
//        tblv_orderView.setItems(observableList);
    }


    // General Controller functions

    void CancelAction(Button cancelBtn) {
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Get a reference to the cancel button's stage (window)
                addProduct_addBtn.setVisible(true);
                addProduct_updatebtn.setVisible(false);
                tblv_product.getSelectionModel().clearSelection();
                clearTextFields(dashboard_product);
            }
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

    public void addProductShowListData() {
        observableList = new ProductModel().getProductList();
        product_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
//        product_col_image.setCellValueFactory(param -> {
//            // Create an imageView based on the image URL in the data
//            ImageView imageView = new ImageView();
//            imageView.setFitHeight(50);
//            imageView.setFitWidth(50);
//
//            String imageUrl = param.getValue().getImage();
//            if(imageUrl != null){
//                Image image = new Image(imageUrl);
//                imageView.setImage(image);
//                return new SimpleObjectProperty<>(imageView);
//            }
//            return null;
//        });
        product_col_amount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_col_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblv_product.setItems(observableList);
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
            imageUrl = image.getUrl();
        }else{
            image = new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg");
            addproduct_imageview.setImage(image);
            imageUrl = image.getUrl();
        }
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
                = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

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
