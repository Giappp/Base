package com.controller.client;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private AnchorPane addProductScene;

    @FXML
    private Button addProduct_addBtn;

    @FXML
    private ComboBox<String> addProduct_brand_cb;

    @FXML
    private TextField addProduct_name_tf;

    @FXML
    private Button addProduct_newBrand_btn;

    @FXML
    private Button addProduct_newType_btn;

    @FXML
    private TextField addProduct_price_tf;

    @FXML
    private ComboBox<String> addProduct_type_cb;

    @FXML
    private ImageView addproduct_imageview;

    @FXML
    private AnchorPane dashboard_addproduct;
    private Image image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listbrands= FXCollections.observableArrayList(new SupplierModel().getBrands());
        ObservableList<String> listtypes = FXCollections.observableArrayList(new ProductCategoryModel().getType());
        addProduct_brand_cb.setItems(listbrands);
        addProduct_type_cb.setItems(listtypes);
        addProduct_addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String brand = addProduct_brand_cb.getSelectionModel().getSelectedItem();
                String type = addProduct_type_cb.getSelectionModel().getSelectedItem();
                String name= addProduct_name_tf.getText();
                Double price = Double.parseDouble(addProduct_price_tf.getText());
                int brandId = new SupplierModel().getIdSupplier(brand);
                int typeId = new ProductCategoryModel().getProductCategoryId(type);
                Product product = new Product(name,brandId,typeId,price,"0",image.getUrl());
                boolean check = new ProductModel().addProduct(product);
                System.out.println(check);
            }
        });
    }

    @FXML
    public void addProductImportImage(){
        String currentPath = System.getProperty("user.dir");
        FileChooser open = new FileChooser();
        open.setTitle("Open image file");
        open.setInitialDirectory(new File(currentPath + "\\src\\main\\resources\\controller\\images"));
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*jpg","*png"));

        File file = open.showOpenDialog(addProductScene.getScene().getWindow());

        if(file != null){
            image = new Image(file.toURI().toString(), 150 ,130, false, true);
            addproduct_imageview.setImage(image);
        }
    }

}
