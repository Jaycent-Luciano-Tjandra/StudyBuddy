package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
public class SignInController implements Initializable {

    @FXML
    private DatePicker myDatePicker;
    @FXML
    private ChoiceBox<String> MonthBox;
    @FXML
    private Label label;
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Button btnLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextField taInformation;
    
    @FXML
    private TextField emailTextField;

// sign in
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginstudybuddy?user=root@localhost";
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void shownotif(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText(); // 

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            showAlert("Error", "Name, email, and password cannot be empty");
        } else if (name.length() <= 2) {
            showAlert("Error", "Name must be more than 2 characters");
        } else if (password.length() <= 5 || !containsLettersAndDigits(password)) {
            showAlert("Error", "Password must be more than 5 characters and contain both letters and digits");
        } else if (!email.contains("@")) {
            showAlert("Error", "Invalid email address. Please include '@' in the email.");
        } else {
            if (registerUser(name, email, password)) {
                shownotif("Success", "User registered successfully. You can now log in.");
                try {
                    switchToLogin(event);
                } catch (IOException e) {
                    e.printStackTrace();
                } // Redirect to login page after successful registration
            } else {
                showAlert("Error", "Registration failed. Please try again.");
            }
        }
    }

    

    
    private boolean containsLettersAndDigits(String str) {
        boolean containsLetters = false;
        boolean containsDigits = false;

        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetters = true;
            } else if (Character.isDigit(c)) {
                containsDigits = true;
            }
        }

        return containsLetters && containsDigits;
    }

    private boolean registerUser(String name, String email, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


     
    
    
    
    @FXML
    private void getDate() {
        LocalDate selectedDate = myDatePicker.getValue();

        if (selectedDate != null) {
            System.out.println("Selected Date: " + selectedDate);
            // Do something with the selected date
        } else {
            System.out.println("No date selected");
        }
    }
    
//    @FXML
//    private Label MonthLabel;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        MonthBox.getItems().addAll("Jan", "Feb");
//    }
    
    public void switchToLogin (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/onboarding.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    public void handleButtonAction(ActionEvent event) {
        // Get the password from the PasswordField
       String password = passwordTextField.getText();

        // Check if the password is not empty and is not already present in taInformation
        if (!password.isEmpty() && !taInformation.getText().contains(password)) {
            // Append the password to the TextField
            taInformation.appendText(password);
        } else {
            // If the password is empty, clear the text in taInformation
            taInformation.clear();
        }
    }


//    
//  
//
//    @FXML
//    void handleButtonAction(ActionEvent event) {
//        // Get the password from the PasswordField
//        String password = pfPassword.getText();
//
//        // Check if the password is not empty and is not already present in taInformation
//        if (!password.isEmpty() && !taInformation.getText().contains(password)) {
//            // Append the password to the TextField
//            taInformation.appendText(password);
//        } else {
//            // If the password is empty, clear the text in taInformation
//            taInformation.clear();
//        }
//    }

  
    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    public void initialize() {
        // This method is called when the FXML file is loaded
        setRealTimeUpdates();
    }

    public void setRealTimeUpdates() {
        // Add real-time update logic here
        Thread updateTimeThread = new Thread(() -> {
            try {
                while (true) {
                    // Get the current time and date
                    String currentTime = getCurrentTime();
                    String currentDate = getCurrentDate();

                    // Update the UI in JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> {
                        timeLabel.setText(currentTime);
                        dateLabel.setText(currentDate);
                    });

                    // Sleep for 1 second
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start the thread
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    private String getCurrentTime() {
        // Get the current time in HH:mm format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(new Date());
    }

    private String getCurrentDate() {
        // Get the current date in a format like "15 November 2023"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(new Date());
    }


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        }
        
        

    }



//@FXML
//private Label MonthLabel;
//@FXML 
//private ChoiceBox <String> MonthBox;
//   
//private String[] MonthList = {"Jan", "Feb"};
//
//@Override 
//public void initialize(URL arg0, ResourceBundle arg1) {
//
//	MonthBox.getItems().addAll(MonthList);
//}
//}

