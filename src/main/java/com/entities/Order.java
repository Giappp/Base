package com.entities;

import java.util.Date;
import java.util.ArrayList;

public class Order {
    private int id;
    private int customerId;
    private int userId;
    private ArrayList<Product> productInOrder;
    private Date dateRecorded;
    private int status;

    public Order() {}

    public Order(int id, int customerId, int userId, ArrayList<Product> productInOrder, Date dateRecorded, int status) {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.productInOrder = productInOrder;
        this.dateRecorded = dateRecorded;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Product> getProductInOrder() {
        return productInOrder;
    }

    public void setProductInOrder(ArrayList<Product> productInOrder) {
        this.productInOrder = productInOrder;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
