package application;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import date.Date;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SampleController extends Application implements Initializable{
	
	
	@FXML
	private ChoiceBox<String> selectChoiceBox;
	@FXML
	private Pane openAccountPane;
	@FXML
	private Button selectButton;
	private String accountType;
	private String name;
	private Date dateOfBirth;
	private Double String;
	private String[] options = {"Open Account", "Close Account", "Deposit Money", "Withdraw Money",
			"Display Database", "Display Database by account type", "Display accounts with fees and montly interst", "Quit"};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		selectChoiceBox.getItems().addAll(options);	
	}
	
	public void showBoxes(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		System.out.println(choice);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			GridPane root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,400,400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
		
	
	
}

