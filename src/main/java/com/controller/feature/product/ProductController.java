package com.controller.feature.product;

import com.controller.AlertMessages;
import com.controller.feature.allfile;
import com.db.dao.JDBCConnect;
import com.entities.Product;
import com.model.ProductCategoryModel;
import com.model.SupplierModel;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController {

    @FXML
    private TableColumn<Product, Float> productColImportedPrice;

    @FXML
    private AnchorPane dashboardProduct;

    @FXML
    private AnchorPane updateScene;

    @FXML
    private TableView<Product> tblvProduct;

    @FXML
    private TableColumn<Product, Integer> productColId;

    @FXML
    private TableColumn<Product, String> productColType;

    @FXML
    private TableColumn<Product, String> productColBrand;

    @FXML
    private TableColumn<Product, String> productColName;

    @FXML
    private TableColumn<Product, Integer> productColAmount;

    @FXML
    private TableColumn<Product, Float> productColPrice;

    @FXML
    private TableColumn<Product, String> productColStatus;

    @FXML
    private Text txtProductId;

    @FXML
    private Button addProductUpdatebtn;

    @FXML
    private TextField addProductId;

    @FXML
    private Button productDeleteBtn;

    @FXML
    private Pagination productPg;

    @FXML
    private TextField addProductImportedpriceTf;
    
    @FXML
    private AnchorPane addProductScene;

    @FXML
    private Button addProductAddBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> addProductBrandCb;

    @FXML
    private TextField addProductNameTf;

    @FXML
    private Button addProductNewBrandBtn;

    @FXML
    private Button addProductNewTypeBtn;

    @FXML
    private TextField addProductSalespriceTf;

    @FXML
    private ComboBox<String> addProductTypeCb;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ImageView addproductImageView;

    private String imageUrl;

    AlertMessages alertMessages;

    private final int currentPage = 1;

    ObservableList<Product> carObservableList = FXCollections.observableArrayList();

    private static final int itemsPerPage = 12; // final variable to specify number of items per page

    public void initialize() {

    }

    private ObservableList<Product> getListProduct() {
        ObservableList<Product> observableList =FXCollections.observableArrayList();
        String sql = "SELECT p.id, pc.name AS typeName, s.name AS brandName, p.name, p.quantityInStock, p.salePrice, p.importedPrice, p.`status`" +
                "FROM product AS p" +
                "LEFT JOIN product_category AS pc ON p.productTypeId = pc.id" +
                "LEFT JOIN supplier AS s ON p.supplierId = s.id" +
                "ORDER BY p.id";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String typeName = rs.getString("typeName");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ObservableList<String> listbrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
//        ObservableList<String> listtypes = FXCollections.observableArrayList(new ProductCategoryModel().getType());
//        addProduct_brand_cb.setItems(listbrands);
//        addProduct_type_cb.setItems(listtypes);
//
//        addProduct_addBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
//                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
//                String name = addProduct_name_tf.getText();
//                String price = addProduct_salesprice_tf.getText();
//
//                if (brand != null && type != null && name != null && price != null
//                        && imageUrl != null) {
//                    int brandId = new SupplierModel().getIdSupplier(brand);
//                    int typeId = new ProductCategoryModel().getProductCategoryId(type);
//
//                    try {
//                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                        alert.setTitle("Confirmation Message");
//                        alert.setHeaderText(null);
//                        alert.setContentText("Add " + name + " To Product lists?");
//                        Optional<ButtonType> option = alert.showAndWait();
//
//                        alertMessages.confirmationMessage("Add " + name + " To Product lists?");
//
//                        if (option.get().equals(ButtonType.OK)) {
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Alert inValid = new Alert(Alert.AlertType.ERROR);
//                    inValid.setContentText("Please fill product information correctly");
//                    inValid.showAndWait();
//                }
//            }
//        });
//
//        new allfile().CancelAction(cancel_btn);
//    }
//
//    @FXML
//    public void addProductImportImage() {
//        Image image = null;
//        String currentPath = System.getProperty("user.dir");
//        FileChooser open = new FileChooser();
//        open.setTitle("Open image file");
//        open.setInitialDirectory(new File(currentPath + "\\src\\main\\resources\\controller\\images"));
//        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));
//
//        File file = open.showOpenDialog(addProductScene.getScene().getWindow());
//
//        if (file != null) {
//            image = new Image(file.toURI().toString(), 150, 130, false, true);
//            addproduct_imageview.setImage(image);
//            imageUrl = image.getUrl();
//        }
//    }
}
