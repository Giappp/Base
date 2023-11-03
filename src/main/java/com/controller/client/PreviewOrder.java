package com.controller.client;

import com.controller.order.OrderController;
import com.entities.*;
import com.model.OrderManage;
import com.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class PreviewOrder implements Initializable {

    private final int itemPerPages = 8;

    @FXML
    private Button cancelButton;

    @FXML
    private Button cancelProductButton;

    @FXML
    private Button createOrderButton;

    @FXML
    private TextField cusAddress;

    @FXML
    private TextField cusEmail;

    @FXML
    private TextField cusName;

    @FXML
    private TextField cusPhone;
    @FXML
    private TextField cusId;

    @FXML
    private TableView<ProductInOrder> orderView;

    @FXML
    private TableColumn<ProductInOrder, Double> productPriceCol;

    @FXML
    private TableColumn<ProductInOrder, String> productColName;

    @FXML
    private TableColumn<ProductInOrder, Integer> productIdCol;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productQuantity;

    @FXML
    private TableColumn<ProductInOrder, Integer> productQuantityCol;

    @FXML
    private TableColumn<ProductInOrder, String> productSupplierCol;

    @FXML
    private TableColumn<ProductInOrder, Double> productTotalPrice;

    @FXML
    private Label totalItemsAll;

    @FXML
    private Label totalPrice;

    @FXML
    private Label totalPriceAll;

    @FXML
    private Button updateProductButton;

    @FXML
    private TextField searchProduct;

    @FXML
    private Pagination orderPag;

    private List<Product> cartList = new ArrayList<>();

    private List<ProductInOrder> productInOrderList = new ArrayList<>();

    private Customer customer = new Customer();

    private ProductInOrder currentProductInOrder = new ProductInOrder();

    private ObservableList<Product> products;

    private final Order order = new Order();

    private final Invoice invoice = new Invoice();

    private final boolean isDeleteColumnAdded = false;

    private TableColumn<ProductInOrder, Void> deleteButtonCol;

    private OrderController orderController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton();
        selectOrderView();
        setUpSearch();
        enterQuantity();
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setData(OrderController orderController,List<Product> cartList, List<ProductInOrder> productInOrderList, Customer customer, ObservableList<Product> products) {
        this.orderController = orderController;
        this.cartList = cartList;
        this.productInOrderList = productInOrderList;
        this.customer = customer;
        this.products = products;
        setDataCustomer();
        setDataForTable();
        setPriceAll();
        setTotalItems();
        setUpOrderPagination();
        setUpSearch();
        setUpOrder();
        setUpInvoice();
        setUpCreateOrderButton();
    }

    private void setUpOrder(){
        if(customer != null && !productInOrderList.isEmpty()){
            order.setCustomerId(customer.getId());
            int total = 0;
            for(ProductInOrder productInOrder:productInOrderList){
                total += productInOrder.getQuantity();
            }
            order.setTotalAmount(total);
            order.setStatus(0);
        }
    }

    private void setUpInvoice(){
        if(customer != null && !productInOrderList.isEmpty()){
            invoice.setTotalPrice(Double.parseDouble(totalPriceAll.getText()));
            invoice.setCustomerId(customer.getId());
            invoice.setUserId(2);
            invoice.setPaymentType(1);
            invoice.setTotalPaid(Double.parseDouble(totalPriceAll.getText()));
        }
    }

    private void setUpCreateOrderButton(){
        createOrderButton.setOnAction(event -> addToDatabase());
    }

    public void setDataForTable(){
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productSupplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderView.getColumns().clear();
        orderView.getColumns().addAll(productIdCol, productPriceCol, productSupplierCol, productQuantityCol, productColName, productTotalPrice);
        TableColumn<ProductInOrder, Void> deleteButtonCol = getProductInOrderVoidTableColumn();

        orderView.getColumns().add(deleteButtonCol);
    }

    private TableColumn<ProductInOrder, Void> getProductInOrderVoidTableColumn() {
        TableColumn<ProductInOrder, Void> deleteButtonCol = new TableColumn<>("Action");
        deleteButtonCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    ProductInOrder product = getTableView().getItems().get(getIndex());
                    deleteProduct(product);
                });
            }
        });
        return deleteButtonCol;
    }

    private void setUpSearch(){
        FilteredList<ProductInOrder> filteredList = new FilteredList<>(FXCollections.observableList(productInOrderList), b-> true);
        searchProduct.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                return product.getName().toLowerCase().contains(searchKeyword) || product.getSupplierName().toLowerCase().contains(searchKeyword);
            });
            // update pagination
            updateOrderPagination(filteredList,newValue);

        });
        // update pagination
        updateOrderPagination(filteredList,"");
    }

    private void deleteProduct(ProductInOrder product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            products.add(new ProductModel().getProduct(product.getProductId()));
            products.sort(Comparator.comparing(Product::getId));
            productInOrderList.remove(product);
            setPriceAll();
            setTotalItems();
            setUpOrderPagination();
            updateOrderTableData(orderPag.getCurrentPageIndex());
            setUpOrder();
        }
    }

    private void setUpOrderPagination() {
        int pageCount = (productInOrderList.size() + itemPerPages - 1) / itemPerPages;
        orderPag.setPageCount(pageCount);
        if(orderView.getItems().isEmpty()){
            updateOrderTableData(0);
        }
        orderPag.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> updateOrderTableData(newValue.intValue()));
    }

    private void updateOrderTableData(int pageIndex) {
        int fromIndex = pageIndex * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, productInOrderList.size());
        setDataForTable();
        orderView.setItems(FXCollections.observableArrayList(productInOrderList.subList(fromIndex, toIndex)));

    }

    public void resetTableData() {
        setDataForTable();

        // Set new data to the TableView
        setTotalItems();
        setPriceAll();
    }

    private void updateOrderPagination(FilteredList<ProductInOrder> products,String newValue){
        int totalItems = products.size();

        // Update the page count based on the total items
        int pageCount;
        if (newValue == null || newValue.trim().isEmpty()) {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        } else if (totalItems == 0) {
            pageCount = 1;
        } else {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        }

        orderPag.setPageCount(pageCount);
        if (orderPag.getCurrentPageIndex() >= pageCount) {
            orderPag.setCurrentPageIndex(pageCount - 1);
        }

        // Calculate the indices for the current page
        int fromIndex = orderPag.getCurrentPageIndex() * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, totalItems);

        // Create a new FilteredList that filters the entire 'products' list
        FilteredList<ProductInOrder> updatedFilteredList = new FilteredList<>(products, b -> true);
        String searchKeyWord = Objects.requireNonNull(newValue).toLowerCase();
        updatedFilteredList.setPredicate(product -> {
            if (newValue.trim().isBlank()) {
                return true;
            }
            return product.getName().toLowerCase().contains(searchKeyWord) || product.getSupplierName().toLowerCase().contains(searchKeyWord);
        });

        // Sort the updated filtered list
        SortedList<ProductInOrder> sortedList = new SortedList<>(updatedFilteredList);
        sortedList.comparatorProperty().bind(orderView.comparatorProperty());

        // Set the data to display in the table based on the updated filtered list
        orderView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
        setDataForTable();
    }

    private void selectOrderView(){
        orderView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                currentProductInOrder = newValue;
                productName.setText(newValue.getName());
                productQuantity.setText(String.valueOf(newValue.getQuantity()));
                productPrice.setText(String.valueOf(newValue.getPrice()));
                totalPrice.setText(String.valueOf(newValue.getTotalPrice()));
                updateProductButton.setDisable(false);
                cancelButton.setDisable(false);
            }else{
                updateProductButton.setDisable(true);
                cancelButton.setDisable(true);
            }
        });
    }

    private void updateButton(){
        updateProductButton.setOnAction(event -> {
            if(!productName.getText().trim().isEmpty() && !productQuantity.getText().trim().isEmpty()
            && !productPrice.getText().trim().isEmpty()){
                productInOrderList.get(productInOrderList.indexOf(currentProductInOrder)).setQuantity(Integer.valueOf(productQuantity.getText()));
                int quantity = Integer.parseInt(productQuantity.getText());
                double totalPrice = quantity * currentProductInOrder.getPrice();
                totalPrice = Math.round(totalPrice * 100.00) / 100.00;
                productInOrderList.get(productInOrderList.indexOf(currentProductInOrder)).setTotalPrice(totalPrice);

                clearTextField();
                setUpOrder();
                setUpInvoice();
                resetTableData();
            }
        });
    }

    public void setDataCustomer(){
        if(customer != null){
            cusName.setText(customer.getName());
            cusPhone.setText(customer.getPhone());
            cusAddress.setText(customer.getAddress());
            cusEmail.setText(customer.getEmail());
            cusId.setText(String.valueOf(customer.getId()));
        }
    }

    public void setTotalItems(){
        totalItemsAll.setText(String.valueOf(productInOrderList.size()));
    }

    public void setPriceAll(){
        double total = 0;
        for(ProductInOrder productInOrder : productInOrderList){
            total += productInOrder.getTotalPrice();
        }
        total = Math.round(total * 100.00) / 100.00;
        totalPriceAll.setText(String.valueOf(total));
    }

    private void clearTextField(){
        currentProductInOrder = null;
        productName.clear();
        productPrice.clear();
        productQuantity.clear();
        totalPrice.setText("");
        updateProductButton.setDisable(true);
        cancelButton.setDisable(true);
    }

    private void addToDatabase(){
        if(productInOrderList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Modal");
            alert.setContentText("Product List is empty");
            alert.showAndWait();
            return;
        }
        OrderManage orderManage = new OrderManage();
        boolean flag = orderManage.addOrder(order,productInOrderList,invoice);
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Modal");
            alert.setContentText("Failed to add to database");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Modal");
            alert.setContentText("Add Successfully");
            alert.showAndWait();
            orderController.clearAll();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    private void enterQuantity(){
        productQuantity.addEventFilter(KeyEvent.KEY_TYPED,(event)->{
            if(!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });
        productQuantity.textProperty().addListener((observable,oldValue,newValue) -> {
            if(newValue != null && !newValue.trim().isEmpty() && currentProductInOrder != null){
                if(isValidInput(productQuantity.getText(),searchProductInCart(currentProductInOrder.getProductId()))){
                    int enteredQuantity = Integer.parseInt(productQuantity.getText());
                    double price = currentProductInOrder.getPrice();
                    double totalPaid = Math.round(enteredQuantity * price * 100.0)/100.0;
                    totalPrice.setText(String.valueOf(totalPaid));
                }
                else{
                    productQuantity.setText(String.valueOf(Objects.requireNonNull(searchProductInCart(currentProductInOrder.getProductId())).getQuantityInStock()));
                }
            }
        });
    }

    private boolean isValidInput(String input,Product product){
        if(isNumeric(input) && !input.trim().isEmpty() && product != null){
            try {
                int value = Integer.parseInt(input);
                return value <= product.getQuantityInStock();
            } catch (NumberFormatException e) {
                // Handle parsing issues (non-integer input)
                return false;
            }
        }
        return false;
    }

    private boolean isNumeric(String newValue) {
        return newValue.matches("\\d*");
    }

    private Product searchProductInCart(int productId){
        for (Product product : cartList) {
            if (product.getId() == productId) {
                return product; // Return the product if found
            }
        }
        return null; // If no product with the given ID is found
    }
}
