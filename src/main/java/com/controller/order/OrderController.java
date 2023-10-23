package com.controller.order;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class OrderController {

    @FXML
    private AnchorPane dashboardOrder;

    @FXML
    private TableView tblvOrderView;

    @FXML
    private TableColumn orderColId;

    @FXML
    private TableColumn orderColType;

    @FXML
    private TableColumn orderColBrand;

    @FXML
    private TableColumn orderColName;

    @FXML
    private TableColumn orderColCusname;

    @FXML
    private TableColumn orderColAmount;

    @FXML
    private TableColumn orderColPrice;

    @FXML
    private TableColumn orderColDate;

    @FXML
    private TextField tfNameChoice;

    @FXML
    private Text displayPriceAll;

    @FXML
    private Button addOrderBtn;

    @FXML
    private Button updateOrderBtn;

    @FXML
    private Button cancelOrderBtn;

    @FXML
    private Spinner spAmountChoice;

    @FXML
    private ComboBox cbTypeChoice;

    @FXML
    private ComboBox cbBrandChoice;

    @FXML
    private TextField tfCusnameChoice;

    @FXML
    private Button deleteOrderBtn;

    @FXML
    private Pagination orderPg;

}
