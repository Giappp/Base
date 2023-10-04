package com.model;

import com.db.dao.JDBCConnect;
import com.entities.GoodsImport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GoodsImportModel {
    public boolean importGoods(GoodsImport goodsImport) {
        String sql = "INSERT into goods_import" +
                "(`product_id`,`quantity`,`unit_price`,`total_price`,`date_imported`,`user_id`) " +
                "VALUES(?,?,?,?,?,?) ";
        try (Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, goodsImport.getProductId());
                preparedStatement.setInt(2, goodsImport.getQuantity());
                preparedStatement.setDouble(3, goodsImport.getUnitPrice());
                preparedStatement.setDouble(4, goodsImport.getTotalPrice());
                preparedStatement.setDate(5, goodsImport.getDateImported());
                preparedStatement.setInt(6,goodsImport.getUserId());
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
