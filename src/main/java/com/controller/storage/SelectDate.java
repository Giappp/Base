package com.controller.storage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SelectDate implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private AnchorPane chooseDateScene;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private Button okBtn;

    private Date beginDate;

    private Date endDate;

    public void setDateRange(Date beginDate, Date endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                beginDate = Date.valueOf(dpStartDate.getValue());
            }else{
                beginDate = Date.valueOf(LocalDate.of(1900,1,1));
            }
        });

        dpStartDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(dpStartDate.getValue() != null){
                    beginDate = Date.valueOf(dpStartDate.getValue());
                }else{
                    beginDate = Date.valueOf(LocalDate.of(1900,1,1));
                }
            }
        });

        dpEndDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                endDate = Date.valueOf(dpEndDate.getValue());
            }else{
                endDate = Date.valueOf(LocalDate.now());
            }
        });

        dpEndDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(dpEndDate.getValue() != null){
                    endDate = Date.valueOf(dpEndDate.getValue());
                }else{
                    endDate = Date.valueOf(LocalDate.now());
                }
            }
        });

        cancelBtn.setOnAction(event -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });

        okBtn.setOnAction(event -> {
            setDateRange(beginDate,endDate);
            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
        });
    }
}