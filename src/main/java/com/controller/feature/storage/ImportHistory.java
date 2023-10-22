package com.controller.feature.storage;

import com.entities.GoodsImport;
import com.entities.Product;
import com.model.GoodsImportModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    FXMLLoader loader;
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
    private SelectDate selectDate;
    public ImportHistory() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();

        selectDate_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    openModalWindow("/controller/client/selectDate.fxml","Date Filter");
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
            }
        });
    }

    public void showData(){
        ObservableList<GoodsImport> goodsImports = FXCollections.observableList(new GoodsImportModel().getData());
        tv_date.setCellValueFactory(new PropertyValueFactory<>("dateImported"));
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_product.setCellFactory(new Callback<TableColumn<GoodsImport, Product>, TableCell<GoodsImport, Product>>() {
            @Override
            public TableCell<GoodsImport, Product> call(TableColumn<GoodsImport, Product> param) {
                return new TableCell<GoodsImport, Product>() {
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
        tv_product.setCellValueFactory(new PropertyValueFactory<>("product"));
        tv_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tv_total.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tv_unit_price.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tv_history.setItems(goodsImports);

    }
    public void showDataByDate(Date beginDate,Date endDate){
        if(beginDate.after(endDate) || beginDate == null || endDate == null){
            showData();
            return;
        }
        ObservableList<GoodsImport> goodsImports = FXCollections.observableList(new GoodsImportModel().getDataByDate(this.beginDate, this.endDate));
        tv_date.setCellValueFactory(new PropertyValueFactory<>("dateImported"));
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_product.setCellFactory(new Callback<TableColumn<GoodsImport, Product>, TableCell<GoodsImport, Product>>() {
            @Override
            public TableCell<GoodsImport, Product> call(TableColumn<GoodsImport, Product> param) {
                return new TableCell<GoodsImport, Product>() {
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
        tv_product.setCellValueFactory(new PropertyValueFactory<>("product"));
        tv_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tv_total.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tv_unit_price.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tv_history.setItems(goodsImports);
    }
    private void openModalWindow(String resource, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource)); // Create a single instance of FXMLLoader

        root = loader.load(); // Load the FXML and set the controller
        selectDate = loader.getController();

        Scene fxmlFile = new Scene(root);
        Stage window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setIconified(false);
        window.setTitle(title);
        window.showAndWait();
    }

}
