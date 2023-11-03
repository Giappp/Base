package com.model;

import com.db.dao.JDBCConnect;
import com.entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerModel {

    public static int getNumberRecords() {
        String sql = "SELECT COUNT(*) FROM customer";
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

    public List<Customer> getListCustomer(int offset, int limit){
        String sql = "SELECT * from customer LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection();
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(sql)){
            preparedStatement.setInt(1,limit);
            preparedStatement.setInt(2,offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setAddress(resultSet.getString("address"));
                customers.add(customer);
            }
            if(!resultSet.isClosed()){
                resultSet.close();
            }
            return customers;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Customer> getListCustomer(){
        String sql = "SELECT * from customer";
        List<Customer> customers = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection();
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setAddress(resultSet.getString("address"));
                customers.add(customer);
            }
            if(!resultSet.isClosed()){
                resultSet.close();
            }
            return customers;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
