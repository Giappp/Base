package com.controller.event;

import com.controller.AlertMessages;
import com.db.dao.JDBCConnect;
import com.entities.Customer;
import com.entities.Event;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class EventController {

    @FXML
    private Text actionStatusLabel;

    @FXML
    private TextField tfEventName;

    @FXML
    private TextField tfDiscountPercent;

    @FXML
    private DatePicker dpEventStartDate;

    @FXML
    private DatePicker dpEventEndDate;

    @FXML
    private ComboBox<Integer> cbEventStartHour;

    @FXML
    private ComboBox<Integer> cbEventStartMinute;

    @FXML
    private ComboBox<String> cbEventStartTimeNotation;

    @FXML
    private ComboBox<Integer> cbEventEndHour;

    @FXML
    private ComboBox<Integer> cbEventEndMinute;

    @FXML
    private ComboBox<String> cbEventEndTimeNotation;

    @FXML
    private Button addEventBtn;

    @FXML
    private Button updateEventBtn;

    @FXML
    private Button cancelEventBtn;

    @FXML
    private Button deleteEventBtn;

    @FXML
    private TableView<Event> eventTblv;

    @FXML
    private TableColumn<Event, Integer> eventColId;

    @FXML
    private TableColumn<Customer, Integer> eventColOrderNumber;

    @FXML
    private TableColumn<Event, String> eventColName;

    @FXML
    private TableColumn<Event, Float> eventColDiscount;

    @FXML
    private TableColumn<Event, String> eventColStartDate;

    @FXML
    private TableColumn<Event, String> eventColStartTime;

    @FXML
    private TableColumn<Event, String> eventColEndDate;

    @FXML
    private TableColumn<Event, String> eventColEndTime;

    @FXML
    private TextField searchEvent;

    @FXML
    private Pagination eventPg;

    @FXML
    private Label validateFields;

    @FXML
    private TextField idTextField;

    ObservableList<Event> eventObservableList = FXCollections.observableArrayList();

    private int currentPage = 1;

    private static final int itemsPerPage = 12; // final variable to specify number of items per page

    AlertMessages alertMessages;

    public void initialize() {
        setIdAdd();
        selectedRecord();
        setupTable();
        setupPagination();
        setStartHourComboBox();
        setEndHourComboBox();
        setStartTimeNotationComboBox();
        setEndTimeNotationComboBox();
        setStartMinuteComboBox();
        setEndMinuteComboBox();
        validateFields();

        cancelEventBtn.setOnAction(event -> clearInfo());
    }

    private void clearInfo() {
        tfEventName.clear();
        tfDiscountPercent.clear();
        dpEventStartDate.getEditor().clear();
        cbEventStartHour.getEditor().clear();
        cbEventStartMinute.getEditor().clear();
        cbEventStartTimeNotation.getEditor().clear();
        dpEventEndDate.getEditor().clear();
        cbEventEndHour.getEditor().clear();
        cbEventEndMinute.getEditor().clear();
        cbEventEndTimeNotation.getEditor().clear();
        addEventBtn.setDisable(false);
        deleteEventBtn.setDisable(true);
        updateEventBtn.setDisable(true);
    }

    private void validateFields() {
        eventColName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(newValue);
        });

        // Add a listener to the discount text field for numeric input validation
        tfDiscountPercent.setTextFormatter(createDecimalTextFormatter());
    }

    private TextFormatter<String> createDecimalTextFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                eventColDiscount.setStyle(""); // Clear any validation style
                return change;
            } else {
                tfDiscountPercent.setStyle("-fx-border-color: red;");
                return null; // Reject input that contains non-numeric characters or multiple decimal points
            }
        });
    }

    private void validateName(String newValue) {
        if (newValue.isEmpty()) {
            eventColName.setStyle("-fx-border-color: red;");
        } else {
            eventColName.setStyle(""); // Clear red border if valid
        }
    }

    private ObservableList<Event> getListEvent() {
        ObservableList<Event> observableList = FXCollections.observableArrayList();
        String sql = "SELECT id, eventName, discount, startDate, startTime, endDate, endTime FROM `event`";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // iterate through the resultSet from db and add to list
                int id = rs.getInt("id");
                String event_name = rs.getString("eventName");
                float discount = rs.getFloat("discount");
                String start_date = rs.getString("startDate");
                String start_time = rs.getString("startTime");
                String end_date = rs.getString("endDate");
                String end_time = rs.getString("endTime");

                // add to list
                observableList.add(new Event(id, event_name, discount, start_date, start_time, end_date, end_time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    private void setupTable() {
        eventObservableList = getListEvent();
        eventColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventColName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventColDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        eventColStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        eventColStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        eventColEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        eventColEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        eventColOrderNumber.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(eventTblv.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        // Create a custom cell factory for the event_col_start_date
        eventColStartTime.setCellFactory(new Callback<TableColumn<Event, String>, TableCell<Event, String>>() {
            @Override
            public TableCell<Event, String> call(TableColumn<Event, String> column) {
                return new TableCell<Event, String>() {
                    private final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    private final DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            try {
                                Date date = inputFormat.parse(item);
                                setText(outputFormat.format(date));
                            } catch (ParseException e) {
                                setText("");
                            }
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        // Create a custom cell factory for the event_col_end_date
        eventColEndDate.setCellFactory(new Callback<TableColumn<Event, String>, TableCell<Event, String>>() {
            @Override
            public TableCell<Event, String> call(TableColumn<Event, String> column) {
                return new TableCell<Event, String>() {
                    private final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    private final DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            try {
                                Date date = inputFormat.parse(item);
                                setText(outputFormat.format(date));
                            } catch (ParseException e) {
                                setText("");
                            }
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        // create FilteredList to filter and search car by search_event
        FilteredList<Event> filteredList = new FilteredList<>(eventObservableList, b -> true); // b->true : means all elements in the list will be included in the filteredList

        // listen to changes in the searchKeyword to update the tableView
        searchEvent.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(events -> {
                String searchKeyword = newValue.toLowerCase();

                return events.getEventName().toLowerCase().contains(searchKeyword);
            });
            // update pagination
            updatePagination(filteredList);

        });
        // update pagination
        updatePagination(filteredList);
        updateEventBtn.setDisable(true);
    }

    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, eventObservableList.size());

        // Clear the table and re-add the events for the current page
        eventTblv.getItems().clear();
        eventTblv.getItems().addAll(eventObservableList.subList(fromIndex, toIndex));
    }

    private void setupPagination() {
        int totalPages = (eventObservableList.size() / itemsPerPage) + (eventObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        eventPg.setPageCount(totalPages);

        eventPg.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    private void updatePagination(FilteredList<Event> filteredList) {
        eventPg.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPage = newIndex.intValue() + 1;
        });

        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        // adjust the pagination's page count and current page if needed
        if (pageCount == 0) {
            pageCount = 1;
        }
        eventPg.setPageCount(pageCount);

        if (eventPg.getCurrentPageIndex() >= pageCount) {
            eventPg.setCurrentPageIndex(pageCount - 1);
        }

        // update the tableView base on the current page
        int fromIndex = eventPg.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        SortedList<Event> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(eventTblv.comparatorProperty());

        eventTblv.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    private int setIdAdd() {
        String sql = "SELECT id FROM event ORDER BY id DESC LIMIT 1";
        try (Connection con = JDBCConnect.getJDBCConnection();
             Statement statement = Objects.requireNonNull(con).createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int eventIdIncrease = resultSet.getInt("id") + 1;
                eventColId.setText(String.valueOf(eventIdIncrease));
                return eventIdIncrease;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void setStartTimeNotationComboBox() {
        cbEventStartTimeNotation.getItems().addAll("A.M", "P.M");
        cbEventStartTimeNotation.setValue("A.M");
    }

    private void setEndTimeNotationComboBox() {
        cbEventEndTimeNotation.getItems().addAll("A.M", "P.M");
        cbEventEndTimeNotation.setValue("A.M");
    }

    private void setStartHourComboBox() {
        cbEventStartHour.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        cbEventStartHour.setValue(7);
    }

    private void setEndHourComboBox() {
        cbEventEndHour.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        cbEventEndHour.setValue(7);
    }

    private void setStartMinuteComboBox() {
        cbEventStartMinute.getItems().addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        cbEventStartMinute.setValue(0);
    }

    private void setEndMinuteComboBox() {
        cbEventEndMinute.getItems().addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        cbEventEndMinute.setValue(0);
    }

    public void resetForm() {
        searchEvent.clear();
        setIdAdd(); // set new value for id field
        tfEventName.clear();
        tfDiscountPercent.clear();
        dpEventStartDate.setValue(null);
        dpEventStartDate.getEditor().clear();
        dpEventEndDate.setValue(null);
        dpEventEndDate.getEditor().clear();
        cbEventStartMinute.setValue(7);
        cbEventStartMinute.setValue(0);
        cbEventStartTimeNotation.setValue("A.M");
        cbEventEndHour.setValue(7);
        cbEventEndMinute.setValue(0);
        cbEventEndTimeNotation.setValue("A.M");

        actionStatusLabel.setText("Adding New Event");
        addEventBtn.setDisable(false);
        updateEventBtn.setDisable(true);
    }

    private void selectedRecord() {
        eventTblv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observableValue, Event oldValue, Event newValue) {
                if (newValue != null) {
                    eventColId.setText(String.valueOf(newValue.getId()));
                    eventColName.setText(String.valueOf(newValue.getEventName()));
                    tfDiscountPercent.setText(String.valueOf(newValue.getDiscount()));

                    // Set value for the startDateDatePicker
                    String startDateString = String.valueOf(newValue.getStartDate());
                    if (startDateString != null && !startDateString.isEmpty()) {
                        LocalDate startDate = LocalDate.parse(startDateString);
                        dpEventStartDate.setValue(startDate);
                    } else {
                        dpEventStartDate.setValue(null);
                    }

                    // Set value for the endDateDatePicker
                    String endDateString = String.valueOf(newValue.getEndDate());
                    if (endDateString != null && !endDateString.isEmpty()) {
                        LocalDate endDate = LocalDate.parse(endDateString);
                        dpEventEndDate.setValue(endDate);
                    } else {
                        dpEventEndDate.setValue(null);
                    }


                    String[] startTimeParts = newValue.getStartTime().split(":");
                    int startHour = Integer.parseInt(startTimeParts[0]);
                    int startMinute = Integer.parseInt(startTimeParts[1]);

                    // Convert 24-hour format to 12-hour format
                    int startHour12 = startHour > 12 ? startHour - 12 : (startHour == 0 ? 12 : startHour);
                    String startTimeNotation = startHour >= 12 ? "P.M" : "A.M";

                    cbEventStartHour.setValue(startHour12);
                    cbEventStartMinute.setValue(startMinute);
                    cbEventStartTimeNotation.setValue(startTimeNotation);

                    String[] endTimeParts = newValue.getEndTime().split(":");
                    int endHour = Integer.parseInt(endTimeParts[0]);
                    int endMinute = Integer.parseInt(endTimeParts[1]);

                    // Convert 24-hour format to 12-hour format
                    int endHour12 = endHour > 12 ? endHour - 12 : (endHour == 0 ? 12 : endHour);
                    String endTimeNotation = endHour >= 12 ? "P.M" : "A.M";

                    cbEventEndHour.setValue(endHour12);
                    cbEventEndMinute.setValue(endMinute);
                    cbEventEndTimeNotation.setValue(endTimeNotation);

                    addEventBtn.setDisable(true);
                    updateEventBtn.setDisable(false);
                    actionStatusLabel.setText("Updating Event");
                } else {
                    resetForm();
                }
            }
        });
    }

    private boolean isFilledFields() {
        if (tfEventName.getText().isEmpty()
                || tfDiscountPercent.getText().isEmpty()
                || dpEventStartDate.getEditor() == null
                || cbEventStartTimeNotation.getItems().isEmpty()
                || cbEventStartHour.getItems().isEmpty()
                || cbEventStartMinute.getItems().isEmpty()
                || dpEventEndDate.getEditor() == null
                || cbEventEndHour.getItems().isEmpty()
                || cbEventEndMinute.getItems().isEmpty()
                || cbEventEndTimeNotation.getItems().isEmpty()) {
            // alert error if required fields no filled
            alertMessages.warningMessage("Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }

    public void addToDatabase() {
        String sql = "INSERT INTO `event`(eventName, discount, startDate, startTime, endDate, endTime) VALUES (?,?,?,?,?,?);";

        try (Connection con = JDBCConnect.getJDBCConnection()) {
            // check required fields filled or not
            if (isFilledFields()) {
                PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql);
                ps.setString(1, tfEventName.getText());
                ps.setString(2, tfDiscountPercent.getText());
                ps.setString(3, dpEventStartDate.getValue().toString());

                int selectedStartHour = cbEventStartHour.getValue();
                int selectedStartMinute = cbEventStartMinute.getValue();
                String selectedStartTimeNotation = cbEventStartTimeNotation.getValue();
                int startHour24 = selectedStartTimeNotation.equals("P.M") ? selectedStartHour + 12 : selectedStartHour;
                String formattedStartTime = String.format("%02d:%02d:00", startHour24, selectedStartMinute);
                ps.setString(4, formattedStartTime);

                ps.setString(5, dpEventEndDate.getValue().toString());
                int selectedEndHour = cbEventEndHour.getValue();
                int selectedEndMinute = cbEventEndMinute.getValue();
                String selectedEndTimeNotation = cbEventEndTimeNotation.getValue();
                int endHour24 = selectedEndTimeNotation.equals("P.M") ? selectedEndHour + 12 : selectedEndHour;
                String formattedEndTime = String.format("%02d:%02d:00", endHour24, selectedEndMinute);
                ps.setString(6, formattedEndTime);

                // execute
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    // alert success if add data successfully
                    alertMessages.successMessage("Added successfully!");

                    // update the table and reset the form
                    setupTable();
                    resetForm();
                } else {
                    // alert error if adding data failed
                    alertMessages.errorMessage("Failed to add event record.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // alert error if an exception occurs
            alertMessages.errorMessage("An error occurred while adding event record.");
        }
    }

    public void updateToDatabase() {
        String sql = "UPDATE `event` SET eventName = ?, discount = ?, startDate = ?, startTime = ?, endDate = ?, endTime = ? WHERE id = ?";

        try (Connection con = JDBCConnect.getJDBCConnection()) {
            if (isFilledFields()) {
                PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql);
                ps.setString(1, tfEventName.getText());
                ps.setString(2, tfDiscountPercent.getText());

                DateTimeFormatter startDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDateValue = dpEventStartDate.getValue();
                String startDateString = startDateValue != null ? startDateValue.format(startDateFormatter) : null;
                ps.setString(3, startDateString);

                int selectedStartHour = cbEventStartHour.getValue();
                int selectedStartMinute = cbEventStartMinute.getValue();
                String selectedStartTimeNotation = cbEventStartTimeNotation.getValue();
                int startHour24 = selectedStartTimeNotation.equals("P.M") ? selectedStartHour + 12 : selectedStartHour;
                String formattedStartTime = String.format("%02d:%02d:00", startHour24, selectedStartMinute);
                ps.setString(4, formattedStartTime);

                DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate endDateValue = dpEventEndDate.getValue();
                String endDateString = endDateValue != null ? endDateValue.format(endDateFormatter) : null;
                ps.setString(5, endDateString);

                int selectedEndHour = cbEventEndHour.getValue();
                int selectedEndMinute = cbEventEndHour.getValue();
                String selectedEndTimeNotation = cbEventEndTimeNotation.getValue();
                int endHour24 = selectedEndTimeNotation.equals("P.M") ? selectedEndHour + 12 : selectedEndHour;
                String formattedEndTime = String.format("%02d:%02d:00", endHour24, selectedEndMinute);
                ps.setString(6, formattedEndTime);

                ps.setString(7, idTextField.getText());

                int rowAffected = ps.executeUpdate();
                // if updated success then alert
                if (rowAffected > 0) {
                    alertMessages.successMessage("Updated successfully!");

                    setupTable();
                    resetForm();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase() {
        String sql = "DELETE FROM `event` WHERE id = ?";
        try (Connection con = JDBCConnect.getJDBCConnection();
             PreparedStatement ps = Objects.requireNonNull(con).prepareStatement(sql)) {

            ps.setString(1, idTextField.getText());

            int rowAffected = ps.executeUpdate();

            if (rowAffected > 0) {
                alertMessages.successMessage("Deleted successfully!");

                setupTable();
                resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
