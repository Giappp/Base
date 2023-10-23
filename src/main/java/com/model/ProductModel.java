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
                    product.setSupplierId(resultSet.getInt("supplierId"));
                    product.setSupplierName(new SupplierModel().getNameSupplier(resultSet.getInt("supplierId")));
                    product.setQuantityInStock(resultSet.getInt("quantityInStock"));
                    product.setSalePrice(resultSet.getDouble("salePrice"));
                    product.setImportedPrice(resultSet.getDouble("importedPrice"));
                    product.setStatus(resultSet.getBoolean("status") ? "Available" : "Unavailable");
                    product.setProductTypeId(resultSet.getInt("productTypeId"));
                    product.setProductType(new ProductCategoryModel().getProductCategoryName(resultSet.getInt("productTypeId")));
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
                "(`name`,`supplierId`,`productTypeId`,`quantityInStock`,`salePrice`,`importedPrice`,`status`,`image`) " +
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
        String sql = "Update `product` SET `name` = ?, `productTypeId` = ?, `supplierId` = ?, `salePrice` = ?" +
                ", `status` = ?, `image` = ?, `importedPrice` = ?" +
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
                preparedStatement.setDouble(7,product.getImportedPrice());
                preparedStatement.setInt(8,product.getId());
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

    public List<Product> getProductList2(int offset,int limit){
        String sql = "SELECT * from product LIMIT ? OFFSET ?";
        List<Product> productList = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1,limit);
                preparedStatement.setInt(2,offset);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setSupplierId(resultSet.getInt("supplierId"));
                    product.setSupplierName(new SupplierModel().getNameSupplier(resultSet.getInt("supplierId")));
                    product.setQuantityInStock(resultSet.getInt("quantityInStock"));
                    product.setSalePrice(resultSet.getDouble("salePrice"));
                    product.setImportedPrice(resultSet.getDouble("importedPrice"));
                    product.setStatus(resultSet.getBoolean("status") ? "Available" : "Unavailable");
                    product.setProductTypeId(resultSet.getInt("productTypeId"));
                    product.setProductType(new ProductCategoryModel().getProductCategoryName(resultSet.getInt("productTypeId")));
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
        String sql = "Select * from product where supplierId = ?";
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
                    return resultSet.getDouble("importedPrice");
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
                    product.setSupplierId(resultSet.getInt("supplierId"));
                    product.setSupplierName(new SupplierModel().getNameSupplier(resultSet.getInt("supplierId")));
                    product.setQuantityInStock(resultSet.getInt("quantityInStock"));
                    product.setSalePrice(resultSet.getDouble("salePrice"));
                    product.setStatus(resultSet.getBoolean("status")  ? "Available" : "Unavailable");
                    product.setProductTypeId(resultSet.getInt("productTypeId"));
                    product.setImportedPrice(resultSet.getDouble("importedPrice"));
                    product.setProductType(new ProductCategoryModel().getProductCategoryName(resultSet.getInt("productTypeId")));
                    product.setImage(resultSet.getString("image"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public String getName(int productId){
        String sql = "Select name from product where id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, productId);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.getString("name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getNumberRecords() {
        String sql = "SELECT COUNT(*) FROM product";
        int count = 0;
        try (Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Retrieve the count value
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
