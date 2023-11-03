package com.controller.storage;

import com.entities.GoodsImport;
import com.entities.Product;
import com.model.GoodsImportModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;


public class ImportHistory implements Initializable {

    Parent root;

    @FXML
    private TableColumn<GoodsImport, Date> tvDate;

    @FXML
    private TableView<GoodsImport> tvHistory;

    @FXML
    private TableColumn<GoodsImport, Integer> tvId;

    @FXML
    private TableColumn<GoodsImport, Product> tvProduct;

    @FXML
    private TableColumn<GoodsImport, Integer> tvQuantity;

    @FXML
    private TableColumn<GoodsImport, Double> tvTotal;

    @FXML
    private TableColumn<GoodsImport, Double> tvUnitPrice;

    @FXML
    private Button selectDateBtn;

    private Date beginDate;

    private Date endDate;

    private com.controller.storage.SelectDate selectDate;

    public ImportHistory() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();

        selectDateBtn.setOnAction(event -> {
            try {
                openModalWindow();
                Date selectedBeginDate = selectDate.getBeginDate();
                Date selectedEndDate = selectDate.getEndDate();

                if (selectedBeginDate != null && selectedEndDate != null) {
                    // Update the date range in ImportHistory
                    beginDate = selectedBeginDate;
                    endDate = selectedEndDate;
                    showDataByDate(beginDate, endDate);
                }else{
                    showData();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showData(){
        ObservableList<GoodsImport> goodsImports = FXCollections.observableList(new GoodsImportModel().getData());
        tvDate.setCellValueFactory(new PropertyValueFactory<>("dateImported"));
        tvId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvProduct.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GoodsImport, Product> call(TableColumn<GoodsImport, Product> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Product product, boolean empty) {
                        super.updateItem(product, empty);
                        if (empty || product == null) {
                            setText(null);
                        } else {
                            setText(product.getName());
                        }
                    }
                };
            }
        });

        // Bind the cell value factory to the product property
        tvProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        tvQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tvTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tvUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tvHistory.setItems(goodsImports);

    }

    public void showDataByDate(Date beginDate,Date endDate){
        if(beginDate.after(endDate) || endDate == null){
            showData();
            return;
        }
        ObservableList<GoodsImport> goodsImports = FXCollections.observableList(new GoodsImportModel().getDataByDate(this.beginDate, this.endDate));
        tvDate.setCellValueFactory(new PropertyValueFactory<>("dateImported"));
        tvId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvProduct.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GoodsImport, Product> call(TableColumn<GoodsImport, Product> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Product product, boolean empty) {
                        super.updateItem(product, empty);
                        if (empty || product == null) {
                            setText(null);
                        } else {
                            setText(product.getName());
                        }
                    }
                };
            }
        });
        // Bind the cell value factory to the product property
        tvProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        tvQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tvTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tvUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tvHistory.setItems(goodsImports);
    }
    private void openModalWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/controller/client/selectDate.fxml")); // Create a single instance of FXMLLoader

        root = loader.load(); // Load the FXML and set the controller
        selectDate = loader.getController();

        Scene fxmlFile = new Scene(root);
        Stage window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle("Date Filter");
        window.showAndWait();
    }
}
