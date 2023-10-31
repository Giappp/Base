package com.entities;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Product {
    private Integer id;
    private String name;
    private String supplierName;
    private Integer supplierId;
    private String productType;
    private Integer productTypeId;
    private Integer quantityInStock;
    private Double salePrice;
    private Double importedPrice;
    private String description;
    private String status;
    private String image;
    private Double importedPriceValue;
    private final DoubleProperty importedPriceProperty = new SimpleDoubleProperty();
    private final IntegerProperty quantityInStockProperty = new SimpleIntegerProperty();

    public Product() {
    }

    public Product(String name, Integer supplierId, Integer productTypeId, Double salePrice, String status,String image,Double importedPrice,Integer quantityInStock) {
        this.name = name;
        this.supplierId = supplierId;
        this.productTypeId = productTypeId;
        this.salePrice = salePrice;
        this.status = status;
        this.image = image;
        this.importedPrice = importedPrice;
        this.quantityInStock = quantityInStock;
    }

    public Product(String name, Integer supplierId, Integer productTypeId, Integer quantityInStock, Double salePrice, Double importedPrice, String image,String status) {
        this.name = name;
        this.supplierId = supplierId;
        this.productTypeId = productTypeId;
        this.quantityInStock = quantityInStock;
        this.salePrice = salePrice;
        this.importedPrice = importedPrice;
        this.image = image;
        this.status = status;
    }

    public Product(Integer id, String name,
                   String supplierName, Integer supplierId,
                   String productType, Integer productTypeId,
                   Double salePrice, String status, String image,Double importedPrice) {
        this.id = id;
        this.name = name;
        this.supplierName = supplierName;
        this.supplierId = supplierId;
        this.productType = productType;
        this.productTypeId = productTypeId;
        this.salePrice = salePrice;
        this.status = status;
        this.image = image;
        this.importedPrice = importedPrice;
    }

    public Double getImportedPrice() {
        return importedPrice;
    }

    public void setImportedPrice(Double importedPrice) {
        this.importedPrice = importedPrice;
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

    public Double getImportedPriceValue() {
        return this.importedPrice * this.quantityInStock;
    }
    public final double getUnitPrice() {
        return importedPriceProperty.get();
    }

    // Setter for the unitPrice property
    public final void setUnitPrice(double value) {
        importedPriceProperty.set(value);
    }

    public final DoubleProperty unitPriceProperty() {
        return importedPriceProperty;
    }
    public final IntegerProperty getQuantityInStockProperty(){
        return quantityInStockProperty;
    }

}
