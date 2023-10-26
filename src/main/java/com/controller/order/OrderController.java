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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    ProductModel productModel = new ProductModel();
    private final int itemPerPages = 10;
    ObservableList<Product> productObservableList;

    @FXML
    private Button addProductToOrder;

    @FXML
    private TextField cusAddress_tf;

    @FXML
    private TextField cusEmail_tf;

    @FXML
    private TextField cusName_tf;

    @FXML
    private TextField cusPhone_tf;

    @FXML
    private AnchorPane dashboardOrder;

    @FXML
    private Button orderClear_btn;

    @FXML
    private Button order_deleteProduct_btn;

    @FXML
    private Pagination order_pag;

    @FXML
    private TextField order_productId_tf;

    @FXML
    private TextField order_productName_tf;

    @FXML
    private ListView<?> order_productView;

    @FXML
    private TextField order_quantity_tf;

    @FXML
    private TextField order_totalPaid_tf;

    @FXML
    private Button order_updateProduct_btn;

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
    private Button selectCus_btn;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private Button viewOrder_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
        searchProduct.textProperty().addListener((observable,oldvalue, newvalue) -> {
            filteredList.setPredicate(product -> {
                if(newvalue == null || newvalue.trim().isBlank()){
                    return true;
                }
                String searchKeyWord = newvalue.toLowerCase();
                return product.getProductType().toLowerCase().contains(searchKeyWord) || product.getName().toLowerCase().contains(searchKeyWord)
                        || product.getSupplierName().toLowerCase().contains(searchKeyWord);
            });
            updatePagination(filteredList,newvalue);
        });

        tableViewProduct.setItems(products);
    }
    public void setUpPagination(){
        int pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        order_pag.setPageCount(pageCount);
        order_pag.setPageFactory(pageIndex -> {
            setUpTableOrder(pageIndex * itemPerPages, Math.min(pageIndex * itemPerPages, productModel.getNumberRecords() - (pageIndex * itemPerPages)), pageIndex);
            return tableViewProduct;
        });
    }

    private void updatePagination(FilteredList<Product> filteredList,String newvalue){
        int totalItems = filteredList.size();
        int pageCount;
        if(newvalue == null || newvalue.trim().isEmpty()){
            pageCount = (productModel.getNumberRecords() + itemPerPages - 1) / itemPerPages;
        }else if(totalItems == 0){
            pageCount = 1;
        }
        else{
            pageCount = (totalItems + itemPerPages-1)/itemPerPages;
        }
        order_pag.setPageCount(pageCount);
        if (order_pag.getCurrentPageIndex() >= pageCount) {
            order_pag.setCurrentPageIndex(pageCount - 1);
        }
        int fromIndex = order_pag.getCurrentPageIndex() * itemPerPages;
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
