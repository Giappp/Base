package com.controller.client;

import com.entities.Supplier;
import com.model.SupplierModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class newSupplier implements Initializable {
    @FXML
    private Button add_btn;

    @FXML
    private Button cancel_btn;

    @FXML
    private Button delete_btn;

    @FXML
    private TextArea ta_supplier_address;

    @FXML
    private TextField tf_supplier_email;

    @FXML
    private TextField tf_supplier_id;

    @FXML
    private TextField tf_supplier_name;

    @FXML
    private TextField tf_supplier_phone;
    @FXML
    private TableView<Supplier> tv_supplier;

    @FXML
    private TableColumn<Supplier, String> tv_supplier_address;

    @FXML
    private TableColumn<Supplier, String> tv_supplier_email;

    @FXML
    private TableColumn<Supplier, String> tv_supplier_id;

    @FXML
    private TableColumn<Supplier, String> tv_supplier_name;

    @FXML
    private TableColumn<Supplier, String> tv_supplier_phone;

    @FXML
    private Button update_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTable();
        add_btn.setDisable(false);
        cancel_btn.setDisable(false);
        update_btn.setDisable(true);
        delete_btn.setDisable(true);

        add_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf_supplier_name.getText().trim();
                String address = ta_supplier_address.getText().trim();
                String phone = tf_supplier_phone.getText().trim();
                String email = tf_supplier_email.getText().trim();
                if(!name.isBlank() && !address.isBlank() && !phone.isBlank() && !email.isBlank()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Add " + name + " To Supplier lists?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
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
            }
        });
        delete_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf_supplier_name.getText().trim();
                String address = ta_supplier_address.getText().trim();
                String phone = tf_supplier_phone.getText().trim();
                String email = tf_supplier_email.getText().trim();
                Integer id = Integer.valueOf(tf_supplier_id.getText());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Delete " + name);
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        Supplier supplier = new Supplier(id,name,address,phone,email);
                        DBDelete(supplier);
                        showTable();
                        clearTextField();
                }
            }
        });

        update_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf_supplier_name.getText().trim();
                String address = ta_supplier_address.getText().trim();
                String phone = tf_supplier_phone.getText().trim();
                String email = tf_supplier_email.getText().trim();
                Integer id = Integer.valueOf(tf_supplier_id.getText());
                if(!name.isBlank() && !address.isBlank() && !phone.isBlank() && !email.isBlank()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Add " + name + " To Supplier lists?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
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
            }
        });
        cancel_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearTextField();
            }
        });
        tv_supplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                tf_supplier_id.setText(String.valueOf(newValue.getId()));
                tf_supplier_name.setText(newValue.getName());
                tf_supplier_phone.setText(newValue.getPhone());
                tf_supplier_email.setText(newValue.getEmail());
                ta_supplier_address.setText(newValue.getAddress());
                add_btn.setDisable(true);
                delete_btn.setDisable(false);
                update_btn.setDisable(false);
            }else{
                clearTextField();
            }
        });
    }

    private void clearTextField() {
        tf_supplier_name.clear();
        tf_supplier_id.clear();
        tf_supplier_email.clear();
        tf_supplier_id.clear();
        tf_supplier_phone.clear();
        ta_supplier_address.clear();
        add_btn.setDisable(false);
        delete_btn.setDisable(true);
        update_btn.setDisable(true);
    }


    public void showTable(){
        ObservableList<Supplier> supplierObservableList = FXCollections.observableList(new SupplierModel().getData());
        tv_supplier_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_supplier_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tv_supplier_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tv_supplier_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tv_supplier_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tv_supplier.setItems(supplierObservableList);
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
