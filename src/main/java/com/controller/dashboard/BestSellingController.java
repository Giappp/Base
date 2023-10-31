package com.controller.dashboard;

import com.db.dao.JDBCConnect;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

public class BestSellingController {

    @FXML
    private PieChart bestSellingChart;

    public void initialize() {
        pieChart();
    }

    public void pieChart() {
        String sql = "SELECT P.name AS product_name, SUM(PO.quantity) AS total_quantity_ordered " +
                "FROM pos.product_in_order PO " +
                "JOIN pos.product P ON PO.productId = P.id " +
                "GROUP BY P.name " +
                "ORDER BY total_quantity_ordered DESC";

        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement stmt = Objects.requireNonNull(con).createStatement()) {

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                int totalQuantityOrdered = resultSet.getInt("total_quantity_ordered");

                // Add the data to the PieChart
                PieChart.Data dataPoint = new PieChart.Data(productName, totalQuantityOrdered);
                bestSellingChart.getData().add(dataPoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
