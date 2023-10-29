package com.controller.order;

import com.entities.Product;
import com.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.ResourceBundle;

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
        setUpPagination();
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

        tableViewProduct.setItems(products);
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
        int pageCount;
        if(newValue == null || newValue.trim().isEmpty()){
            pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        }else if(totalItems == 0){
            pageCount = 1;
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

        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableViewProduct.comparatorProperty());

        tableViewProduct.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }
    public void openModalWindow(String resource, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent modalWindow = loader.load();
        Stage window = new Stage();
        window.setScene(new Scene(modalWindow));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
    }
}
