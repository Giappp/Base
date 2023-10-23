package com.controller.storage;

import com.entities.Product;
import com.model.ProductModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StorageController implements Initializable {

    Parent root;

    Scene fxmlFile;

    Stage window;

    private static final int ITEMS_PER_PAGE = 10;

    public AnchorPane dashboardStorage;

    public TextField productFieldSearch;

    public TableView<Product> tbvGoods;

    public TableColumn<Product, Integer> goodsColId;

    public TableColumn<Product, String> goodsColName;

    public TableColumn<Product, String> goodsColType;

    public TableColumn<Product, String> goodsColSupplier;

    public TableColumn<Product, Double> goodsColPrice;

    public TableColumn<Product, String> goodsColStatus;

    public TableColumn<Product, Integer> goodsColAmount;

    public TableColumn<Product, Double> goodsColTotal;

    public Button importProductBtn;

    public Button historyBtn;

    public Pagination storagePg;

    ProductModel productModel = new ProductModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        importProductBtn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/importProduct.fxml", "Import Product");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        historyBtn.setOnAction(event -> {
            try {
                openModalWindow("/controller/client/importHistory.fxml", "Import History");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        int pageCount = (productModel.getNumberRecords() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE;
        storagePg.setPageCount(pageCount);
        storagePg.setPageFactory(pageIndex -> {
            storageList(pageIndex * ITEMS_PER_PAGE, Math.min(pageIndex * ITEMS_PER_PAGE, productModel.getNumberRecords() - (pageIndex * ITEMS_PER_PAGE)), pageIndex);
            return tbvGoods;
        });

    }

    private void openModalWindow (String resource, String title) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

    public void storageList( int offset, int limit, int pageIndex){
        ObservableList<Product> storageList = FXCollections.observableList(productModel.getProductList2(pageIndex * ITEMS_PER_PAGE, ITEMS_PER_PAGE));
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
            return totalBinding.asObject();
        });
        tbvGoods.setItems(storageList);
    }
}
