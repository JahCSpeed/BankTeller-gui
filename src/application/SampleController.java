package application;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import date.Date;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

public class SampleController implements Initializable{
	
	
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
	
}

