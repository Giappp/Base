package com.model;

import com.db.dao.JDBCConnect;
import com.entities.GoodsImport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodsImportModel {
    public boolean importGoods(GoodsImport goodsImport) {
        String sql = "INSERT into goods_import" +
                "(`productId`, `userId`, `quantity`,`unitPrice`,`totalPrice`,`dateImported`) " +
                "VALUES(?,?,?,?,?,?) ";
        try (Connection connection = JDBCConnect.getJDBCConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, goodsImport.getProductId());
                preparedStatement.setInt(2,2);
                preparedStatement.setInt(3, goodsImport.getQuantity());
                preparedStatement.setDouble(4, goodsImport.getUnitPrice());
                preparedStatement.setDouble(5, goodsImport.getTotalPrice());
                preparedStatement.setDate(6, goodsImport.getDateImported());

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
                bindData(goodsImportList, resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return goodsImportList;
    }
    public List<GoodsImport> getDataByDate(Date begin,Date end){
        String sql = "SELECT * FROM goods_import where dateImported >= ? and dateImported <= ?";
        List<GoodsImport> goodsImports = new ArrayList<>();
        try(Connection connection = JDBCConnect.getJDBCConnection()){
            assert connection != null;
            try( PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setDate(1,begin);
                preparedStatement.setDate(2,end);
                ResultSet resultSet = preparedStatement.executeQuery();
                bindData(goodsImports, resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return goodsImports;
    }

    private void bindData(List<GoodsImport> goodsImports, ResultSet resultSet) throws SQLException {
        while(resultSet.next()){
            GoodsImport goodsImport = new GoodsImport();
            goodsImport.setId(resultSet.getInt("id"));
            goodsImport.setProductId(resultSet.getInt("productId"));
            goodsImport.setQuantity(resultSet.getInt("quantity"));
            goodsImport.setUnitPrice(resultSet.getDouble("unitPrice"));
            goodsImport.setTotalPrice(resultSet.getDouble("totalPrice"));
            goodsImport.setDateImported(resultSet.getDate("dateImported"));
            goodsImport.setUserId(resultSet.getInt("userId"));
            goodsImport.setProduct(new ProductModel().getProduct(resultSet.getInt("productId")));
            goodsImports.add(goodsImport);
        }
    }
}
