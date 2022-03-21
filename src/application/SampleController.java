package application;

import java.net.URL;
import java.util.ResourceBundle;

import accounts.Profile;
import date.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SampleController extends Application implements Initializable{
	
	private Stage primaryStage;
	@FXML
	private GridPane mainPane;
	@FXML
	private ChoiceBox<String> selectChoiceBox;
	@FXML
	private Pane openAccountPane;
	@FXML
	private Label openAccountPaneError;
	@FXML
	private Pane moreInfoPane;
	@FXML
	private Button selectButton;
	@FXML
	private Button moreInfoButton;
	@FXML
	private TextField nameField;
	@FXML
	private TextField accountField;
	@FXML
	private TextField dobField;
	@FXML
	private TextField balanceField;
	@FXML
	private TextField campusCodeField;
	
	private String accountType;
	private String name;
	private Date dateOfBirth;
	private Double String;
	private Profile accountProfile;
	private final String[] commandOptions = {"Open Account", "Close Account", "Deposit Money", "Withdraw Money",
			"Display Database", "Display Database by account type", "Display accounts with fees and montly interst", "Quit"};
	private final String[] accountTypes = {"college", "c", "college checking", "cc", "savings", "s", "money market", "mm"};
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		selectChoiceBox.getItems().addAll(commandOptions);
		openAccountPane.setVisible(false);
		moreInfoPane.setVisible(false);
		this.selectButton = new Button();
		selectButton.setVisible(true);
	}
	
	public void doAction(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		switch (choice) {
			case "Open Account":
				openAccountPane.setVisible(true);
				System.out.println(choice);
				break;
			case "Quit":
				this.quit();
				break;
			default:
				break;
		}
		
	}
	public void moreInfoNeeded(ActionEvent event) {
		if(populateProfile() == 0) {
			this.moreInfoPane.setVisible(true);
			this.openAccountPane.setDisable(true);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
			this.mainPane = FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(mainPane,980,650);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void quit() {
		this.primaryStage = (Stage)this.mainPane.getScene().getWindow();
		this.primaryStage.close();
	}
	private int populateProfile() {
		this.name = this.nameField.getText();
		String fname, lname;
		try {
			String[] tokens = this.name.split("\\s+");
			fname = tokens[0];
			lname = tokens[1];
		}catch(IndexOutOfBoundsException e){
			fname = null;
			lname = null;
			this.openAccountPaneError.setText("Invalid Name. Must Be in format \"FirstName LastName\"");
			return -1;
		}
		this.accountType = this.accountField.getText();
		if(searchLoop(this.accountTypes,accountType.toLowerCase())  || this.accountField.getText().isEmpty()) {
			this.openAccountPaneError.setText("Invalid Account Type. Must Be in format \"College\" or \"C\"");
			return -1;
		}
		this.dateOfBirth = new Date(this.dobField.getText());
		this.accountProfile = new Profile(fname,lname,this.dateOfBirth);
		if(!this.accountProfile.getDOB().isValid() || this.accountProfile.getDOB().compareTo(new Date()) > 0 || this.accountProfile.getDOB().compareTo(new Date())  == 0) {
			this.openAccountPaneError.setText("Date of birth invalid.");
			return -1;
		}
		return 0;
	}
	private boolean searchLoop(String[] arrayString, String searchString) {
		if(searchString.equals("")) {
			return false;
		}
	    for (String string : arrayString) {
	        if (string.equals(searchString))
	        	return true;
	    }
	    
	    return false;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
		
	
	
}

