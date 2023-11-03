package com.controller.product;

import com.entities.Supplier;
import com.model.SupplierModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class newSupplier implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextArea taSupplierAddress;

    @FXML
    private TextField tfSupplierEmail;

    @FXML
    private TextField tfSupplierId;

    @FXML
    private TextField tfSupplierName;

    @FXML
    private TextField tfSupplierPhone;

    @FXML
    private TableView<Supplier> tvSupplier;

    @FXML
    private TableColumn<Supplier, String> tvSupplierAddress;

    @FXML
    private TableColumn<Supplier, String> tvSupplierEmail;

    @FXML
    private TableColumn<Supplier, String> tvSupplierId;

    @FXML
    private TableColumn<Supplier, String> tvSupplierName;

    @FXML
    private TableColumn<Supplier, String> tvSupplierPhone;

    @FXML
    private Button updateBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTable();
        addBtn.setDisable(false);
        cancelBtn.setDisable(false);
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);

        addBtn.setOnAction(event -> {
            String name = tvSupplierName.getText().trim();
            String address = taSupplierAddress.getText().trim();
            String phone = tfSupplierPhone.getText().trim();
            String email = tfSupplierEmail.getText().trim();
            if(!name.isBlank() && !address.isBlank() && !phone.isBlank() && !email.isBlank()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Add " + name + " To Supplier lists?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    Supplier supplier = new Supplier(name,address,phone,email);
                    DBAdd(supplier);
                    showTable();
                    clearTextField();
                }
            }else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        deleteBtn.setOnAction(event -> {
            String name = tfSupplierName.getText().trim();
            String address = taSupplierAddress.getText().trim();
            String phone = tfSupplierPhone.getText().trim();
            String email = tfSupplierEmail.getText().trim();
            int id = Integer.parseInt(tfSupplierId.getText());

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Delete " + name);
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    Supplier supplier = new Supplier(id,name,address,phone,email);
                    DBDelete(supplier);
                    showTable();
                    clearTextField();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        updateBtn.setOnAction(event -> {
            String name = tfSupplierName.getText().trim();
            String address = taSupplierAddress.getText().trim();
            String phone = tfSupplierPhone.getText().trim();
            String email = tfSupplierEmail.getText().trim();
            int id = Integer.parseInt(tfSupplierId.getText());
            if(!name.isBlank() && !address.isBlank() && !phone.isBlank() && !email.isBlank()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Add " + name + " To Supplier lists?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                    Supplier supplier = new Supplier(id,name,address,phone,email);
                    DBUpdate(supplier);
                    showTable();
                    clearTextField();
                }
            }else {
                Alert inValid = new Alert(Alert.AlertType.ERROR);
                inValid.setContentText("Please fill product information correctly");
                inValid.showAndWait();
            }
        });

        cancelBtn.setOnAction(event -> clearTextField());

        tvSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                tfSupplierId.setText(String.valueOf(newValue.getId()));
                tfSupplierName.setText(newValue.getName());
                tfSupplierPhone.setText(newValue.getPhone());
                tfSupplierEmail.setText(newValue.getEmail());
                taSupplierAddress.setText(newValue.getAddress());
                addBtn.setDisable(true);
                deleteBtn.setDisable(false);
                updateBtn.setDisable(false);
            }else{
                clearTextField();
            }
        });
    }

    private void clearTextField() {
        tfSupplierName.clear();
        tfSupplierId.clear();
        tfSupplierEmail.clear();
        tfSupplierPhone.clear();
        taSupplierAddress.clear();
        addBtn.setDisable(false);
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
    }

    public void showTable(){
        ObservableList<Supplier> supplierObservableList = FXCollections.observableList(new SupplierModel().getData());
        tvSupplierId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tvSupplierPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tvSupplierEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tvSupplier.setItems(supplierObservableList);
    }

    public static void DBAdd(Supplier supplier){
        boolean check = new SupplierModel().insertSupplier(supplier);
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

    public static void DBDelete(Supplier supplier){
        boolean check = new SupplierModel().deleteSupplier(supplier);

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

    public static void DBUpdate(Supplier supplier){
        boolean check = new SupplierModel().updateSupplier(supplier);
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
}
