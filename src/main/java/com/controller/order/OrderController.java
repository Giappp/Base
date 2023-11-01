package com.controller.order;

import com.controller.AlertMessages;
import com.controller.client.PreviewOrder;
import com.controller.data;
import com.entities.Customer;
import com.entities.Product;
import com.entities.ProductInOrder;
import com.model.CustomerModel;
import com.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderController implements Initializable {

    ProductModel productModel = new ProductModel();

    CustomerModel customerModel = new CustomerModel();

    private final int itemPerPages = 12;

    @FXML
    private TableView<Customer> CustomerTable;

    @FXML
    private TableColumn<Customer, String> cusColEmail;

    @FXML
    private TableColumn<Customer, Integer> cusColID;

    @FXML
    private TableColumn<Customer, String> cusColName;

    @FXML
    private TableColumn<Customer, String> cusColPhone;

    @FXML
    private Pagination cusPag;

    @FXML
    private Button addCustomerBtn;

    @FXML
    private TextField customerId;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerPhone;

    @FXML
    private Button addProductToOrder;

    @FXML
    private Button orderClear_btn;

    @FXML
    private Pagination order_pag;

    @FXML
    private TextField order_productId_tf;

    @FXML
    private TextField productSalePrice;

    @FXML
    private TextField order_productName_tf;

    @FXML
    private TextField order_quantity_tf;

    @FXML
    private TextField order_totalPaid_tf;

    @FXML
    private ComboBox<String> paymentComboBox;

    @FXML
    private TableColumn<Product, Integer> productColAmount;

    @FXML
    private TableColumn<Product, String> productColBrand;

    @FXML
    private TableColumn<Product, Integer> productColId;

    @FXML
    private TableColumn<Product, String> productColName;

    @FXML
    private TableColumn<Product, Double> productColPrice;

    @FXML
    private TableColumn<Product, String> productColStatus;

    @FXML
    private TableColumn<Product, String> productColType;

    @FXML
    private TextField searchProduct;

    @FXML
    private ImageView productImage;

    @FXML
    private TextField searchCustomer;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private Button viewOrder_btn;

    @FXML
    private Label productInCartCount;

    @FXML
    private Button clearCartButton;

    PreviewOrder previewOrderController;
    private ObservableList<Product> products;
    private ObservableList<Customer> customers;
    private final int cusPerPages = 4;
    Boolean orderFlag = false;
    private Product currentSelectProduct;
    private Customer currentSelectCustomer;
    private List<Product> cartList = new ArrayList<>();

    private ObservableList<Product> products;

    private ObservableList<Customer> customers;

    private final int cusPerPages = 4;

    private Product currentSelectProduct;

    private Customer currentSelectCustomer;

    List<Product> cartList = new ArrayList<>();

    List<ProductInOrder> productInOrderList = new ArrayList<>();

    FXMLLoader loader;

    AlertMessages alertMessages = new AlertMessages();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTableOrder();
        setUpPagination();
        selectProduct();

        selectCustomer();
        setupCustomerTable();
        setupCustomerPagination();

        addProductToOrderAction();
        viewOrderAction();
        setClearCartButton();
        setCountProductInCart();

        setOrderClearButton();
    }

    private void setupCustomerTable() {
        customers = data.customers;
        cusColID.setCellValueFactory(new PropertyValueFactory<>("id"));
        cusColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cusColPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        cusColEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        FilteredList<Customer> filteredList = new FilteredList<>(customers, b -> true);

        searchCustomer.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                return customer.getName().toLowerCase().contains(searchKeyword) || customer.getPhone().toLowerCase().contains(searchKeyword)
                        || customer.getEmail().toLowerCase().contains(searchKeyword);
            });
            // update pagination
            updateCustomerPagination(filteredList,newValue);

        });
        // update pagination
        updateCustomerPagination(filteredList,"");
    }

    private void updateCustomerTableData(int pageIndex) {
        int fromIndex = pageIndex * cusPerPages;
        int toIndex = Math.min(fromIndex + cusPerPages, customers.size());
        CustomerTable.setItems(FXCollections.observableArrayList(customers.subList(fromIndex, toIndex)));
    }

    private void setupCustomerPagination() {
        int pageCount = (customers.size() + cusPerPages - 1) / cusPerPages;
        cusPag.setPageCount(pageCount);
        cusPag.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> updateCustomerTableData(newValue.intValue()));
    }


    private void updateCustomerPagination(FilteredList<Customer> filteredList,String newValue) {
        int totalItems = filteredList.size();

        // Update the page count based on the total items
        int pageCount;
        if (newValue == null || newValue.trim().isEmpty()) {
            pageCount = (totalItems + cusPerPages - 1) / cusPerPages;
        } else if (totalItems == 0) {
            pageCount = 1;
        } else {
            pageCount = (totalItems + cusPerPages - 1) / cusPerPages;
        }

        cusPag.setPageCount(pageCount);
        if (cusPag.getCurrentPageIndex() >= pageCount) {
            cusPag.setCurrentPageIndex(pageCount - 1);
        }

        // Calculate the indices for the current page
        int fromIndex = cusPag.getCurrentPageIndex() * cusPerPages;
        int toIndex = Math.min(fromIndex + cusPerPages, totalItems);

        // Create a new FilteredList that filters the entire 'products' list
        FilteredList<Customer> updatedFilteredList = new FilteredList<>(customers, b -> true);
        String searchKeyWord = (newValue != null) ? newValue.toLowerCase() : "";
        updatedFilteredList.setPredicate(customer -> {
            if (newValue == null || newValue.trim().isBlank()) {
                return true;
            }
            return customer.getName().toLowerCase().contains(searchKeyWord) || customer.getPhone().toLowerCase().contains(searchKeyWord)
                    || customer.getEmail().toLowerCase().contains(searchKeyWord);
        });

        // Sort the updated filtered list
        SortedList<Customer> sortedList = new SortedList<>(updatedFilteredList);
        sortedList.comparatorProperty().bind(CustomerTable.comparatorProperty());

        // Set the data to display in the table based on the updated filtered list
        CustomerTable.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    private void selectCustomer(){
        CustomerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                currentSelectCustomer = newValue;
                customerId.setText(String.valueOf(newValue.getId()));
                customerName.setText(newValue.getName());
                customerEmail.setText(newValue.getEmail());
                customerPhone.setText(newValue.getPhone());
            }
        });
    }

    private void addProductToOrderAction(){
        addProductToOrder.setOnAction(event -> {
            if(currentSelectProduct != null && !order_quantity_tf.getText().trim().isEmpty()){
                if(cartList.contains(currentSelectProduct)){
                    alertMessages.errorMessage("Duplicate Product");
                    tableViewProduct.getSelectionModel().clearSelection();
                }else{
                    cartList.add(currentSelectProduct);
                    ProductInOrder productInOrder = new ProductInOrder();
                    productInOrder.setProductId(currentSelectProduct.getId());
                    productInOrder.setQuantity(Integer.parseInt(order_quantity_tf.getText()));
                    productInOrder.setPrice(currentSelectProduct.getSalePrice());
                    productInOrder.setName(currentSelectProduct.getName());
                    productInOrder.setSupplierName(currentSelectProduct.getSupplierName());
                    int enteredQuantity = Integer.parseInt(order_quantity_tf.getText());
                    double price = Double.parseDouble(productSalePrice.getText());
                    double totalPaid = Math.round(enteredQuantity * price * 100.0)/100.0;
                    products.remove(currentSelectProduct);
                    setUpTableOrder();
                    productInOrder.setTotalPrice(totalPaid);
                    productInOrderList.add(productInOrder);
                    tableViewProduct.getSelectionModel().clearSelection();
                    setCountProductInCart();
                }
            }
            currentSelectProduct = null;
            addProductToOrder.setDisable(true);
        });
    }

    private void viewOrderAction(){
        viewOrder_btn.setOnAction(event -> {
            if(currentSelectCustomer == null){
                alertMessages.errorMessage("Customer Information is null!");
            }else if(productInOrderList.isEmpty()){
                alertMessages.errorMessage("Product cart is empty");
            }
            else{
                try {
                    openModalWindow();
                    // Pass the data to the method in the PreviewOrderController
                    setCountProductInCart();
                    setUpTableOrder();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void clearAll() {
        data.reset();
        setUpTableOrder();
        productInOrderList.clear();
        cartList.clear();
        setCountProductInCart();
        productImage.setImage(null);
        order_totalPaid_tf.clear();
        order_quantity_tf.clear();
        order_productName_tf.clear();
        order_productId_tf.clear();
        setCountProductInCart();
        currentSelectProduct = null;
        currentSelectCustomer = null;
        CustomerTable.getSelectionModel().clearSelection();
        customerPhone.clear();
        customerEmail.clear();
        customerName.clear();
        customerId.clear();
    }

    private void setCountProductInCart(){
        productInCartCount.setText(String.valueOf(productInOrderList.size()));
    }
    private void setOrderClearButton(){
        orderClear_btn.setOnAction(event -> clearAll());
    }
    private void setClearCartButton(){
        clearCartButton.setOnAction(event -> {
            data.reset();
            setUpTableOrder();
            productInOrderList.clear();
            cartList.clear();
            setCountProductInCart();
        });
    }

    private void displayCartList(){
        if(!cartList.isEmpty() && !productInOrderList.isEmpty()) {
            for (Product product : cartList) {
                System.out.println(product.getId());
                System.out.println(product.getName());
                System.out.println(product.getSalePrice());
            }
            for(ProductInOrder productInOrder : productInOrderList){
                System.out.println(productInOrder.getProductId());
                System.out.println(productInOrder.getQuantity());
            }
        }
    }

    private void setUpTableOrder(){
        products = data.products;
        productColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productColAmount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        productColPrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        productColBrand.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        productColType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        productColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        FilteredList<Product> filteredList = new FilteredList<>(products,b -> true);
        searchProduct.textProperty().addListener((observable,oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if(newValue == null || newValue.trim().isBlank()){
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();
                return product.getProductType().toLowerCase().contains(searchKeyWord) || product.getName().toLowerCase().contains(searchKeyWord)
                        || product.getSupplierName().toLowerCase().contains(searchKeyWord);
            });
            updatePagination(filteredList,newValue);
        });

        updatePagination(filteredList,"");
    }
    private void setUpPagination(){
        int pageCount = (products.size() + itemPerPages - 1) / itemPerPages;
        order_pag.setPageCount(pageCount);
        order_pag.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> updateProductData(newValue.intValue()));
    }

    private void updateProductData(int pageIndex){
        int fromIndex = pageIndex * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, products.size());
        tableViewProduct.setItems(FXCollections.observableArrayList(products.subList(fromIndex, toIndex)));
    }

    private void updatePagination(FilteredList<Product> filteredList, String newValue) {
        // Calculate the total number of items in the filtered list
        int totalItems = filteredList.size();

        // Update the page count based on the total items
        int pageCount;
        if (newValue == null || newValue.trim().isEmpty()) {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        } else if (totalItems == 0) {
            pageCount = 1;
        } else {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        }

        order_pag.setPageCount(pageCount);
        if (order_pag.getCurrentPageIndex() >= pageCount) {
            order_pag.setCurrentPageIndex(pageCount - 1);
        }

        // Calculate the indices for the current page
        int fromIndex = order_pag.getCurrentPageIndex() * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, totalItems);

        // Create a new FilteredList that filters the entire 'products' list
        FilteredList<Product> updatedFilteredList = new FilteredList<>(products, b -> true);
        String searchKeyWord = (newValue != null) ? newValue.toLowerCase() : "";
        updatedFilteredList.setPredicate(product -> {
            if (newValue == null || newValue.trim().isBlank()) {
                return true;
            }
            return product.getProductType().toLowerCase().contains(searchKeyWord)
                    || product.getName().toLowerCase().contains(searchKeyWord)
                    || product.getSupplierName().toLowerCase().contains(searchKeyWord);
        });

        // Sort the updated filtered list
        SortedList<Product> sortedList = new SortedList<>(updatedFilteredList);
        sortedList.comparatorProperty().bind(tableViewProduct.comparatorProperty());

        // Set the data to display in the table based on the updated filtered list
        tableViewProduct.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    private void selectProduct(){
        tableViewProduct.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                currentSelectProduct = newValue;
                if (checkImageUrl(newValue.getImage())) {
//                    Image img = new Image(newValue.getImage());
//                    addproduct_imageview.setImage(img);
                    String currentPath = System.getProperty("user.dir");
                    productImage.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                } else if (newValue.getImage() == null) {
                    String currentPath = System.getProperty("user.dir");
                    productImage.setImage(new Image(currentPath + "\\src\\main\\resources\\controller\\images\\default.jpg"));
                }
                order_productId_tf.setText(String.valueOf(currentSelectProduct.getId()));
                order_productName_tf.setText(currentSelectProduct.getName());
                order_quantity_tf.setText("1");
                productSalePrice.setText(String.valueOf(currentSelectProduct.getSalePrice()));
                Integer enteredQuantity = Integer.valueOf(order_quantity_tf.getText());
                Double price = newValue.getSalePrice();
                Double totalPaid = Math.round(enteredQuantity * price * 100.0)/100.0;
                order_quantity_tf.setStyle(null);
                order_totalPaid_tf.setText(String.valueOf(totalPaid));
                addProductToOrder.setDisable(false);
                enterQuantity();
            }
        });
    }

    private boolean isNodeInsideTableView(EventTarget target, TableView<?> tableView) {
        // Check if the target node or any of its ancestors are the TableView
        while (target != null && !(target instanceof Scene)) {
            if (target.equals(tableView)) {
                return true; // Node is inside the TableView
            }
            target = ((javafx.scene.Node) target).getParent();
        }
        return false; // Node is outside the TableView
    }

    private void enterQuantity(){
        order_quantity_tf.addEventFilter(KeyEvent.KEY_TYPED,(event)->{
            if(!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });
        order_quantity_tf.textProperty().addListener((observable,oldValue,newValue) -> {
            if(newValue != null && !newValue.trim().isEmpty()){
                if(isValidInput(order_quantity_tf.getText())){
                    int enteredQuantity = Integer.parseInt(order_quantity_tf.getText());
                    double price = Double.parseDouble(productSalePrice.getText());
                    double totalPaid = Math.round(enteredQuantity * price * 100.0)/100.0;
                    addProductToOrder.setDisable(false);
                    order_quantity_tf.setStyle(null);
                    order_totalPaid_tf.setText(String.valueOf(totalPaid));
                }
                else{
                    order_quantity_tf.setText(String.valueOf(oldValue));
                }
            }
        });
    }

    private boolean isValidInput(String input){
        if(isNumeric(input) && !input.trim().isEmpty() && currentSelectProduct != null){
            try {
                int value = Integer.parseInt(input);
                return value <= currentSelectProduct.getQuantityInStock();
            } catch (NumberFormatException e) {
                // Handle parsing issues (non-integer input)
                return false;
            }
        }
        return false;
    }

    private boolean checkImageUrl (String url){
        String regex
                = "(\\S+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        if (url == null) {
            return false;
        }

        Matcher m = p.matcher(url);

        return m.matches();
    }

    private boolean isNumeric(String newValue) {
        return newValue.matches("\\d*");
    }

    private void openModalWindow() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/controller/client/previewOrder.fxml"));
        Parent modalWindow = loader.load();
        previewOrderController = loader.getController();
        previewOrderController.setData(this,cartList, productInOrderList,currentSelectCustomer,products);
        Stage window = new Stage();
        window.setScene(new Scene(modalWindow));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle("Preview Order");
        window.showAndWait();
    }
}

