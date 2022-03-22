package application;

import java.net.URL;
import java.util.ResourceBundle;

import accountType.Checking;
import accountType.CollegeChecking;
import accountType.MoneyMarket;
import accountType.Savings;
import accounts.Account;
import accounts.AccountDatabase;
import accounts.Profile;
import date.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class SampleController extends Application implements Initializable{
	@FXML
	private Stage primaryStage;
	@FXML
	private GridPane mainPane;
	@FXML
	private ChoiceBox<String> selectChoiceBox;
	@FXML
	private Pane actionPane,basicInfoPane , moreInfoPane;
	@FXML
	private Label openAccountPaneError,openAccountPaneError2, validOrCampusCodeLabel;
	@FXML
	private Button selectButton, moreInfoButton, moreInfoBackButton, actionPaneBackButton;
	@FXML
	private RadioButton checkingButton, collegeCheckingButton, savingsButton, moneyMarketButton;
	@FXML
	private TextField nameField, dobField, balanceField, validORcampusCodeField;
	
	private String accountType;
	private String name;
	private Date dateOfBirth;
	private Profile accountProfile;
	private double initialBalance;
	private int campusCode;
	private int isLoyal;
	private final String[] commandOptions = {"Open Account", "Close Account", "Deposit Money", "Withdraw Money",
			"Display Database", "Display Database by account type", "Display accounts with fees and montly interst", "Quit"};
	private final String CHECKING = "C";
	private final String COLLEGE_CHECKING = "CC";
	private final String SAVINGS = "S";
	private final String MONEY_MARKET = "MM";
	private AccountDatabase mainDatabase;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.mainDatabase = new AccountDatabase();
		selectChoiceBox.getItems().addAll(commandOptions);
		basicInfoPane.setVisible(false);
		moreInfoPane.setVisible(false);
		this.selectButton = new Button();
		selectButton.setVisible(true);
		ToggleGroup tg = new ToggleGroup();
		this.checkingButton.setToggleGroup(tg);
		this.collegeCheckingButton.setToggleGroup(tg);
		this.savingsButton.setToggleGroup(tg);
		this.moneyMarketButton.setToggleGroup(tg);
	}
	public void doAction(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		if(this.checkBalance() == -1 || this.checkcampusCode() == -1 || this.checkLoyal() == -1) {
			return;
		}
		this.openAccountPaneError2.setText("");
		switch (choice) {
			case "Open Account":
				System.out.println("Time to Open Account!");
				this.mainDatabase.open(this.openAccountCommand(this.accountType, this.accountProfile, this.initialBalance, this.campusCode,this.isLoyal));
				this.mainDatabase.print();
				break;
			case "Close Account":
				System.out.println("Time to Close Account!");
				break;
			case "Deposit Money":
				System.out.println("Time to Deposit Money!");
				break;
			case "Withdraw Money":
				System.out.println("Time to Withdrawl Money!");
				break;
			case "Quit":
				this.quit();
				break;
			default:
				break;
		}
	}
	
	/**
	 Attempts to open an account based on the params given.
	 @param accountType
	        profile The identifications of a person (fname, lname, dob)
	 	    balance The amount to deposit into the new account.
	 		code The campus code for a college checking account, if applicable and necessary.
	 		paramSize The number of parameters in the given line.
	 @return The account created based on the accountType given if there are no problems.
	 		 null otherwise (a problem was encountered in making the account)
	 */
	private Account openAccountCommand(String accountType, Profile profile, double balance, int code,int isLoyal) {
		switch(accountType) {
			case CHECKING:
				return this.checkingAccount(profile, balance);
			case COLLEGE_CHECKING:
				return this.collageCheckingAccount(profile, balance,code);
			case MONEY_MARKET:
				return this.moneyMarketAccount(profile, balance);
			case SAVINGS:
				return this.savingsAccount(profile, balance, isLoyal);
			default:
				return null;
				
		}
		
	}
	/**
	 Opens a checking account given a starting balance and a profile of the person using it.
	 @param profile The identifications of a person (fname, lname, dob)
	 	 	balance The starting amount of money to open the account.
	 @return null if an invalid deposit was made, a duplicate account is in the database, 
	 		      or the account was reopened.
	 		 The newly created account, newAcc, otherwise.
	 */
	private Account checkingAccount(Profile profile, double balance) {
		Checking newAcc = new Checking(profile,balance);
		if(this.mainDatabase.getAccount(newAcc) == null && this.mainDatabase.checkDupChecking(newAcc) == -1) {
			this.openAccountPaneError2.setTextFill(Color.GREEN);
			this.openAccountPaneError2.setText("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null && this.mainDatabase.checkDupChecking(newAcc) != -1) {
				this.openAccountPaneError2.setTextFill(Color.RED);
				this.openAccountPaneError2.setText(profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.openAccountPaneError2.setTextFill(Color.GREEN);
				this.openAccountPaneError2.setText("Account reopened.");
				return null;
			}
			return null;
			
		}
	
	}

	/**
	 Opens a college checking account given a profile, starting balance, and a campus code.
	 @param profile The identifications of a person using the account (fname, lname, dob)
	        balance The starting amount of money to open the account.
	        code The campus code required to open the account. Cannot open the account with a wrong code.
	 @return null if an invalid deposit was made, an invalid campus code was inputed,
	 		 a duplicate account was found in the database, or the account was reopened.
	 		 The newly created account, newAcc, otherwise.
	 */
	private Account collageCheckingAccount(Profile profile, double balance, int code) {
		CollegeChecking newAcc = new CollegeChecking(profile,balance,code);
		if(this.mainDatabase.checkDupChecking(newAcc) == -1) {
			this.openAccountPaneError2.setTextFill(Color.GREEN);
			this.openAccountPaneError2.setText("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null ||this.mainDatabase.checkDupChecking(newAcc) == -1) {
				this.openAccountPaneError2.setTextFill(Color.RED);
				this.openAccountPaneError2.setText(profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(acct.isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.openAccountPaneError2.setTextFill(Color.GREEN);
				this.openAccountPaneError2.setText("Account reopened.");
				return null;
			}
			this.openAccountPaneError2.setTextFill(Color.RED);
			this.openAccountPaneError2.setText(profile.toString() + " same account(type) is in the database.");
			return null;
		}
	}
	
	/**
	 Opens a savings account given a profile, starting balance, and loyalty code.
	 @param profile The identifications of a person using the account (fname, lname, dob)
	        balance The starting amount of money to put into the account.
	        code A number that is either 0 or 1. States if the holder is loyal or not.
	 @return null if an invalid deposit was made, an invalid loyalty code was inputed,
	 		 a duplicate account was found in the database, or the account was reopened.
	 		 The newly created account, newAcc, otherwise.
	 */
	private Account savingsAccount(Profile profile, double balance, int code) {
		Savings newAcc = new Savings(profile,balance,(code == 1?true:false));
		if(this.mainDatabase.getAccount(newAcc) == null) {
			this.openAccountPaneError2.setTextFill(Color.GREEN);
			this.openAccountPaneError2.setText("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.openAccountPaneError2.setTextFill(Color.GREEN);
				this.openAccountPaneError2.setText("Account reopened.");
				return null;
			}
			this.openAccountPaneError2.setTextFill(Color.RED);
			this.openAccountPaneError2.setText(profile.toString() + " same account(type) is in the database.");
			return null;
		}		
	}
	
	/**
	 Opens a money market account given a profile and a starting balance.
	 @param profile The identifications of a person using the account (fname, lname, dob)
	        balance The starting amount of money to put into the account.
	 @return null if an invalid deposit was made, the balance was not at least $2500 to open,
	 		 a duplicate account was found in the database, or the account was reopened.
	 		 The newly created account, newAcc, otherwise.
	 */
	private Account moneyMarketAccount(Profile profile, double balance) {
		MoneyMarket newAcc = new MoneyMarket(profile,balance);
		if(this.mainDatabase.getAccount(newAcc) == null) {
			this.openAccountPaneError2.setTextFill(Color.GREEN);
			this.openAccountPaneError2.setText("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.openAccountPaneError2.setTextFill(Color.GREEN);
				this.openAccountPaneError2.setText("Account reopened.");
				return null;
			}
			this.openAccountPaneError2.setTextFill(Color.RED);
			this.openAccountPaneError2.setText(profile.toString() + " same account(type) is in the database.");
			return null;
		}		
		
	}
	
	
	
	
	
	private int checkBalance() {
		try {
			this.initialBalance = Double.parseDouble(this.balanceField.getText());
			if(this.initialBalance <= 0) {
				this.openAccountPaneError2.setText("Initial deposit cannot be 0 or negative.");
				return -1;
			}
			if(this.accountType.equals(MONEY_MARKET)) {
				if(this.initialBalance < 2500) {
					this.openAccountPaneError2.setText("Minimum of $2500 to open a MoneyMarket account.");
					return -1;
				}
			}
		}catch(NumberFormatException e) {
			this.openAccountPaneError2.setText("ERROR: Initial deposit entry is not a valid amount.");
			return -1;
		}
		
		return 0;
	}
	private int checkcampusCode() {
		if(!this.accountType.equals(COLLEGE_CHECKING)) {
			return 0;
		}
		try {
			this.campusCode = Integer.parseInt(this.validORcampusCodeField.getText());
			if(this.campusCode > 2 || this.campusCode < 0) {
				this.openAccountPaneError2.setText("Invalid campus code.");
				return -1;
			}
		}catch(NumberFormatException e) {
			this.openAccountPaneError2.setText("Invalid campus code.");
			return -1;
		}
		return 0;
	}
	private int checkLoyal() {
		if(!this.accountType.equals(SAVINGS)) {
			return 0;
		}
		try {
			this.isLoyal = Integer.parseInt(this.validORcampusCodeField.getText());
			if(this.isLoyal > 1 || this.isLoyal < 0) {
				this.openAccountPaneError2.setText("Invalid loyalty code.");
				return -1;
			}
		}catch(NumberFormatException e) {
			this.openAccountPaneError2.setText("Invalid loyalty code.");
			return -1;
		}
		return 0;
	}
	public void getInfo(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		switch (choice) {
			case "Open Account":
				basicInfoPane.setVisible(true);
				
				System.out.println(choice);
				break;
			case "Quit":
				this.quit();
				break;
			default:
				break;
		}
		
	}
	public void selectedAccountType(ActionEvent event) {
		System.out.println("New Account Selected");
		if(this.checkingButton.isSelected()) {
			this.accountType = CHECKING;
		}else if(this.collegeCheckingButton.isSelected()) {
			this.accountType = COLLEGE_CHECKING;
		}else if(this.savingsButton.isSelected()) {
			this.accountType = SAVINGS;
		}else if(this.moneyMarketButton.isSelected()) {
			this.accountType = MONEY_MARKET;
		}
	}
	public void moreInfoNeeded(ActionEvent event) {
		if(populateProfile() == 0) {
			this.setMoreInfoVisable();
			this.moreInfoButton.setVisible(false);
			this.basicInfoPane.setDisable(true);
		}
	}
	public void backButton(ActionEvent event) {
		//Re-enables the first info pane
		this.moreInfoButton.setVisible(true);
		this.basicInfoPane.setDisable(false);
		
		//Hides the More Info Pane
		this.moreInfoPane.setVisible(false);
	}
	public void actionPaneBackButton(ActionEvent event) {
		//Re-enables the first info pane
		this.actionPane.setDisable(false);
		
		//Hides the Basic Info Pane
		this.basicInfoPane.setVisible(false);
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
		if(!isButtonSelected()) {
			this.openAccountPaneError.setText("Must select account type");
			return -1;
		}
		this.dateOfBirth = new Date(this.dobField.getText());
		this.accountProfile = new Profile(fname,lname,this.dateOfBirth);
		if(!this.accountProfile.getDOB().isValid() || this.accountProfile.getDOB().compareTo(new Date()) > 0 || this.accountProfile.getDOB().compareTo(new Date())  == 0) {
			this.openAccountPaneError.setText("Date of birth invalid.");
			return -1;
		}
		this.openAccountPaneError.setText("");
		return 0;
	}
	private boolean isButtonSelected() {
		if(!this.checkingButton.isSelected() && !this.collegeCheckingButton.isSelected() && !this.savingsButton.isSelected() && !this.moneyMarketButton.isSelected() ) {
			return false;
		}
	    return true;
	}
	private void setMoreInfoVisable() {
		System.out.println("Account Type: " + this.accountType);
		
		if(this.accountType.equals(COLLEGE_CHECKING)) {
			this.validOrCampusCodeLabel.setText("Campus Code");
			this.validORcampusCodeField.setPromptText("Ex: \"1\", \"2\", or \"3\"");
			this.validOrCampusCodeLabel.setVisible(true);
			this.validORcampusCodeField.setVisible(true);
		}else if(this.accountType.equals(SAVINGS)) {
			this.validOrCampusCodeLabel.setText("Loyal Customer");
			this.validORcampusCodeField.setPromptText("Ex: \"0\", or \"1\"");
			this.validOrCampusCodeLabel.setVisible(true);
			this.validORcampusCodeField.setVisible(true);
		}else {
			this.validOrCampusCodeLabel.setVisible(false);
			this.validORcampusCodeField.setVisible(false);
		}
		this.moreInfoPane.setVisible(true);
		
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
	public static void main(String[] args) {
		launch(args);
	}
		
	
	
}

