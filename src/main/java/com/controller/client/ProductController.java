package com.controller.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;

public class ProductController {

    @FXML
    private AnchorPane addProductScene;

    @FXML
    private Button addProduct_addBtn;

    @FXML
    private ComboBox<?> addProduct_brand_cb;

    @FXML
    private TextField addProduct_name_tf;

    @FXML
    private Button addProduct_newBrand_btn;

    @FXML
    private Button addProduct_newType_btn;

    @FXML
    private TextField addProduct_price_tf;

    @FXML
    private ComboBox<?> addProduct_type_cb;

    @FXML
    private ImageView addproduct_imageview;

    @FXML
    private AnchorPane dashboard_addproduct;
    private Image image;

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
