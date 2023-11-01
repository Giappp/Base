package com.controller.storage;

import com.db.dao.JDBCConnect;
import com.entities.Customer;
import com.entities.Product;
import com.model.ProductModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StorageController implements Initializable {

    @FXML
    private Label totalItems;

    @FXML
    private TextField productFieldSearch;

    @FXML
    private TableView<Product> tbvGoods;

    @FXML
    private TableColumn<Product, Integer> goodsColId;

    @FXML
    private TableColumn<Product, String> goodsColName;

    @FXML
    private TableColumn<Product, String> goodsColType;

    @FXML
    private TableColumn<Product, String> goodsColSupplier;

    @FXML
    private TableColumn<Product, Double> goodsColPrice;

    @FXML
    private TableColumn<Product, String> goodsColStatus;

    @FXML
    private TableColumn<Product, Integer> goodsColAmount;

    @FXML
    private TableColumn<Product, Double> goodsColTotal;

    @FXML
    private Button importProductBtn;

    @FXML
    private Button historyBtn;

    @FXML
    private Pagination storagePg;

    private static final int ITEMS_PER_PAGE = 18;

    private ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    ProductModel productModel = new ProductModel();

    Stage window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        importProductBtn.setOnAction(event -> openModalWindow("/controller/client/importProduct.fxml", "Import Product"));

        historyBtn.setOnAction(event -> openModalWindow("/controller/client/importHistory.fxml", "Import History"));

        setupTable();
        setupPagination();

        allProducts.addAll(productModel.getProductList2(0, productModel.getNumberRecords()));

        //Create a filter for the search box
        productFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            productObservableList.setAll(getFilteredProducts(searchText));
        });

    }

    List<Product> allProducts = new ArrayList<>();

    private ObservableList<Product> getProducts() {
        return FXCollections.observableList(allProducts);
    }

    private List<Product> getFilteredProducts(String searchText) {
        if (searchText.isEmpty()) {
            return allProducts;
        }

        return allProducts.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(searchText) ||
                                product.getProductType().toLowerCase().contains(searchText) ||
                                product.getSupplierName().toLowerCase().contains(searchText) ||
                                product.getStatus().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }


    private void openModalWindow(String resource, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
            Scene fxmlFile = new Scene(root);
            window = new Stage();
            window.setScene(fxmlFile);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setIconified(false);
            window.setTitle(title);
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTable() {
        goodsColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        goodsColAmount.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        goodsColSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        goodsColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        goodsColPrice.setCellValueFactory(new PropertyValueFactory<>("importedPrice"));
        goodsColType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        goodsColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        goodsColTotal.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            DoubleBinding totalBinding = Bindings.createDoubleBinding(() ->
                            product.getImportedPrice() * product.getQuantityInStock(),
                    product.unitPriceProperty(),
                    product.getQuantityInStockProperty()
            );
            DoubleBinding roundedTotalBinding = Bindings.createDoubleBinding(() ->
                            Math.round(totalBinding.get() * 100.0) / 100.0,
                    totalBinding
            );

            return Bindings.createObjectBinding(
                    () -> roundedTotalBinding.get(),
                    roundedTotalBinding
            );
        });

        productObservableList = getProducts(0); // get default product
        tbvGoods.setItems(productObservableList);
    }

    private ObservableList<Product> getProducts(int offset) {
        return FXCollections.observableList(productModel.getProductList2(offset, StorageController.ITEMS_PER_PAGE));
    }

    private void setupPagination() {
        int pageCount = (productModel.getNumberRecords() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
        storagePg.setPageCount(pageCount);

        int totalItemCount = productModel.getNumberRecords();
        totalItems.setText("Total: " + totalItemCount);

        storagePg.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int pageIndex = newValue.intValue();
            int offset = pageIndex * ITEMS_PER_PAGE;
//            productObservableList = getProducts(offset);
//            tbvGoods.setItems(productObservableList);
            productObservableList.setAll(allProducts.subList(offset, Math.min(offset + ITEMS_PER_PAGE, allProducts.size())));
        });
    }
}
