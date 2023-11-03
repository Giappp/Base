package com.model;

import com.db.dao.JDBCConnect;
import com.entities.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class InvoiceModel {
    public boolean addToDatabase(Invoice invoice){
        String sql = "INSERT INTO invoice(`orderId`,`customerId`,`userId`,`paymentType`,`totalPrice`,`totalPaid`,`dateRecorded`)" +
                " VALUES(?,?,?,?,?,?,?)";
        try(Connection connection = JDBCConnect.getJDBCConnection();
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(sql)){
            preparedStatement.setInt(1,invoice.getOrderId());
            preparedStatement.setInt(2,invoice.getCustomerId());
            preparedStatement.setInt(3,invoice.getUserId());
            preparedStatement.setInt(4,invoice.getPaymentType());
            preparedStatement.setDouble(5,invoice.getTotalPrice());
            preparedStatement.setDouble(6,invoice.getTotalPaid());
            preparedStatement.setDate(7,invoice.getDateRecorded());
            return preparedStatement.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
