package model;

import java.sql.*;

import mysql_database.DBConnect;
import entity.Order;
import entity.Product;
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
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
                orderResult.setId(rs.getInt("id"));
                orderResult.setCustomerId(rs.getInt("customer_id"));
                orderResult.setUserId(rs.getInt("user_id"));
                orderResult.setDateRecorded(rs.getDate("date_recorded"));
                orderResult.setStatus(rs.getInt("status"));
                sql = "SELECT * FROM `product_in_order` WHERE `order_id` = ?;";
                DBConnect.closeResultSet(rs);
                DBConnect.closePreparedStatement(ps);
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                ArrayList<Product> productList = new ArrayList<Product>();
                ProductManage pm = new ProductManage();
                while (rs.next()) productList.add(pm.getProduct(rs.getInt("product_id")));
                orderResult.setProductInOrder(productList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closePreparedStatement(ps);
            DBConnect.closeConnection(conn);
        }
        return result ? orderResult : null;
    }

    private boolean add(Order order) {

    }
}
