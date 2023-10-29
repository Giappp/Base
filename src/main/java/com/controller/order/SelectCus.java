package com.controller.order;

import com.controller.AlertMessages;
import com.entities.Customer;
import com.model.CustomerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectCus implements Initializable {

    private static final int ITEMS_PER_PAGE = 10;

    CustomerModel customerModel = new CustomerModel();

    @FXML
    private Button acceptBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextArea customerAddressTa;

    @FXML
    private TableColumn<Customer, String> customerColAddress;

    @FXML
    private TableColumn<Customer, String> customerColEmail;

    @FXML
    private TableColumn<Customer, Integer> customerColId;

    @FXML
    private TableColumn<Customer, String> customerColName;

    @FXML
    private TableColumn<Customer, String> customerColPhone;

    @FXML
    private TextField customerEmailTf;

    @FXML
    private TextField customerNameTf;

    @FXML
    private Pagination customerPag;

    @FXML
    private TextField customerPhoneTf;

    @FXML
    private TextField customerIdTf;

    @FXML
    private TableView<Customer> customerTb;

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int pageCount = (CustomerModel.getNumberRecords() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
        customerPag.setPageCount(pageCount);
        customerPag.setPageFactory(pageIndex -> {
            setDataTable(pageIndex * ITEMS_PER_PAGE, Math.min(pageIndex * ITEMS_PER_PAGE, CustomerModel.getNumberRecords() - (pageIndex * ITEMS_PER_PAGE)), pageIndex);
            return customerTb;
        });

        customerTb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                customerIdTf.setText(String.valueOf(newValue.getId()));
                customerNameTf.setText(newValue.getName());
                customerEmailTf.setText(newValue.getEmail());
                customerPhoneTf.setText(newValue.getPhone());
                customerAddressTa.setText(newValue.getAddress());
            } else {
                resetField();
            }
        });

        acceptBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validateFields()) {
                    customer = new Customer();
                    customer.setId(Integer.parseInt(customerIdTf.getText()));
                    customer.setName(customerNameTf.getText());
                    customer.setEmail(customerEmailTf.getText());
                    customer.setPhone(customerPhoneTf.getText());
                    customer.setAddress(customerAddressTa.getText());
                    Stage stage = (Stage) acceptBtn.getScene().getWindow();
                    stage.close();
                } else {
                    AlertMessages alertMessages = new AlertMessages();
                    alertMessages.errorMessage("Please fill all the information");
                }
            }
        });
    }

    public void setDataTable(int offset, int limit, int pageIndex) {
        ObservableList<Customer> customerObservableList = FXCollections.observableList(customerModel.getListCustomer(pageIndex * ITEMS_PER_PAGE, ITEMS_PER_PAGE));
        customerColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTb.setItems(customerObservableList);
    }

    public void resetField() {
        customerNameTf.setText(null);
        customerEmailTf.setText(null);
        customerPhoneTf.setText(null);
        customerAddressTa.setText(null);
    }

    public boolean validateFields() {
        return !customerEmailTf.getText().isBlank() && !customerNameTf.getText().isBlank() &&
                !customerPhoneTf.getText().isBlank() && !customerAddressTa.getText().isBlank();
    }
}
