package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TableController {
	

	 @FXML
	 private Button addButton;
	@FXML
	public Label usernameLabel;
	public ObservableList<EventData> dataEvent;
	public User currentUser;
	public Stage popupStage;
	
	
	
	 public void setCurrentUser(User currentUser) {
	        this.currentUser = currentUser;
	    }
	 
	 public static String jdbcUrl = "jdbc:mysql://localhost:3306/loginStudyBuddy";
		public static String dbUser = "root";
		public static String dbPassword = "";

		@SuppressWarnings("exports")
		public static Connection connect() throws SQLException {
			return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
		}


		 @FXML
	    public TableView<EventData> eventTable;
	    @SuppressWarnings("exports")
		@FXML
	    public TableColumn<EventData, String> eventDate;
	    @FXML
	    public TableColumn<EventData, String> eventDesc;
	    
	    

	public Button deleteEvent;
	

	@FXML
	public void initialize() {
		
		
		if (currentUser == null) {
            return;
        }
		eventDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		eventDesc.setCellValueFactory(new PropertyValueFactory<>("description"));


		dataEvent = FXCollections.observableArrayList();
		eventTable.setItems(dataEvent);

		
		showPemasukanData();
		deleteEvent();
	}
	
	@FXML
	private void handleAddEvent(ActionEvent event) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarFXML.fxml"));
	        Parent root = loader.load();

	        // Create a new stage for the popup
	        popupStage = new Stage();
	        popupStage.setTitle("Event Calendar");
	        popupStage.initModality(Modality.APPLICATION_MODAL);
	        popupStage.setScene(new Scene(root));

	        // Set the current user in the CalendarController
	        CalendarController calendarController = loader.getController();
	        calendarController.setCurrentUser(currentUser);

	        // Show the popup
	        popupStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public void showPemasukanData() {
		getPemasukanData();
		eventTable.setItems(dataEvent);

	}

	private void getPemasukanData() {

        
 
        String url = "jdbc:mysql://localhost:3306/loginStudyBuddy";
		String username = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url)) {
			String query = "SELECT * FROM events WHERE user_id = " + "'"+ currentUser.getUserId()+"'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String tanggal = resultSet.getString("event_date");
				String desc = resultSet.getString("event_description");
				Integer id = resultSet.getInt("event_id");
				
				EventData event = new EventData(tanggal, desc, id); 
				dataEvent.add(event);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	

	public void deleteEvent() {
		EventData selectedPemasukan = eventTable.getSelectionModel().getSelectedItem();

		if (selectedPemasukan != null) {
			hapusPemasukan(selectedPemasukan.getEventId());
			eventTable.getItems().remove(selectedPemasukan);
			//showSuccessAlert("Delete data pemasukan berhasil");
			eventTable.refresh();
		}

	}
	
	public static void hapusPemasukan(Integer idpemasukan) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = connect();
			String sql = "DELETE FROM events WHERE event_id = ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, idpemasukan);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
    public void refreshTableView() {
        if (currentUser == null) {
            return;
        }

        // Clear existing data and fetch new data from the database
        dataEvent.clear();
        showPemasukanData();
        // Refresh the TableView
        eventTable.refresh();
    }
	
	   @FXML
	    private Label label;

	    private Stage stage;
	    private Scene scene;
	    private Parent root;

    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/onboarding.fxml"));
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

}
