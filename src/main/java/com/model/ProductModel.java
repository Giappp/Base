package com.model;

import com.db.dao.JDBCConnect;
import com.entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                    product.setSupplierName(new SupplierModel().getNameSupplier(resultSet.getInt("supplier_id")));
                    product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    product.setSalePrice(resultSet.getDouble("sale_price"));
                    product.setImportedPrice(resultSet.getDouble("imported_price"));
                    product.setStatus(resultSet.getBoolean("status") ? "Available" : "Unavailable");
                    product.setProductTypeId(resultSet.getInt("product_type_id"));
                    product.setProductType(new ProductCategoryModel().getProductCategoryName(resultSet.getInt("product_type_id")));
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
                "(`name`,`supplier_id`,`product_type_id`,`quantity_in_stock`,`sale_price`,`imported_price`,`status`,`image`) " +
                "VALUES(?,?,?,?,?,?,?,?) ";
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,product.getName());
                preparedStatement.setInt(2,product.getSupplierId());
                preparedStatement.setInt(3,product.getProductTypeId());
                preparedStatement.setDouble(4,product.getQuantityInStock());
                preparedStatement.setDouble(5,product.getSalePrice());
                preparedStatement.setDouble(6,product.getImportedPrice());
                preparedStatement.setBoolean(7,product.getStatus().equalsIgnoreCase("available"));
                preparedStatement.setString(8,product.getImage());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "Update `product` SET `name` = ?, `product_type_id` = ?, `supplier_id` = ?, `sale_price` = ?" +
                ", `status` = ?, `image` = ? " +
                "WHERE id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,product.getName());
                preparedStatement.setInt(2,product.getProductTypeId());
                preparedStatement.setInt(3,product.getSupplierId());
                preparedStatement.setDouble(4,product.getSalePrice());
                preparedStatement.setBoolean(5,product.getStatus().equalsIgnoreCase("available"));
                preparedStatement.setString(6,product.getImage());
                preparedStatement.setInt(7,product.getId());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(Product product){
        String sql = "Delete from `product` where `id` = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1,product.getId());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getProductList2(){
        String sql = "SELECT * from product";
        List<Product> productList = new ArrayList<>();
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
                    product.setImportedPrice(resultSet.getDouble("imported_price"));
                    product.setSalePrice(resultSet.getDouble("sale_price"));
                    product.setStatus(resultSet.getBoolean("status")  ? "Available" : "Unavailable" );
                    product.setProductTypeId(resultSet.getInt("product_type_id"));
                    product.setImage(resultSet.getString("image"));
                    productList.add(product);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }
    public List<String> getNameProductFromSupplier(Integer supplierId){
        String sql = "Select * from product where supplier_id = ?";
        List<String> productName = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,supplierId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    productName.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productName;
    }
    public Integer getIdProduct(String name){
        String sql = "Select `id` from product where name = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1,name);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public void setProductById(Integer id){
        String sql = "Select * from product where id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Double getImportedPrice(Integer id){
        String sql = "Select * from product where id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getDouble("imported_price");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Product getProduct(int productId) {
        String sql = "Select * from product where id = ?";
        Product product = new Product();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,productId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setSupplierId(resultSet.getInt("supplier_id"));
                    product.setSupplierName(new SupplierModel().getNameSupplier(resultSet.getInt("supplier_id")));
                    product.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    product.setSalePrice(resultSet.getDouble("sale_price"));
                    product.setStatus(resultSet.getBoolean("status")  ? "Available" : "Unavailable");
                    product.setProductTypeId(resultSet.getInt("product_type_id"));
                    product.setImportedPrice(resultSet.getDouble("imported_price"));
                    product.setProductType(new ProductCategoryModel().getProductCategoryName(resultSet.getInt("product_type_id")));
                    product.setImage(resultSet.getString("image"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
}
