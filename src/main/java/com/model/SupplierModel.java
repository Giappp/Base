package com.model;

import com.db.dao.JDBCConnect;
import com.entities.ProductCategory;
import com.entities.Supplier;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {

    public List<Supplier> getData(){
        String sql = "SELECT * FROM supplier";
        List<Supplier> supplierList = new ArrayList<>();
        try( Connection connection = JDBCConnect.getJDBCConnection()){
            assert  connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("id"));
                    supplier.setName(resultSet.getString("name"));
                    supplier.setAddress(resultSet.getString("address"));
                    supplier.setPhone(resultSet.getString("phone"));
                    supplier.setEmail(resultSet.getString("email"));
                    supplier.setDetails(resultSet.getString("details"));
                    supplierList.add(supplier);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return supplierList;
    }

    public boolean insertSupplier(Supplier supplier){
        String sql = "INSERT INTO supplier(`name`,`address`,`phone`,`email`) VALUES(?,?,?,?)";
        try( Connection connection = JDBCConnect.getJDBCConnection()){
            assert  connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,supplier.getName());
                preparedStatement.setString(2,supplier.getAddress());
                preparedStatement.setString(3,supplier.getPhone());
                preparedStatement.setString(4,supplier.getEmail());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateSupplier(Supplier supplier){
        String sql = "UPDATE `supplier` SET `name` = ?, address = ?, phone = ?, email = ? WHERE id = ?";
        try( Connection connection = JDBCConnect.getJDBCConnection()){
            assert  connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,supplier.getName());
                preparedStatement.setString(2,supplier.getAddress());
                preparedStatement.setString(3,supplier.getPhone());
                preparedStatement.setString(4,supplier.getEmail());
                preparedStatement.setInt(5,supplier.getId());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSupplier(Supplier supplier){
        String sql = "DELETE FROM `supplier` WHERE id = ?";
        try( Connection connection = JDBCConnect.getJDBCConnection()){
            assert  connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1,supplier.getId());
                return preparedStatement.executeUpdate() > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public List<String> getBrands(){
        String sql = "SELECT `id`,`name` FROM supplier";
        List<String> brands = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()){
                    brands.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return brands;
    }
    public Integer getIdSupplier(String name){
        String sql = "Select `id` from supplier where name = ?";
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
    public String getNameSupplier(Integer id){
        String sql = "Select `name` from supplier where id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
