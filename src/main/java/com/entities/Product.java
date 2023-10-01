package com.entities;

import java.sql.Date;

public class Product {
    private Integer id;
    private String name;
    private String supplierName;
    private Integer supplierId;
    private String productType;
    private Integer productTypeId;
    private Integer quantityInStock;
    private Double salePrice;
    private Double discountPercentage;
    private String description;
    private String status;
    private String image;

    public Product() {
    }

    public Product(String name, Integer supplierId, Integer productTypeId, Double salePrice, String status,String image) {
        this.name = name;
        this.supplierId = supplierId;
        this.productTypeId = productTypeId;
        this.salePrice = salePrice;
        this.status = status;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double unitPrice) {
        this.salePrice = unitPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }
}
