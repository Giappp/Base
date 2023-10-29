package com.controller.order;

import com.controller.AlertMessages;
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
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderController implements Initializable {

    ProductModel productModel = new ProductModel();

    private final int itemPerPages = 10;

    @FXML
    private Button addProductToOrder;

    @FXML
    private TextField cusAddressTf;

    @FXML
    private TextField cusEmailTf;

    @FXML
    private TextField cusNameTf;

    @FXML
    private TextField cusPhoneTf;

    @FXML
    private Button orderClearBtn;

    @FXML
    private Button orderDeleteProductBtn;

    @FXML
    private Pagination orderPag;

    @FXML
    private TextField orderProductIdTf;

    @FXML
    private TextField orderProductNameTf;

    @FXML
    private ListView<?> orderProductView;

    @FXML
    private TextField orderQuantityTf;

    @FXML
    private TextField orderTotalPaidTf;

    @FXML
    private Button orderUpdateProductBtn;

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
    private Button selectCusBtn;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private Button viewOrderBtn;

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
    }

    public void setUpTableOrder(int offset,int limit,int pageIndex){
        ObservableList<Product> products = FXCollections.observableList(productModel.getProductList2(pageIndex * itemPerPages, itemPerPages));
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
    public void setUpPagination(){
        int pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        orderPag.setPageCount(pageCount);
        orderPag.setPageFactory(pageIndex -> {
            setUpTableOrder(pageIndex * itemPerPages, Math.min(pageIndex * itemPerPages, productModel.getNumberRecords() - (pageIndex * itemPerPages)), pageIndex);
            return tableViewProduct;
        });
    }

    private void updatePagination(FilteredList<Product> filteredList,String newValue){
        int totalItems = filteredList.size();

        // Update the page count based on the total items
        int pageCount;
        if(newValue == null || newValue.trim().isEmpty()){
            pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        }else if(totalItems == 0){
            pageCount = 1;
        } else {
            pageCount = (totalItems + itemPerPages - 1) / itemPerPages;
        }
        else{
            pageCount = (totalItems + itemPerPages-1)/itemPerPages;
        }
        orderPag.setPageCount(pageCount);
        if (orderPag.getCurrentPageIndex() >= pageCount) {
            orderPag.setCurrentPageIndex(pageCount - 1);
        }
        int fromIndex = orderPag.getCurrentPageIndex() * itemPerPages;
        int toIndex = Math.min(fromIndex + itemPerPages, totalItems);

        // Create a new FilteredList that filters the entire 'products' list
        FilteredList<Product> updatedFilteredList = new FilteredList<>(products, b -> true);
        String searchKeyWord = newvalue.toLowerCase();
        updatedFilteredList.setPredicate(product -> {
            if (newvalue == null || newvalue.trim().isBlank()) {
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

    private void openModalWindow(String resource, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent modalWindow = loader.load();
        Stage window = new Stage();
        window.setScene(new Scene(modalWindow));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }
}
