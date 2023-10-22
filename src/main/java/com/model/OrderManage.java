package com.model;

import java.sql.*;

import com.db.dao.JDBCConnect;
import com.entities.Order;
import com.entities.Product;
import java.util.ArrayList;

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
                ArrayList<Product> productList = new ArrayList<Product>();
                ProductModel productModel = new ProductModel();
                while (rs.next()) productList.add(productModel.getProduct(rs.getInt("productId")));
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

    private boolean add(Order order) {
        return false;
    }
}
