package com.model;

import com.db.dao.JDBCConnect;
import com.entities.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class CustomerModel {

    private ObservableList<Customer> getCustomerObservableList() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        String sql = "SELECT id, name, address, phone, email FROM customer";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                observableList.add(new Customer(id, name, address, phone, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }
}
