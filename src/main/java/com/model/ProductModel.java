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
        String sql = "SELECT * from product";
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setSupplierId(resultSet.getInt("supplier_id"));
                    product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    product.setSalePrice(resultSet.getDouble("sale_price"));
                    product.setStatus(resultSet.getString("status"));
                    product.setProductType(resultSet.getString("product_type_id"));
                    product.setImage(resultSet.getString("image"));
                    productObservableList.add(product);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return productObservableList;
    }
    public boolean addProduct(Product product){
        String sql = "INSERT into product" +
                "(`name`,`supplier_id`,`product_type_id`,`sale_price`,`status`,`image`) " +
                "VALUES(?,?,?,?,?,?) ";
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,product.getName());
                preparedStatement.setInt(2,product.getSupplierId());
                preparedStatement.setInt(3,product.getProductTypeId());
                preparedStatement.setDouble(4,product.getSalePrice());
                preparedStatement.setString(5,product.getStatus());
                preparedStatement.setString(6,product.getImage());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
