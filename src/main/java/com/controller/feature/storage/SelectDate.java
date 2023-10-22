package com.controller.feature.storage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        dp_startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue != null){
                    beginDate = Date.valueOf(dp_startDate.getValue());
                }else{
                    beginDate = Date.valueOf(LocalDate.of(1900,1,1));
                }
            }
        });
        dp_startDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(dp_startDate.getValue() != null){
                    beginDate = Date.valueOf(dp_startDate.getValue());
                }else{
                    beginDate = Date.valueOf(LocalDate.of(1900,1,1));
                }
            }
        });
        dp_endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue != null){
                    endDate = Date.valueOf(dp_endDate.getValue());
                }else{
                    endDate = Date.valueOf(LocalDate.now());
                }
            }
        });
        dp_endDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if(dp_endDate.getValue() != null){
                    endDate = Date.valueOf(dp_endDate.getValue());
                }else{
                    endDate = Date.valueOf(LocalDate.now());
                }
            }
        });
        cancel_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancel_btn.getScene().getWindow();
                stage.close();
            }
        });
        ok_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setDateRange(beginDate,endDate);
                Stage stage = (Stage) ok_btn.getScene().getWindow();
                stage.close();
            }
        });
    }
}