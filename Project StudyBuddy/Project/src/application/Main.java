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


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		//fade in ke splash
		FadeTransition fadeIn = new FadeTransition(Duration.millis(1000),root);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.play();
		//delay, close splash
		fadeIn.setOnFinished(event-> {primaryStage.close(); loadMainApplication(primaryStage);});
		  PauseTransition pause = new PauseTransition(Duration.seconds(6));
          pause.play();
          
          String css = this.getClass().getResource("application.css").toExternalForm();

			Scene scene1 = new Scene(FXMLLoader.load(getClass().getResource("onboarding.fxml")));
			
			 scene1.getStylesheets().add(css);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadMainApplication(Stage primaryStage) {
		try {
			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("onboarding.fxml"));
			Parent root = mainLoader.load();
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
