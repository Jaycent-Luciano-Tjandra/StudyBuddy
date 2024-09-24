package application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarController{
    private Stage calendarStage;
    private User currentUser;
    private int selectedDay = -1;
    @FXML
    private Button addEventBtn;

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginstudybuddy?user=root@localhost";


    @FXML
    private Label monthLabel;

    @FXML
    private GridPane calendarGridPane;

    @FXML
    private TextArea eventTextArea;

    private LocalDate currentDate;
    private List<String> events;

    @FXML
	public void initialize() {
        currentDate = LocalDate.now();
        events = new ArrayList<>();
        updateUserEvents(); // Retrieve events for the logged-in user
        updateCalendar();
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void setCalendarStage(Stage calendarStage) {
        this.calendarStage = calendarStage;
    }

    @FXML
    private void handleDateButtonClick(int day) {
        selectedDay = day;
        LocalDate selectedDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), selectedDay);
        String eventText = showEventsForDate(selectedDate);
        eventTextArea.setText(eventText);
    }

    @FXML
    private void handleAddEventButton() {
        String eventText = eventTextArea.getText().trim();
        if (!eventText.isEmpty() && selectedDay != -1) {
            saveEventToDatabase(selectedDay, eventText);
            updateCalendar();
            eventTextArea.clear();
        } else {
            showAlert("Error", "Please select a day and enter event details.");
        }
    }

    private void saveEventToDatabase(int day, String eventText) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "INSERT INTO events (user_id, event_date, event_description) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, currentUser.getUserId());

                // Construct the event date using selectedDay
                LocalDate selectedDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);

                statement.setDate(2, Date.valueOf(selectedDate));
                statement.setString(3, eventText);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    

    @FXML
    private void handlePrevYearButton() {
        currentDate = currentDate.minusYears(1);
        updateCalendar();
    }

    @FXML
    private void handlePrevMonthButton() {
        currentDate = currentDate.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleNextMonthButton() {
        currentDate = currentDate.plusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleNextYearButton() {
        currentDate = currentDate.plusYears(1);
        updateCalendar();
    }

   
   
    public void loadCalendarScene() {
        updateUserEvents(); // Call updateUserEvents when loading the calendar scene
        // Any other initialization or UI updates you might need...
    }


	int finalday;

    private void updateCalendar() {
        monthLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        calendarGridPane.getChildren().clear();

        int daysInMonth = currentDate.lengthOfMonth();
        int day = 1;

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int col = 0; col < 7; col++) {
            Label dayLabel = new Label(dayNames[col]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            calendarGridPane.add(dayLabel, col, 0);
        }

        for (int row = 1; row <= 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (day <= daysInMonth && (row == 1 && col >= currentDate.withDayOfMonth(1).getDayOfWeek().getValue() || row > 1)) {
                    Button dayButton = new Button(Integer.toString(day));

                    dayButton.setPrefWidth(30);
                    dayButton.setPrefHeight(30);

                    calendarGridPane.add(dayButton, col, row);

                    LocalDate currentDay = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
                    if (hasEventsForDay(currentDay)) {
                        dayButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
                    }

                    if (currentDate.getDayOfMonth() == day) {
                        dayButton.setStyle(dayButton.getStyle() + "; -fx-background-color: lightblue;");
                    }

                    int capturedDay = day;
                    dayButton.setOnAction(e -> handleDateButtonClick(capturedDay));

                    day++;
                }
            }
        }
    }

    private boolean hasEventsForDay(LocalDate date) {
        return events.stream()
                .anyMatch(event -> LocalDate.parse(event.split(":")[0].trim()).equals(date));
    }


    private String showEventsForDate(LocalDate date) {
        StringBuilder eventText = new StringBuilder();
        events.stream()
                .filter(event -> LocalDate.parse(event.split(":")[0].trim()).equals(date))
                .forEach(event -> eventText.append(event.split(":")[1].trim()).append("\n"));
        return eventText.toString();
    }

    private void updateUserEvents() {
        // Check if currentUser is null before proceeding
        if (currentUser == null) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "SELECT * FROM events WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, currentUser.getUserId());
                // Rest of your code...
            }
        } catch (SQLException e) {
            // Log a more informative message or handle the exception appropriately
            System.err.println("Error updating user events: " + e.getMessage());
            e.printStackTrace();
        }
    }



   

    private StringBuilder retrieveEventsFromDatabase() {
        StringBuilder eventsForMonth = new StringBuilder("Events for " + currentDate.getMonth() + " " + currentDate.getYear() + ":\n");
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "SELECT * FROM events WHERE user_id = ? AND MONTH(event_date) = ? AND YEAR(event_date) = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, currentUser.getUserId());
                statement.setInt(2, currentDate.getMonthValue());
                statement.setInt(3, currentDate.getYear());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String eventDate = resultSet.getString("event_date");
                        String eventDescription = resultSet.getString("event_description");
                        eventsForMonth.append(eventDate).append(": ").append(eventDescription).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventsForMonth;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showEventsPopup(String eventsText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Events");
        alert.setHeaderText(null);
        TextArea textArea = new TextArea(eventsText);
        textArea.setEditable(false);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

   

}