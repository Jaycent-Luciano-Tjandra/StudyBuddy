package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;


import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController   {

    @FXML
    private TextField emailTextField;
    
    @FXML
    private TextField taInformation;
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/loginstudybuddy";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "";

    private User currentUser;
    
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
    
    //login

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty");
        } else {
            if (validateLogin(email, password)) {
                currentUser = getUserByEmail(email);
                loadCalendarScene();
            } else {
                showAlert("Error", "Invalid email or password");
            }
        }
    }

    private User getUserByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt("id");
                        String firstName = resultSet.getString("name");
                        return new User(userId, firstName, email);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validateLogin(String email, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        return verifyPassword(password, storedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return inputPassword.equals(storedPassword);
    }

    public void loadCalendarScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Parent root = loader.load();

            // Access the controller and set the current user
            TableController tableController = loader.getController();
            tableController.setCurrentUser(currentUser);
            tableController.initialize();
            
            
            // Display the scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load Calendar scene");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

   
//
//
//    public void setRealTimeUpdates() {
//    	Thread updateTimeThread = new Thread(() -> {
//    	    try {
//    	        while (true) {
//    	            // Get the current time and date
//    	            String currentTime = getCurrentTime();
//    	            String currentDate = getCurrentDate();
//
//    	            // Update the UI in JavaFX Application Thread
//    	            javafx.application.Platform.runLater(() -> {
//    	                timeLabel.setText(currentTime);
//    	                dateLabel.setText(currentDate);
//    	            });
//
//    	            // Sleep for 1 second
//    	            Thread.sleep(1000);
//    	        }
//    	    } catch (InterruptedException e) {
//    	        e.printStackTrace();
//    	    }
//    	});
//
//    	// Start the thread
//    	updateTimeThread.setDaemon(true);
//    	updateTimeThread.start();
//
//    }
//
//    public void initialize(URL location, ResourceBundle resources) {
//        // ...
//    }
// {
//        System.out.println("Initializing Controller...");
//
//        // Check if labels are null
//        System.out.println("timeLabel: " + timeLabel);
//        System.out.println("dateLabel: " + dateLabel);
//
//        // This method is called when the FXML file is loaded
//        setRealTimeUpdates();
//    }
//    
//    private String getCurrentTime() {
//        // Get the current time in HH:mm format
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        return timeFormat.format(new Date());
//    }
//
//    private String getCurrentDate() {
//        // Get the current date in a format like "15 November 2023"
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
//        return dateFormat.format(new Date());
//    }

    

    @FXML
    private Label label;
    private Stage stage;
    private Scene scene;
    private Parent root;
    

    
    public void switchToSplashScreen (ActionEvent event) throws IOException {
      root = FXMLLoader.load(getClass().getResource("/application/SplashScreen.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     stage.setScene(scene);
     stage.show();
    }
    
    public void switchToHome(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
 
    
    public void switchToCalendar (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/Calendar.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    public void switchToTimer (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/menuTimer.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    
    public void switchToSignIn (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/signin3.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    public void switchToMusicPage (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/PlayMusic.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    public void switchToMenuCalendar (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/Calendar.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    public void switchToScreenTime (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/menuBar.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    

    public void switchToMenuFolder (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/menuAdd.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    public void switchToLogin (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/login.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }
    
    
    

    
//    public void openFile(ActionEvent event) {
//        String buildpath = (String) event.getSource().getUserData();
//
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("open", buildpath);
//            processBuilder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//   


//
//    @FXML
//    private PasswordField pfPassword;
//    @FXML
//    private Button lblPassword;
//
//    @FXML
//    private TextField taInformation;
//
//    private boolean isShowingPassword = false;
//    
//
//    @FXML
//    void handleButtonAction(ActionEvent event) {
//        if (isShowingPassword) {
//            taInformation.clear();
//        } else {
//            taInformation.setText(pfPassword.getText());
//        }
//        isShowingPassword = !isShowingPassword;
//    }
//
//
//
//        @FXML
//        private Label chooseMusic;
//
//        private MediaPlayer mediaPlayer;
//
//        @FXML
//        void chooseMusic(MouseEvent event) {
//            FileChooser chooser = new FileChooser();
//            chooser.setTitle("Select your music");
//
//            // Set extension filter to restrict to audio files
//            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg");
//            chooser.getExtensionFilters().add(extFilter);
//
//            File file = chooser.showOpenDialog(null);
//            if (file != null) {
//                String selectedFile = file.toURI().toString();
//
//                // Check if the file is a video (contains video extensions)
//                List<String> videoExtensions = Arrays.asList(".mp4", ".avi", ".mkv");
//                boolean isVideo = videoExtensions.stream().anyMatch(ext -> file.getName().toLowerCase().endsWith(ext));
//
//                // If it's a video, play only the audio
//                if (isVideo) {
//                    Media media = new Media(selectedFile);
//                    mediaPlayer = new MediaPlayer(media);
//                    mediaPlayer.setOnReady(() -> chooseMusic.setText(file.getName() + " (Audio Only)"));
//                } else {
//                    // If it's an audio file, play normally
//                    Media media = new Media(selectedFile);
//                    mediaPlayer = new MediaPlayer(media);
//                    mediaPlayer.setOnReady(() -> chooseMusic.setText(file.getName()));
//                }
//            }
//        }
//
//        @FXML
//        void pause(MouseEvent event) {
//            if (mediaPlayer != null) {
//                mediaPlayer.pause();
//            }
//        }
//
//        @FXML
//        void play(MouseEvent event) {
//            if (mediaPlayer != null) {
//                mediaPlayer.play();
//            }
//        }
//
//        @FXML
//        void stop(MouseEvent event) {
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//            }
//        }
    


    
   
}
