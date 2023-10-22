package com.controller.feature.customer;

import com.controller.AlertMessages;
import com.db.dao.JDBCConnect;
import com.entities.Customer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Pattern;

public class CustomerController {

    @FXML
    private Text actionStatusLabel;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private AnchorPane dashboardCustomer;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField tfAddCusname;

    @FXML
    private TextField tfAddAddress;

    @FXML
    private TextField tfAddPhone;

    @FXML
    private TextField tfAddEmail;

    @FXML
    private Button addCustomerBtn;

    @FXML
    private Button updateCustomerBtn;

    @FXML
    private Button cancelCustomerBtn;

    @FXML
    private Button deleteCustomerBtn;

    @FXML
    private TableView<Customer> customerTblv;

    @FXML
    private TableColumn<Customer, Integer> customerColId;

    @FXML
    private TableColumn<Customer, Integer> customerColOrderNumber;

    @FXML
    private TableColumn<Customer, String> customerColName;

    @FXML
    private TableColumn<Customer, String> customerColEmail;

    @FXML
    private TableColumn<Customer, String> customerColPhone;

    @FXML
    private TableColumn<Customer, String> customerColAddress;

    @FXML
    private TextField searchCustomer;

    @FXML
    private Pagination customerPg;

    private final int currentPage = 1;

    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    private final int itemsPerPage = 12;

    AlertMessages alertMessages;

    public void initialize() {
        setupTable();
        setupPagination();
        setIdAdd();
        selectedRecord();
        addSearchByToComboBox();

        tfAddPhone.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*"); // Check if the given string contains only digits
    }

    private void addSearchByToComboBox() {
        searchComboBox.getItems().addAll("Name", "Email", "Phone");
        searchComboBox.setValue("Name");
    }

    private ObservableList<Customer> getCustomerObservableList() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        String sql = "SELECT id, name, address, phone, email FROM customer";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                observableList.add(new Customer(id, name, address, phone, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    private void setupTable() {
        customerObservableList = getCustomerObservableList();
        customerColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerColOrderNumber.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(customerTblv.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        // Create filter list
        FilteredList<Customer> filteredList = new FilteredList<>(customerObservableList, b -> true);

        // listen to the changes in the search_customer to update table view
        searchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(Customer -> {

                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchComboBox.getValue().toLowerCase();

                if (searchBy.equals("name") && Customer.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("phone") && Customer.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else return searchBy.equals("email") && Customer.getEmail().toLowerCase().contains(searchKeyword);
            });
            // update pagination
            updatePagination(filteredList);
        });
        // update pagination
        updatePagination(filteredList);
        updateCustomerBtn.setDisable(true);
    }

    private void setupPagination() {
        int totalPages = (customerObservableList.size() / itemsPerPage) + (customerObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        customerPg.setPageCount(totalPages); // set page count for pagination

        customerPg.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    private void updatePagination(FilteredList<Customer> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        if (pageCount == 0) {
            pageCount = 1;
        }

        customerPg.setPageCount(pageCount);
        if (customerPg.getCurrentPageIndex() >= pageCount) {
            customerPg.setCurrentPageIndex(pageCount - 1);
        }

        // update table view base on current page
        int fromIndex = customerPg.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTblv.comparatorProperty());

        customerTblv.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, customerObservableList.size());

        // Clear the table and re-add the events for the current page
        customerTblv.getItems().clear();
        customerTblv.getItems().addAll(customerObservableList.subList(fromIndex, toIndex));
    }

    public void deleteCustomerFromDatabase() {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {

            ps.setString(1, idTextField.getText());

            int rowAffected = ps.executeUpdate();

            if (rowAffected > 0) {
                alertMessages.successMessage("Deleted successfully!");

                setupTable();
                resetForm();
            }
        } catch (Exception e) {
            alertMessages.errorMessage("Cannot delete!");
            e.printStackTrace();
        }
    }

    private int setIdAdd() {
        String sql = "SELECT id FROM customer ORDER BY id DESC LIMIT 1";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1) + 1;
                idTextField.setText(String.valueOf(id));
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addCustomerToDatabase() {
        String sql = "INSERT INTO customer(name, address, phone, email) VALUES (?,?,?,?,)";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {

            if (isFilledFields() && validateEmail() && validatePhoneNumber()) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, tfAddEmail.getText());
                preparedStatement.setString(2, tfAddAddress.getText());
                preparedStatement.setString(3, tfAddPhone.getText());
                preparedStatement.setString(4, tfAddEmail.getText());

                preparedStatement.executeUpdate();

                alertMessages.successMessage("Added successfully!");

                // update table view and reset form
                setupTable();
                resetForm();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerToDatabase() {
        String sql = "UPDATE customer SET name = ?,address = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {

            if (isFilledFields() && validatePhoneNumber() && validateEmail()) {
                ps.setString(1, tfAddCusname.getText());
                ps.setString(2, tfAddAddress.getText());
                ps.setString(3, tfAddPhone.getText());
                ps.setString(4, tfAddEmail.getText());
                ps.setString(6, idTextField.getText());

                ps.executeUpdate();

                alertMessages.successMessage("Updated successfully!");

                // update table view and reset form
                setupTable();
                resetForm();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetForm() {
        searchCustomer.clear();
        setIdAdd();
        tfAddCusname.clear();
        tfAddAddress.clear();
        tfAddPhone.clear();
        tfAddEmail.clear();

        actionStatusLabel.setText("Adding New Customer");
        addCustomerBtn.setDisable(false);
        updateCustomerBtn.setDisable(true);
    }

    private void selectedRecord() {
        customerTblv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observableValue, Customer oldValue, Customer newValue) {
                if (newValue != null) {
                    idTextField.setText(String.valueOf(newValue.getId()));
                    tfAddCusname.setText(String.valueOf(newValue.getName()));
                    tfAddPhone.setText(String.valueOf(newValue.getPhone()));
                    tfAddAddress.setText(newValue.getAddress());
                    tfAddEmail.setText(String.valueOf(newValue.getEmail()));

                    addCustomerBtn.setDisable(true);
                    actionStatusLabel.setText("Updating Customer");
                    updateCustomerBtn.setDisable(false);
                } else {
                    resetForm();
                }
            }
        });
    }

    private boolean isFilledFields() {
        if (tfAddCusname.getText().isEmpty()
                || tfAddPhone.getText().isEmpty()
                || tfAddEmail.getText().isEmpty()
                || tfAddAddress.getText().isEmpty()) {
            alertMessages.warningMessage("Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = tfAddPhone.getText().trim();
        if (!Pattern.compile("^0\\d{9}$").matcher(phoneNumber).matches()) {
            alertMessages.warningMessage("Phone number must start with '0' and be 10 digits long.");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = tfAddEmail.getText().trim();
        if (!Pattern.compile("\\S+@\\S+\\.\\S+").matcher(email).matches()) {
            alertMessages.warningMessage("Invalid email format.");
            return false;
        }
        return true;
    }
}
