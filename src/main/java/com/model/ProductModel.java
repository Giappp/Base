package com.model;

import com.db.dao.JDBCConnect;
import com.entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductModel {
    public ObservableList<Product> getProductList(){
        String sql = "SELECT product.id, product.name, s.name AS supplier_name, " +
                "product.quantity_in_stock, product.unit_price, product.date, " +
                "product.status, product.supplier_id,pc.name " +
                "FROM product " +
                "INNER JOIN product_category AS pc ON product.id = pc.id " +
                "INNER JOIN supplier AS s ON product.supplier_id = s.id " +
                "ORDER BY product.id";
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        try(Connection connection = JDBCConnect.getJDBCConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("product.id"));
                product.setName(resultSet.getString("product.name"));
                product.setSupplierName(resultSet.getString("supplier_name"));
                product.setQuantityInStock(resultSet.getInt("product.quantity_in_stock"));
                product.setUnitPrice(resultSet.getDouble("product.unit_price"));
                product.setDate(resultSet.getDate("product.date"));
                product.setStatus(resultSet.getString("product.status"));
                product.setProductType(resultSet.getString("pc.name"));
                productObservableList.add(product);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productObservableList;
    }
}
