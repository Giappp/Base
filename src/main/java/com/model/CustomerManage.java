package model;

import java.sql.*;
import mysql_database.DBConnect;
import entity.Customer;

public class CustomerManage {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public CustomerManage() {}

    public Customer getCustomer(int id) {
        Customer customerResult = new Customer();
        boolean result = false;
        String sql = "SELECT * FROM `customer` WHERE `id` = ?;";
        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                customerResult.setId(rs.getInt("id"));
                customerResult.setName(rs.getString("name"));
                customerResult.setAddress(rs.getString("address"));
                customerResult.setEmail(rs.getString("email"));
                customerResult.setPhone(rs.getString("phone"));
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closePreparedStatement(ps);
            DBConnect.closeConnection(conn);
        }
        return result ? customerResult : null;
    }

    public boolean add(Customer customer) {
        boolean result = false;
        String sql = "INSERT INTO `customer` VALUES (?, ?, ?, ?, ?);";
        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getEmail());
            int executeResult = ps.executeUpdate();
            if (executeResult != 0) {
                System.out.println("Successfully added!");
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.closePreparedStatement(ps);
            DBConnect.closeConnection(conn);
        }
        return result;
    }

    public boolean update(Customer customer, int selectedId) {
        String sql = "UPDATE `customer` SET `id` = ?, `name` = ?, `address` = ?, `phone` = ?, `email` = ? WHERE `id` = ?;";
        boolean result = false;
        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getEmail());
            ps.setInt(6, selectedId);
            int executeResult = ps.executeUpdate();
            if (executeResult != 0) {
                System.out.println("Successfully updated!");
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.closePreparedStatement(ps);
            DBConnect.closeConnection(conn);
        }
        return result;
    }

    public boolean delete(int selectedId) {
        String sql = "DELETE `customer` WHERE `id` = ?;";
        boolean result = false;
        try {
            conn = DBConnect.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, selectedId);
            int executeResult = ps.executeUpdate();
            if (executeResult != 0) {
                System.out.println("Successfully deleted!");
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnect.closePreparedStatement(ps);
            DBConnect.closeConnection(conn);
        }
        return result;
    }
}
