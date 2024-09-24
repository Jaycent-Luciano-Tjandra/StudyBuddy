package application;


import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class CalendarApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the initial scene (login.fxml)
            Parent root = FXMLLoader.load(getClass().getResource("onboarding.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

            // Fade in animation for the login scene
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            // Delay and then close the login scene
            fadeIn.setOnFinished(event -> {
                primaryStage.close();
                // Load the main application scene
                loadMainApplication(primaryStage);
            });

            // Pause before transitioning to the next scene
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> {
                primaryStage.close();
                // Load the main application scene
                loadMainApplication(primaryStage);
            });
            pause.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMainApplication(Stage primaryStage) {
        try {
            // Load the main application scene (onboarding.fxml)
            Parent root = FXMLLoader.load(getClass().getResource("onboarding.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
