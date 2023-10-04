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
