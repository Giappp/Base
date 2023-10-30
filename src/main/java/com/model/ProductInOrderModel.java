package com.model;

import com.db.dao.JDBCConnect;
import com.entities.ProductInOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductInOrderModel {
    public boolean addProductToOrder(List<ProductInOrder> productInOrderList,int orderId){
        String sql = "INSERT INTO product_in_order(`orderId`,`productId`,`quantity`) VALUES(?,?,?)";
        try(Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                for(ProductInOrder product:productInOrderList){
                    preparedStatement.setInt(1,orderId);
                    preparedStatement.setInt(2,product.getProductId());
                    preparedStatement.setInt(3,product.getQuantity());
                    preparedStatement.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
