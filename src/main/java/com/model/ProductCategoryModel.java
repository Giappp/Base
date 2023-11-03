package com.model;

import com.db.dao.JDBCConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryModel {
    public List<String> getType(){
        String sql = "SELECT `name` FROM product_category";
        List<String> types = new ArrayList<>();
        try(Connection connection =JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()){
                        types.add(resultSet.getString("name"));
                    }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }
    public Integer getProductCategoryId(String name){
        String sql = "Select `id` from product_category where name = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1,name);
                ResultSet resultSet= preparedStatement.executeQuery();
                if(resultSet.next()){
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public String getProductCategoryName(Integer id){
        String sql = "Select `name` from product_category where id = ?";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1,id);
                ResultSet resultSet= preparedStatement.executeQuery();
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
