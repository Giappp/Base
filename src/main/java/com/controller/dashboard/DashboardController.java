package com.controller.dashboard;

import com.db.dao.JDBCConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Text earningText;

    @FXML
    private Text totalOrderText;

    @FXML
    private Text productImportText;

    @FXML
    private Text totalCustomerText;

    @FXML
    private LineChart<String, Number> saleRevenueChart;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    private Label compareRevenueLabel;

    @FXML
    private Label totalOrdersMonthLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label compareTotalOrdersLabel;

    @FXML
    private BarChart<String, Number> saleOrderChart;

    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    public void initialize(URL location, ResourceBundle resourceBundle) {

        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        totalRevenueLabel.setText("Total Revenue " + monthName + ": ");
        totalOrdersMonthLabel.setText("Total Orders " + monthName + ":");

        displayTotalEarning();
        displayTotalOrder();
        displayTotalProductImport();
        displayTotalCustomer();

        // Populate the line chart when the view is initialized
        revenueChart();

        // Apply custom tooltips to each data point
        for (XYChart.Series<String, Number> series : saleRevenueChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                int totalOrders = data.getYValue().intValue();
                Tooltip tooltip = createTooltipRevenue(totalOrders);
                Tooltip.install(data.getNode(), tooltip);
            }
        }

        // Populate the line chart when the view is initialized
        orderChart();

        // Apply custom tooltips to each data point
        for (XYChart.Series<String, Number> series : saleOrderChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                int totalOrders = data.getYValue().intValue();
                Tooltip tooltip = createTooltipOrder(totalOrders);
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }

    public void displayTotalProductImport() {
        String sql = "SELECT COUNT(quantity) AS totalProductImport FROM goods_import";

        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement stmt = Objects.requireNonNull(con).createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int total = rs.getInt("totalProductImport");
                productImportText.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayTotalEarning() {
        String sql = "SELECT SUM(totalPaid) AS totalEarning FROM invoice";

        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement stmt = Objects.requireNonNull(con).createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int total = rs.getInt("totalEarning");
                earningText.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayTotalCustomer() {
        String sql = "SELECT COUNT(*) AS totalCustomer FROM customer";

        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement stmt = Objects.requireNonNull(con).createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int total = rs.getInt("totalCustomer");
                totalCustomerText.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayTotalOrder() {
        String sql = "SELECT COUNT(*) AS totalOrder FROM `order`";

        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement stmt = Objects.requireNonNull(con).createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int total = rs.getInt("totalOrder");
                totalOrderText.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void revenueChart() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        String fullMonthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int currentYear = currentDate.getYear();

        int lastMonth, lastYear = 0;
        if (currentMonth == 1) {
            lastMonth = 12;
            lastYear = currentYear - 1;
        } else {
            lastMonth = currentMonth -1;
            lastYear = currentYear;
        }

        double revenueThisMonth = 0, revenueLastMonth = 0;

        int lastDay = java.time.YearMonth.of(currentYear, currentMonth).lengthOfMonth();

        String sql = "SELECT DAY(dateRecorded) AS day, IFNULL(SUM(totalPaid), 0) AS revenue " +
                "FROM `invoice` " +
                "WHERE YEAR(dateRecorded) = ? AND MONTH(dateRecorded) = ? " +
                "GROUP BY DAY(dateRecorded)";

        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement psThisMonth = Objects.requireNonNull(con).prepareStatement(sql)) {

            psThisMonth.setInt(1, currentYear);
            psThisMonth.setInt(2, currentMonth);

            ResultSet rsThisMonth = psThisMonth.executeQuery();

            PreparedStatement psLastMonth = con.prepareStatement(sql);
            psLastMonth.setInt(1, lastYear);
            psLastMonth.setInt(2, lastMonth);

            ResultSet rsLastMonth = psLastMonth.executeQuery();

            XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (rsThisMonth.next()) {
                int day = rsThisMonth.getInt("day");
                double revenue = rsThisMonth.getDouble("revenue");

                // Add the data points to the series
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(Integer.toString(day), revenue);
                series.getData().add(dataPoint);

                //Create a tooltip for the data point
                Tooltip tooltip = new Tooltip("Revenue: " + revenue + "\nDate: " + day + "/" + currentMonth + "/" + currentYear);
                tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

                // Postpone setting the tooltip until the line chart is fully rendered
                Platform.runLater(() -> {
                    Node node = dataPoint.getNode();
                    Tooltip.install(node, tooltip);

                    // Add event handler to show the tooltip when hovering over the data point
                    node.setOnMouseEntered(event -> tooltip.show(node, event.getScreenX(), event.getScreenY() + 20));
                    node.setOnMouseExited(event -> tooltip.hide());
                });

                revenueThisMonth += revenue;
            }
            while (rsLastMonth.next()) {
                revenueLastMonth += rsLastMonth.getDouble("revenue");
            }

            // Fill in missing days with zero revenue
            for (int day = 1; day <= lastDay; day++) {
                boolean found = false;
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (Integer.parseInt(data.getXValue()) == day) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    series.getData().add(new XYChart.Data<>(Integer.toString(day), 0));
                }
            }

            // Set the name for the series (optional)
            series.setName("Revenue " + fullMonthName);

            // Update revenueLabel and compareRevenueLabel
            revenueLabel.setText(String.format("%.2f", revenueThisMonth));
            compareRevenueLabel.setText(decimalFormat.format(revenueThisMonth - revenueLastMonth));

            // Update revenueLabel and compareRevenueLabel
            revenueLabel.setText(decimalFormat.format(revenueThisMonth));

            double compareRevenue = revenueThisMonth - revenueLastMonth;

            // Set the text fill based on compareRevenue value
            if (compareRevenue > 0) {
                compareRevenueLabel.setText("+" + decimalFormat.format(compareRevenue));
                compareRevenueLabel.setTextFill(Color.GREEN);
            } else {
                compareRevenueLabel.setText(decimalFormat.format(compareRevenue));
                compareRevenueLabel.setTextFill(Color.RED);
            }

            // Sort the data by day
            series.getData().sort(Comparator.comparing(data -> Integer.parseInt(data.getXValue())));

            saleRevenueChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tooltip createTooltipRevenue(int totalAmount) {
        Tooltip tooltip = new Tooltip("Orders: " + totalAmount);

        // Apply custom CSS styles to the tooltip
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
        return tooltip;
    }

    public void orderChart() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        String fullMonthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        int currentYear = currentDate.getYear();

        int lastMonth, lastYear = 0;
        if (currentMonth == 1) {
            lastMonth = 12;
            lastYear = currentYear - 1;
        } else {
            lastMonth = currentMonth -1;
            lastYear = currentYear;
        }

        int ordersThisMonth = 0, ordersLastMonth = 0;

        int lastDay = java.time.YearMonth.of(currentYear, currentMonth).lengthOfMonth();

        String sql = "SELECT DAY(dateRecorded) AS day, IFNULL(COUNT(*), 0) AS totalOrders " +
                "FROM `order` " +
                "WHERE YEAR(dateRecorded) = ? AND MONTH(dateRecorded) = ? " +
                "GROUP BY DAY(dateRecorded)";

        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement psThisMonth = Objects.requireNonNull(con).prepareStatement(sql)) {

            psThisMonth.setInt(1, currentYear);
            psThisMonth.setInt(2, currentMonth);

            ResultSet rsThisMonth = psThisMonth.executeQuery();

            PreparedStatement psLastMonth = con.prepareStatement(sql);
            psLastMonth.setInt(1, lastYear);
            psLastMonth.setInt(2, lastMonth);

            ResultSet rsLastMonth = psLastMonth.executeQuery();

            XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (rsThisMonth.next()) {
                int day = rsThisMonth.getInt("day");
                int orders = rsThisMonth.getInt("totalOrders");

                // Add the data points to the series
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(Integer.toString(day), orders);
                series.getData().add(dataPoint);

                //Create a tooltip for the data point
                Tooltip tooltip = new Tooltip("Orders: " + orders + "\nDate: " + day + "/" + currentMonth + "/" + currentYear);
                tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

                // Postpone setting the tooltip until the line chart is fully rendered
                Platform.runLater(() -> {
                    Node node = dataPoint.getNode();
                    Tooltip.install(node, tooltip);

                    // Add event handler to show the tooltip when hovering over the data point
                    node.setOnMouseEntered(event -> tooltip.show(node, event.getScreenX(), event.getScreenY() + 20));
                    node.setOnMouseExited(event -> tooltip.hide());
                });

                ordersThisMonth += orders;
            }
            while (rsLastMonth.next()) {
                ordersLastMonth += rsLastMonth.getInt("totalOrders");
            }

            // Fill in missing days with zero revenue
            for (int day = 1; day <= lastDay; day++) {
                boolean found = false;
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (Integer.parseInt(data.getXValue()) == day) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    series.getData().add(new XYChart.Data<>(Integer.toString(day), 0));
                }
            }

            // Set the name for the series (optional)
            series.setName("Orders " + fullMonthName);

            // Update revenueLabel and compareRevenueLabel
            totalOrdersLabel.setText(String.valueOf(ordersThisMonth));
            compareTotalOrdersLabel.setText(String.valueOf(ordersThisMonth - ordersLastMonth));

            totalOrdersLabel.setText(String.valueOf(ordersThisMonth));

            int compareOrders = ordersThisMonth - ordersLastMonth;


            // Set the text fill based on compareRevenue value
            if (compareOrders > 0) {
                compareTotalOrdersLabel.setText("+" + compareOrders);
                compareTotalOrdersLabel.setTextFill(Color.GREEN);
            } else {
                compareTotalOrdersLabel.setText(String.valueOf(compareOrders));
                compareTotalOrdersLabel.setTextFill(Color.RED);
            }

            // Sort the data by day
            series.getData().sort(Comparator.comparing(data -> Integer.parseInt(data.getXValue())));

            saleOrderChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tooltip createTooltipOrder(int totalOrders) {
        Tooltip tooltip = new Tooltip("Orders: " + totalOrders);

        // Apply custom CSS styles to the tooltip
        tooltip.setStyle("-fx-background-color: #333333; -fx-text-fill: white;");

        return tooltip;
    }
}
