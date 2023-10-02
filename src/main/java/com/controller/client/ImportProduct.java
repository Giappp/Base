package com.controller.client;

import com.model.ProductModel;
import com.model.SupplierModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportProduct implements Initializable {

    @FXML
    private Spinner<Integer> amount_sp;

    @FXML
    private Button cancel_btn;

    @FXML
    private Button import_btn;

    @FXML
    private TextField importedPrice_tf;

    @FXML
    private Button newSupplier_btn;

    @FXML
    private ComboBox<String> product_cb;

    @FXML
    private ComboBox<String> supplier_cb;
    @FXML
    private Label total_lb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
        supplier_cb.setItems(listBrands);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        amount_sp.setValueFactory(valueFactory);
        amount_sp.setEditable(true);
        // Set the value factory for the Spinner

        supplier_cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String supplierName = supplier_cb.getValue();
                if(supplierName != null){
                    Integer supplierId = new SupplierModel().getIdSupplier(supplierName);
                    ObservableList<String> listProducts = FXCollections.observableArrayList(new ProductModel().getNameProductFromSupplier(supplierId));
                    product_cb.setItems(listProducts);
                }
            }
        });
        supplier_cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null){
                    System.out.println(observable);
                    System.out.println(oldValue);
                    System.out.println(newValue);
                    Integer supplierId = new SupplierModel().getIdSupplier(newValue);
                    ObservableList<String> listProducts = FXCollections.observableArrayList(new ProductModel().getNameProductFromSupplier(supplierId));
                    product_cb.setItems(listProducts);
                }
            }
        });

        product_cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String productName = product_cb.getValue();
                if(productName != null){
                    Integer productId = new ProductModel().getIdProduct(productName);
                    Double price = new ProductModel().getImportedPrice(productId);
                    importedPrice_tf.setText(String.valueOf(price));
                }
            }
        });
        product_cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null){
                    System.out.println(observable);
                    System.out.println(oldValue);
                    System.out.println(newValue);
                    Integer productId = new ProductModel().getIdProduct(newValue);
                    Double price = new ProductModel().getImportedPrice(productId);
                    importedPrice_tf.setText(String.valueOf(price));
                    total_lb.setText("");
                    amount_sp.getEditor().setText("0");
                }
            }
        });
        amount_sp.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if(importedPrice_tf != null){
                    Double totalValue = newValue * Double.parseDouble(importedPrice_tf.getText());
                    total_lb.setText(String.valueOf(totalValue));
                    total_lb.setStyle("-fx-text-fill: red;" +
                            "-fx-font-weight: bold");
                }
            }
        });
        amount_sp.getEditor().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(importedPrice_tf != null){
                    try {
                        int amount = Integer.parseInt(amount_sp.getEditor().getText());
                        double price = Double.parseDouble(importedPrice_tf.getText());

                        double totalValue = amount * price;
                        total_lb.setText(String.valueOf(totalValue) + "$");
                        total_lb.setStyle("-fx-text-fill: red;" +
                                "-fx-font-weight: bold");

                        System.out.println("Amount: " + amount);
                        System.out.println("Price: " + price);
                        System.out.println("Total Value: " + totalValue);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for price.");
                    }
                }
            }
        });
    }
}
