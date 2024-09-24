package application;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class MainPanelController implements Initializable {

    @FXML
    private AnchorPane timerPane;

    @FXML
    private Text hoursTimer;

    @FXML
    private Text minutesTimer;

    @FXML
    private Text secondsTimer;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane menuPane;

    @FXML
    private ComboBox<Integer> hoursInput;

    @FXML
    private ComboBox<Integer> minutesInput;

    @FXML
    private ComboBox<Integer> secondsInput;

    @FXML
    private Button buttonStart;

    private Map<Integer, String> numberMap;
    private int currSeconds;
    private volatile boolean stopThread = false;
    private Thread thrd;

    @FXML
    private Label label;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void playNotificationSound() {
        try {
            // Adjust the file path based on the location of your sound file
            String soundFilePath = "/Users/teresaangela/short-success-sound-glockenspiel-treasure-video-game-6346.mp3";

            // Encode the file path to handle spaces and special characters
            soundFilePath = soundFilePath.replace(" ", "%20");
            soundFilePath = new URL("file://" + soundFilePath).toExternalForm();

            Media sound = new Media(soundFilePath);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// timer
    @SuppressWarnings("exports")
    @FXML
    public void Start(ActionEvent event) {
        stopThread = true;
        if (thrd != null) {
            try {
                thrd.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                thrd = null; // Set thrd to null after joining
            }
        }

        currSeconds = hmsToSeconds(hoursInput.getValue(), minutesInput.getValue(), secondsInput.getValue());
        hoursInput.setValue(0);
        minutesInput.setValue(0);
        secondsInput.setValue(0);
        stopThread = false;

        scrollUp();


    }
 
    void startCountdown() {
        thrd = new Thread(() -> {
            try {
                while (!stopThread) {
                    setOutput();
                    Thread.sleep(1000);
                    if (currSeconds == 0) {
                        scrollDown();
                        stopThread = true;
                        playNotificationSound();  // Play notification sound
                    }
                    currSeconds -= 1;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thrd.start();
    }


    void setOutput() {
        LinkedList<Integer> currHms = secondstoHms(currSeconds);
        hoursTimer.setText(numberMap.get(currHms.get(0)));
        minutesTimer.setText(numberMap.get(currHms.get(1)));
        secondsTimer.setText(numberMap.get(currHms.get(2)));
    }

    @SuppressWarnings("exports")
    @FXML
    public void unStart(ActionEvent event) {
        stopThread = false;
        scrollDown();
    }

    Integer hmsToSeconds(Integer h, Integer m, Integer s) {
        Integer hToSeconds = h * 3600;
        Integer mToSeconds = m * 60;
        return hToSeconds + mToSeconds + s;
    }

    LinkedList<Integer> secondstoHms(Integer currSecond) {
        Integer hours = currSecond / 3600;
        currSecond = currSecond % 3600;
        Integer minutes = currSecond / 60;
        currSecond = currSecond % 60;
        Integer seconds = currSecond;
        LinkedList<Integer> answer = new LinkedList<>();
        answer.add(hours);
        answer.add(minutes);
        answer.add(seconds);
        return answer;
    }

    void scrollUp() {
        TranslateTransition trl = new TranslateTransition();
        trl.setDuration(Duration.millis(30));
        trl.setToX(0);
        trl.setToY(-400);
        trl.setNode(menuPane);
        TranslateTransition tr2 = new TranslateTransition();
        tr2.setDuration(Duration.millis(30));
        tr2.setFromX(0);
        tr2.setFromY(400);
        tr2.setToX(0);
        tr2.setToY(0);
        tr2.setNode(timerPane);
        ParallelTransition pt = new ParallelTransition(trl, tr2);
        pt.setOnFinished(e -> {
            try {
                System.out.println("Start countdown");
                startCountdown();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
        pt.play();
    }

    void scrollDown() {
        TranslateTransition trl = new TranslateTransition();
        trl.setDuration(Duration.millis(30));
        trl.setToX(0);
        trl.setToY(400);
        trl.setNode(timerPane);
        TranslateTransition tr2 = new TranslateTransition();
        tr2.setDuration(Duration.millis(30));
        tr2.setFromX(0);
        tr2.setFromY(-400);
        tr2.setToX(0);
        tr2.setToY(0);
        tr2.setNode(menuPane);
        ParallelTransition pt = new ParallelTransition(trl, tr2);
        pt.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Integer> hoursList = FXCollections.observableArrayList();
        ObservableList<Integer> minutesAndSecondsList = FXCollections.observableArrayList();
        for (int i = 0; i <= 24; i++) {
            hoursList.add(i);
            minutesAndSecondsList.add(i);
        }

        if (hoursInput != null && minutesInput != null && secondsInput != null) {
            hoursInput.setItems(hoursList);
            hoursInput.setValue(0);

            minutesInput.setItems(minutesAndSecondsList);
            minutesInput.setValue(0);

            secondsInput.setItems(minutesAndSecondsList);
            secondsInput.setValue(0);
        }

        numberMap = new TreeMap<>();
        for (Integer i = 0; i <= 60; i++) {
            if (i < 10) {
                numberMap.put(i, "0" + i.toString());
            } else {
                numberMap.put(i, i.toString());
            }
        }

        setRealTimeUpdates();  // Add this line to initialize the real-time updates
    }

// end timer
    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    public void setRealTimeUpdates() {
        Thread updateTimeThread = new Thread(() -> {
            try {
                while (true) {
                    String currentTime = getCurrentTime();
                    String currentDate = getCurrentDate();

                    javafx.application.Platform.runLater(() -> {
                        timeLabel.setText(currentTime);
                        dateLabel.setText(currentDate);
                    });

                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(new Date());
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(new Date());
    }
    

    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/onboardingya.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSplashScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/SplashScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCalendar(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/Menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTimer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/menuTimer.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSignIn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/signin3.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    public void switchToMenuFolder (ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/application/menuAdd.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
      }

    public void switchToScreenTime(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/menuBar.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            MainPanelController controller = loader.getController();
            
            // Add any additional setup or method calls on the controller if needed

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception: log it, show an alert, etc.
        }
    }

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField taInformation;

    private boolean isShowingPassword = false;

    @FXML
    void handleButtonAction(ActionEvent event) {
        if (isShowingPassword) {
            taInformation.clear();
        } else {
            taInformation.setText(pfPassword.getText());
        }
        isShowingPassword = !isShowingPassword;
    }

    // play music
    @FXML
    private Label chooseMusic;

    private MediaPlayer mediaPlayer;

    @FXML
    void chooseMusic(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select your music");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.ogg");
        chooser.getExtensionFilters().add(extFilter);

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            String selectedFile = file.toURI().toString();
            List<String> videoExtensions = Arrays.asList(".mp4", ".avi", ".mkv");
            boolean isVideo = videoExtensions.stream().anyMatch(ext -> file.getName().toLowerCase().endsWith(ext));

            if (isVideo) {
                Media media = new Media(selectedFile);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> chooseMusic.setText(file.getName() + " (Audio Only)"));
            } else {
                Media media = new Media(selectedFile);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> chooseMusic.setText(file.getName()));
            }
        }
    }

    @FXML
    void pause(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    void play(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    void stop(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    
    
}
