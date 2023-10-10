package com.model;

import com.db.dao.JDBCConnect;
import com.entities.GoodsImport;
import com.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<GoodsImport> getData(){
        String sql = "SELECT * from goods_import";
        List<GoodsImport> goodsImportList = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try( PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    GoodsImport goodsImport = new GoodsImport();
                    goodsImport.setId(resultSet.getInt("id"));
                    goodsImport.setProductId(resultSet.getInt("product_id"));
                    goodsImport.setQuantity(resultSet.getInt("quantity"));
                    goodsImport.setUnitPrice(resultSet.getDouble("unit_price"));
                    goodsImport.setTotalPrice(resultSet.getDouble("total_price"));
                    goodsImport.setDateImported(resultSet.getDate("date_imported"));
                    goodsImport.setUserId(resultSet.getInt("user_id"));
                    goodsImport.setProduct(new ProductModel().getProduct(resultSet.getInt("product_id")));
                    goodsImportList.add(goodsImport);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return goodsImportList;
    }
}
