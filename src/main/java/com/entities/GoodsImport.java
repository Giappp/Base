package com.entities;

import java.sql.Date;

public class GoodsImport {
    private Integer id;
    private Integer productId;
    private Product product;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Date dateImported;
    private Integer userId;

    public GoodsImport() {
    }


    public GoodsImport(Integer productId, Product product, Integer quantity, Double unitPrice, Double totalPrice, Date dateImported, Integer userId) {
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.dateImported = dateImported;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateImported() {
        return dateImported;
    }

    public void setDateImported(Date dateImported) {
        this.dateImported = dateImported;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
