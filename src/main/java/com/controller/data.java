package com.controller;

import com.entities.Customer;
import com.entities.Product;
import com.model.CustomerModel;
import com.model.ProductModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class data {

    public static String username;
    public static String email;
    public static String phone;
    public static Double salePrice;
    public static Integer id;

    public static ProductModel productModel = new ProductModel();
    public static CustomerModel customerModel = new CustomerModel();
    public static ObservableList<Product> products = productModel.getProductList();
    public static ObservableList<Customer> customers = FXCollections.observableList(customerModel.getListCustomer());

    public static void reset(){
        products = productModel.getProductList();
        customers = FXCollections.observableList(customerModel.getListCustomer());
    }

}
