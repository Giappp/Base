package com.model;

import java.sql.*;

import com.db.dao.JDBCConnect;
import com.entities.Order;
import com.entities.Product;
import com.entities.ProductInOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderManage {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Order getOrder(int id) {
        Order orderResult = new Order();
        boolean result = false;
        String sql = "SELECT * FROM `order` WHERE `id` = ?;";
        try {
            conn = JDBCConnect.getJDBCConnection();
            assert conn != null;
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
                orderResult.setId(rs.getInt("id"));
                orderResult.setCustomerId(rs.getInt("customerId"));
                orderResult.setUserId(rs.getInt("userId"));
                orderResult.setDateRecorded(rs.getDate("dateRecorded"));
                orderResult.setStatus(rs.getInt("status"));
                sql = "SELECT * FROM `product_in_order` WHERE `orderId` = ?;";
                JDBCConnect.closeResultSet(rs);
                JDBCConnect.closePreparedStatement(ps);
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                ArrayList<ProductInOrder> productList = new ArrayList<ProductInOrder>();
                ProductModel productModel = new ProductModel();

                orderResult.setProductInOrder(productList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnect.closeResultSet(rs);
            JDBCConnect.closePreparedStatement(ps);
            JDBCConnect.closeConnection(conn);
        }
        return result ? orderResult : null;
    }

    public boolean addOrder(Order order, List<ProductInOrder> productInOrderList) {
        String sql = "INSERT INTO `order`(customerId,userId,dateRecorded,totalAmount,status) VALUES(?,?,?,?,?)";
        int orderId = -1;
        try(Connection connection = JDBCConnect.getJDBCConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,order.getCustomerId());
            preparedStatement.setInt(2,2);
            Date instantDate = Date.valueOf(LocalDate.now());
            preparedStatement.setDate(3,instantDate);
            preparedStatement.setInt(4,order.getTotalAmount());
            preparedStatement.setInt(5,order.getStatus());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1); // Retrieve the generated ID
                        ProductInOrderModel productInOrderModel = new ProductInOrderModel();
                        return productInOrderModel.addProductToOrder(productInOrderList, orderId);
                    } else {
                        // Handle if no generated keys found
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
