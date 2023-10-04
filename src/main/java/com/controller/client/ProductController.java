package com.controller.client;

import com.controller.logSign.DBController;
import com.entities.Product;
import com.entities.ProductCategory;
import com.entities.Supplier;
import com.model.ProductCategoryModel;
import com.model.ProductModel;
import com.model.SupplierModel;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private AnchorPane addProductScene;

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
    private TextField addProduct_importedprice_tf;

    @FXML
    private ComboBox<String> addProduct_type_cb;

    @FXML
    private ImageView addproduct_imageview;

    @FXML
    private AnchorPane dashboard_addproduct;

    private String imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listbrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
        ObservableList<String> listtypes = FXCollections.observableArrayList(new ProductCategoryModel().getType());
        addProduct_brand_cb.setItems(listbrands);
        addProduct_type_cb.setItems(listtypes);

        addProduct_addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
                String name = addProduct_name_tf.getText();
                String price = addProduct_salesprice_tf.getText();
                String importedPrice = addProduct_importedprice_tf.getText();

                if (brand != null && type != null && name != null && price != null
                        && imageUrl != null && importedPrice != null) {
                    int brandId = new SupplierModel().getIdSupplier(brand);
                    int typeId = new ProductCategoryModel().getProductCategoryId(type);

                    try {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Add " + name + " To Product lists?");
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get().equals(ButtonType.OK)) {
                            Product product = new Product(name,brandId,typeId,0,Double.parseDouble(price), Double.parseDouble(importedPrice),imageUrl);
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

        cancel_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to cancel the current product?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        // Get a reference to the cancel button's stage (window)
                        Stage stage = (Stage) cancel_btn.getScene().getWindow();
                        stage.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void addProductImportImage() {
        Image image = null;
        String currentPath = System.getProperty("user.dir");
        FileChooser open = new FileChooser();
        open.setTitle("Open image file");
        open.setInitialDirectory(new File(currentPath + "\\src\\main\\resources\\controller\\images"));
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg", "*png"));

        File file = open.showOpenDialog(addProductScene.getScene().getWindow());

        if (file != null) {
            image = new Image(file.toURI().toString(), 150, 130, false, true);
            addproduct_imageview.setImage(image);
            imageUrl = image.getUrl();
        }
    }
}
