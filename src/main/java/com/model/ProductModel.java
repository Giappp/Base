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
                "product.quantity_in_stock, product.sale_price, " +
                "product.status, product.image, product.supplier_id, pc.name " +
                "FROM product " +
                "INNER JOIN product_category AS pc ON product.id = pc.id " +
                "INNER JOIN supplier AS s ON product.supplier_id = s.id " +
                "ORDER BY product.id";
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Product product = new Product();
                    product.setId(resultSet.getInt("product.id"));
                    product.setName(resultSet.getString("product.name"));
                    product.setSupplierName(resultSet.getString("supplier_name"));
                    product.setQuantityInStock(resultSet.getInt("product.quantity_in_stock"));
                    product.setSalePrice(resultSet.getDouble("product.sale_price"));
                    product.setStatus(resultSet.getString("product.status"));
                    product.setProductType(resultSet.getString("pc.name"));
                    product.setImage(resultSet.getString("product.image"));
                    productObservableList.add(product);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return productObservableList;
    }
    public boolean addProduct(Product product){
        String sql = "INSERT into `pos`.`goods_import` " +
                "(`product_id`, `quantity`, `unit_price`, `total_price`, `date_imported`, `user_id`) " +
                "VALUES(?,?,?,?,?,?) ";
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
