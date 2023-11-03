package com.controller.storage;

import com.entities.GoodsImport;
import com.entities.Product;
import com.model.GoodsImportModel;
import com.model.ProductModel;
import com.model.SupplierModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ImportProduct implements Initializable {

    Parent root;

    Scene fxmlFile;

    Stage window;

    @FXML
    private Spinner<Integer> amountSp;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button importBtn;

    @FXML
    private TextField importedPriceTf;

    @FXML
    private Button newSupplierBtn;

    @FXML
    private ComboBox<String> productCb;

    @FXML
    private ComboBox<String> supplierCb;

    @FXML
    private Label totalLb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listBrands = FXCollections.observableArrayList(new SupplierModel().getBrands());
        supplierCb.setItems(listBrands);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        amountSp.setValueFactory(valueFactory);
        amountSp.setEditable(true);
        // Set the value factory for the Spinner

        supplierCb.setOnAction(event -> {
            String supplierName = supplierCb.getValue();
            if(supplierName != null){
                Integer supplierId = new SupplierModel().getIdSupplier(supplierName);
                ObservableList<String> listProducts = FXCollections.observableArrayList(new ProductModel().getNameProductFromSupplier(supplierId));
                productCb.setItems(listProducts);
            }
        });

        productCb.setOnAction(event -> {
            String productName = productCb.getValue();
            if(productName != null){
                Integer productId = new ProductModel().getIdProduct(productName);
                Double price = new ProductModel().getImportedPrice(productId);
                importedPriceTf.setText(String.valueOf(price));
            }
        });

        amountSp.getEditor().setOnAction(event -> {
            if(importedPriceTf != null){
                try {
                    int amount = Integer.parseInt(amountSp.getEditor().getText());
                    double price = Double.parseDouble(importedPriceTf.getText());

                    double totalValue = amount * price;
                    totalLb.setText(String.valueOf(totalValue));
                    totalLb.setStyle("-fx-text-fill: red;" +
                            "-fx-font-weight: bold");

                    System.out.println("Amount: " + amount);
                    System.out.println("Price: " + price);
                    System.out.println("Total Value: " + totalValue);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for price.");
                }
            }
        });
        amountSp.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                int amount = Integer.parseInt(amountSp.getEditor().getText());
                double price = Double.parseDouble(importedPriceTf.getText());

                double totalValue = amount * price;
                totalLb.setText(String.valueOf(totalValue));
            }
        });

        supplierCb.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                System.out.println(observable);
                System.out.println(oldValue);
                System.out.println(newValue);
                Integer supplierId = new SupplierModel().getIdSupplier(newValue);
                ObservableList<String> listProducts = FXCollections.observableArrayList(new ProductModel().getNameProductFromSupplier(supplierId));
                productCb.setItems(listProducts);
            }
        });

        productCb.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                System.out.println(observable);
                System.out.println(oldValue);
                System.out.println(newValue);
                Integer productId = new ProductModel().getIdProduct(newValue);
                Double price = new ProductModel().getImportedPrice(productId);
                importedPriceTf.setText(String.valueOf(price));
                totalLb.setText("");
                amountSp.getEditor().setText("0");
            }
        });

        amountSp.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(importedPriceTf != null){
                Double totalValue = newValue * Double.parseDouble(importedPriceTf.getText());
                totalLb.setText(String.valueOf(totalValue));
                totalLb.setStyle("-fx-text-fill: red;" +
                        "-fx-font-weight: bold");
            }
        });

        importBtn.setOnAction(event -> {
            String supplierName = supplierCb.getValue();
            String productName = productCb.getValue();
            Double importedPrice = Double.valueOf(importedPriceTf.getText());
            Integer amount = amountSp.getValue();
            Double totalPrice = Double.valueOf(totalLb.getText());
            if(supplierName != null && productName != null){
                Integer productId = new ProductModel().getIdProduct(productName);
                Product product = new ProductModel().getProduct(productId);
                Date date = Date.valueOf(LocalDate.now());
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Import " + amount + " " + productName +" from " + supplierName
                    + " with total of " + totalPrice + "$");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                        GoodsImport goodsImport = new GoodsImport(productId,product,amount,importedPrice,totalPrice,date,1);
                        boolean check = new GoodsImportModel().importGoods(goodsImport);
                        System.out.println(check);

                        if (check) {
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setContentText("Import product successfully");
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
                inValid.setContentText("Please fill all the information correctly");
                inValid.showAndWait();
            }
        });

        cancelBtn.setOnAction(event -> {
            supplierCb.getEditor().clear();
            productCb.getEditor().clear();
            importedPriceTf.clear();
            amountSp.getEditor().clear();
        });

        newSupplierBtn.setOnAction(event -> {
            try {
                openModalWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> newSuppliers = FXCollections.observableArrayList(new SupplierModel().getBrands());
            supplierCb.setItems(newSuppliers);
        });
    }

    private void openModalWindow () throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/controller/client/newSupplier.fxml")));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle("Supplier Management");
        window.showAndWait();
    }
}
