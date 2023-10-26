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
    private Button accept_btn;

    @FXML
    private Button cancel_btn;

    @FXML
    private TextArea customer_address_ta;

    @FXML
    private TableColumn<Customer, String> customer_col_address;

    @FXML
    private TableColumn<Customer, String> customer_col_email;

    @FXML
    private TableColumn<Customer, Integer> customer_col_id;

    @FXML
    private TableColumn<Customer, String> customer_col_name;

    @FXML
    private TableColumn<Customer, String> customer_col_phone;

    @FXML
    private TextField customer_email_tf;

    @FXML
    private TextField customer_name_tf;

    @FXML
    private Pagination customer_pag;

    @FXML
    private TextField customer_phone_tf;
    @FXML
    private TextField customer_id_tf;
    @FXML
    private TableView<Customer> customer_tb;
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
        customer_pag.setPageCount(pageCount);
        customer_pag.setPageFactory(pageIndex -> {
            setDataTable(pageIndex * ITEMS_PER_PAGE, Math.min(pageIndex * ITEMS_PER_PAGE, CustomerModel.getNumberRecords() - (pageIndex * ITEMS_PER_PAGE)), pageIndex);
            return customer_tb;
        });
        customer_tb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                customer_id_tf.setText(String.valueOf(newValue.getId()));
                customer_name_tf.setText(newValue.getName());
                customer_email_tf.setText(newValue.getEmail());
                customer_phone_tf.setText(newValue.getPhone());
                customer_address_ta.setText(newValue.getAddress());
            } else {
                resetField();
            }
        });
        accept_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (validateFields()) {
                    customer = new Customer();
                    customer.setId(Integer.parseInt(customer_id_tf.getText()));
                    customer.setName(customer_name_tf.getText());
                    customer.setEmail(customer_email_tf.getText());
                    customer.setPhone(customer_phone_tf.getText());
                    customer.setAddress(customer_address_ta.getText());
                    Stage stage = (Stage) accept_btn.getScene().getWindow();
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
        customer_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        customer_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        customer_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        customer_col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customer_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        customer_tb.setItems(customerObservableList);
    }

    public void resetField() {
        customer_name_tf.setText(null);
        customer_email_tf.setText(null);
        customer_col_phone.setText(null);
        customer_address_ta.setText(null);
    }

    public boolean validateFields() {
        return !customer_email_tf.getText().isBlank() && !customer_name_tf.getText().isBlank() &&
                !customer_phone_tf.getText().isBlank() && !customer_address_ta.getText().isBlank();
    }
}
