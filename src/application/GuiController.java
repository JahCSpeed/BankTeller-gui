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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 The GuiController contains all the main functionalities for the Bank Teller user interface and can process actions and show errors.
 It has a three step procedure for the User Interface, which are:
 	1. Select the action.
 	2. Fill in the parameters for the holder and account type.
 	3. State the amount of money to deposit in or withdraw out of the account.
 Note not all steps are executed based on the action selected. Print commands, for instance, only do Step 1.
 @author Jah C. Speed, Abe Vitangcol
 */
public class GuiController extends Application implements Initializable{
	@FXML
	private Stage primaryStage;
	@FXML
	private GridPane mainPane;
	@FXML
	private ChoiceBox<String> selectChoiceBox;
	@FXML
	private Pane actionPane,basicInfoPane , moreInfoPane;
	@FXML
	private Label validOrCampusCodeLabel,moneyLabel;
	@FXML
	private Button selectButton, moreInfoButton, finalButton, moreInfoBackButton, actionPaneBackButton;
	@FXML
	private RadioButton checkingButton, collegeCheckingButton, savingsButton, moneyMarketButton;
	@FXML
	private TextField nameField, dobField, balanceField, validORcampusCodeField;
	@FXML
	private TextArea printArea,openAccountPaneError,openAccountPaneError2,goodMessage;
	private String accountType, name;
	private Date dateOfBirth;
	private Profile accountProfile;
	private double initialBalance;
	private int campusCode, isLoyal;
	
	private final String[] commandOptions = {"Open Account", "Close Account", "Deposit Money", "Withdraw Money",
			"Display Database", "Display Database by account type", "Display accounts with fees and monthly interest","Update Balances", "Quit"};
	private final String CHECKING = "C", COLLEGE_CHECKING = "CC", SAVINGS = "S", MONEY_MARKET = "MM";
	private AccountDatabase mainDatabase;
	private static final double MM_BALANCE_THRESHOLD = 2500;
	
	/**
	 Initializes the GUI by setting up the choice box, hiding all unnecessary information panes, and initializes the database.
	 @param arg0 
	        arg1 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.mainDatabase = new AccountDatabase();
		selectChoiceBox.getItems().addAll(commandOptions);
		basicInfoPane.setVisible(false);
		moreInfoPane.setVisible(false);
		selectButton.setVisible(true);
		goodMessage.setStyle("-fx-text-fill: green ;");
		openAccountPaneError.setStyle("-fx-text-fill: red ;");
		openAccountPaneError2.setStyle("-fx-text-fill: red ;");
		ToggleGroup tg = new ToggleGroup();
		this.checkingButton.setToggleGroup(tg);
		this.collegeCheckingButton.setToggleGroup(tg);
		this.savingsButton.setToggleGroup(tg);
		this.moneyMarketButton.setToggleGroup(tg);
	}
	
	/**
	 Does a specific action based on the choice made from the choice box.
	 After successfully doing the said action, returns back to the default user interface.
	 Will not return back to the default interface if an error was encountered.
	 @param event A button click on continue to execute the action.
	 */
	public void doAction(ActionEvent event) {
		Account tempAccount;
		String choice = selectChoiceBox.getValue();
		if(this.checkBalance() == -1 || this.checkcampusCode() == -1 || this.checkLoyal() == -1) {
			return;
		}
		this.openAccountPaneError2.setText("");
		switch (choice) {
			case "Open Account":
				Account temp  = this.openAccountCommand(this.accountType, this.accountProfile, this.initialBalance, this.campusCode,this.isLoyal);
				if(temp == null) {
					return;
				}else {
					this.mainDatabase.open(this.openAccountCommand(this.accountType, this.accountProfile, this.initialBalance, this.campusCode,this.isLoyal));
				}
				break;
			case "Close Account":
				tempAccount = this.closeAccountCommand(this.accountType, this.accountProfile);
				if(tempAccount == null) {
					this.openAccountPaneError.setText("ERROR " +  this.accountProfile.toString() + " " + this.accountType + " is not in the database.");
					return;
				}
				if(!this.mainDatabase.close(tempAccount)) {
					this.openAccountPaneError.setText("ERROR Account is closed already.");
					return;
				}else {
					this.goodMessage.setText("Account closed.");
				}
				break;
			case "Deposit Money":
				tempAccount = this.manipluateMoney(this.accountType, this.accountProfile, this.initialBalance);
				if(tempAccount == null) {
					this.openAccountPaneError2.setText("ERROR Depositing into account.");
					return;
				}
				if(this.mainDatabase.getAccount(tempAccount) == null) {
					this.openAccountPaneError2.setText("ERROR " + tempAccount.getHolder().toString() + " " + tempAccount.getType() + " is not in the database.");
					return;
				}
				if(this.mainDatabase.getAccount(tempAccount).isClosed()) {
					this.openAccountPaneError2.setText("ERROR Can't deposit into closed account.");
					return;
				}
				this.mainDatabase.deposit(tempAccount);
				this.goodMessage.setText("Deposit - balance updated.");
				break;
			case "Withdraw Money":
				tempAccount = this.manipluateMoney(accountType,  this.accountProfile,  this.initialBalance);
				if(tempAccount == null) {
					this.openAccountPaneError2.setText("ERROR withdrawing from account.");
					return;
				}
				if(this.mainDatabase.getAccount(tempAccount) == null) {
					this.openAccountPaneError2.setText("ERROR " + tempAccount.getHolder().toString() + " " + tempAccount.getType() + " is not in the database.");
					return;
				}
				if(this.mainDatabase.getAccount(tempAccount).isClosed()) {
					this.openAccountPaneError2.setText("ERROR Can't withdraw from closed account.");
					return;
				}
				if(this.mainDatabase.withdraw(tempAccount)) {
					this.goodMessage.setText("Withdraw - balance updated.");
					break;
				}else {
					this.openAccountPaneError2.setText("ERROR Withdraw - insufficient fund.");
					return;
				}
		default:
				break;
		}
		resetToDefault();
		
	}
	
	/**
	 Manipulates money from a specific account with a specific profile and account type.
	 @param accountType The type of account to be manipulated from (savings, checking, etc.)
	        profile The identifications of the person using the account (fname, lname, dob)
	        balance The amount of money to to add or subtract from the account.
	 @return null if the balance was invalid or missing in the command or if the account type was invalid.
	 		 The new account created to find and manipulate from otherwise.
	 */
	private Account manipluateMoney(String accountType, Profile profile, double balance) {
		Account tempAccount = null;
		switch(accountType) {
		case "C":
			tempAccount = new Checking(profile,balance);
			break;
		case "CC":
			tempAccount = new CollegeChecking(profile,balance,-1);
			break;
		case "MM":
			tempAccount = new MoneyMarket(profile,balance);
			break;
		case "S":
			tempAccount =  new Savings(profile,balance,false);
			break;
		default:
			break;
		}
		return tempAccount;	
	}
	
	/**
	 Attempts to open an account based on the params given.
	 @param accountType The type of account to be opened.
	        profile The identifications of a person (fname, lname, dob)
	 	    balance The amount to deposit into the new account.
	 		code The campus code for a college checking account, if applicable and necessary.
	 		isLoyal A 0 or 1 value stating if the holder is loyal or not. Savings account only.
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
			this.goodMessage.setText("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null && this.mainDatabase.checkDupChecking(newAcc) != -1) {
				this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.goodMessage.setText("Account reopened.");
				resetToDefault();
				return null;
			}
			this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
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
			this.goodMessage.setText("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null ||this.mainDatabase.checkDupChecking(newAcc) == -1) {
				this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(acct.isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.goodMessage.setText("Account reopened.");
				resetToDefault();
				return null;
			}
			this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
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
			this.goodMessage.setText("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.goodMessage.setText("Account reopened.");
				resetToDefault();
				return null;
			}
			this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
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
			this.goodMessage.setText("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				this.goodMessage.setText("Account reopened.");
				resetToDefault();
				return null;
			}
			this.openAccountPaneError2.setText("ERROR " + profile.toString() + " same account(type) is in the database.");
			return null;
		}		
		
	}
	
	/**
	 Attempts to close an account based on the type to be closed and the person holding the account.
	 @param accountType The type of account to be closed.
	        profile The identifications of a person using the account (fname, lname, dob)
	 @return The account that was closed, null otherwise.
	 */
	private Account closeAccountCommand(String accountType, Profile profile) {
		switch(accountType) {
			case CHECKING:
				return this.closeCheckingAccount(profile);
			case COLLEGE_CHECKING:
				return this.closeCollegeCheckingAccount(profile);
			case MONEY_MARKET:
				return this.closeMoneyMarketAccount(profile);
			case SAVINGS:
				return this.closeSavingsAccount(profile);
			default:
				return null;
				
		}
	}
	/**
	 Closes a checking account given a profile.
	 @param profile The identifications of the person using the account (fname, lname, dob)
	 @return null if the account was not found in the database
	 		 The newly created account to find and close otherwise.
	 */
	private Account closeCheckingAccount(Profile profile) {
		Checking acct = (Checking)(this.mainDatabase.getAccount(new Checking(profile,-1)));
		return acct;
	}
	/**
	 Closes a college checking account given a profile.
	 @param profile The identifications of the person using the account (fname, lname, dob)
	 @return null if the account was not found in the database
	 		 The newly created account to find and close otherwise.
	 */
	private Account closeCollegeCheckingAccount(Profile profile) {
		CollegeChecking acct = (CollegeChecking)(this.mainDatabase.getAccount(new CollegeChecking(profile,-1,-1)));
		return acct;
	}
	/**
	 Closes a money market account given a profile.
	 @param profile The identifications of the person using the account (fname, lname, dob)
	 @return null if the account was not found in the database
	 		 The newly created account to find and close otherwise.
	 */
	private Account closeMoneyMarketAccount(Profile profile) {
		MoneyMarket acct = (MoneyMarket)(this.mainDatabase.getAccount(new MoneyMarket(profile,-1)));
		return acct;
	}
	
	/**
	 Closes a savings account given a profile.
	 @param profile The identifications of the person using the account (fname, lname, dob)
	 @return null if the account was not found in the database
	 		 The newly created account to find and close otherwise.
	 */
	private Account closeSavingsAccount(Profile profile) {
		Savings acct = (Savings)(this.mainDatabase.getAccount(new Savings(profile,-1,false)));
		return acct;
	}
	
	/**
	 Goes back to the default look of the GUI, which is a similar look as when the GUI was first opened.
	 Used when one set of actions is completed. Does not reset the drop down menu of the GUI.
	 */
	private void resetToDefault() {
		clearAllFields();
		hideAllPanes();
		this.actionPane.setDisable(false);
		this.moreInfoPane.setDisable(false);
		this.basicInfoPane.setDisable(false);
		this.moreInfoButton.setVisible(true);
		this.finalButton.setVisible(true);
		this.moreInfoBackButton.setVisible(true);
		this.actionPaneBackButton.setVisible(true);
	}
	
	/**
	 Clears all typable or selectable fields for the next set of actions.
	 Not only are the fields cleared, but the variables are also set back to its original values of -1 or null.
	 */
	private void clearAllFields() {
		this.nameField.clear();
		this.name = "";
		this.checkingButton.setSelected(false);
		this.collegeCheckingButton.setSelected(false);
		this.savingsButton.setSelected(false);
		this.moneyMarketButton.setSelected(false);
		this.accountType = "";
		this.dobField.clear();
		this.dateOfBirth = null;
		this.balanceField.clear();
		this.initialBalance = -1;
		this.validORcampusCodeField.clear();
		this.isLoyal = -1;
		this.campusCode = -1;
	}
	
	/**
	 Hides the panes for profile and account information as well as amount to put into the account.
	 Used after completing an action to return to the action selection step of the GUI.
	 */
	private void hideAllPanes() {
		this.moreInfoPane.setVisible(false);
		this.basicInfoPane.setVisible(false);
	}
	
	/**
	 Checks if the balance being deposited into an account is valid.
	 An invalid amount is a 0 or negative amount, too little placed in a MoneyMarket account, or simply not a number.
	 @return -1 if an invalid deposit amount was encountered (see above line), 0 otherwise.
	 */
	private int checkBalance() {
		String choice = selectChoiceBox.getValue();
		if(!this.moreInfoPane.isVisible()) {
			return 0;
		}
		try {
			this.initialBalance = Double.parseDouble(this.balanceField.getText());
			if(this.initialBalance <= 0) {
				if(choice.equals("Open Account"))
					this.openAccountPaneError2.setText("Initial deposit cannot be 0 or negative.");
				else
					this.openAccountPaneError2.setText("Deposit - amount cannot be 0 or negative.");
				return -1;
			}
			if(this.accountType.equals(MONEY_MARKET)) {
				if(this.initialBalance < MM_BALANCE_THRESHOLD) {
					this.openAccountPaneError2.setText("Minimum of $" + MM_BALANCE_THRESHOLD + " to open a MoneyMarket account.");
					return -1;
				}
			}
		}catch(NumberFormatException e) {
			if(choice.equals("Open Account"))
				this.openAccountPaneError2.setText("ERROR: Initial deposit entry is not a valid amount.");
			else
				this.openAccountPaneError2.setText("ERROR: Deposit entry is not a valid amount.");
			return -1;
		}
		
		return 0;
	}
	
	/**
	 Checks to see if the loyal input boxes are visible and for the right action.
	 Also capable of catching number format errors for incorrect campus codes.
	 @return -1 if an invalid campus code was encountered, 0 otherwise.
	 */
	private int checkcampusCode() {
		if(!this.moreInfoPane.isVisible() || !this.validORcampusCodeField.isVisible()) {
			return 0;
		}
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
	
	/**
	 Checks to see if the loyal input boxes are visible and for the right action.
	 Also capable of catching number format errors for incorrect loyalty codes.
	 @return -1 if an invalid loyalty code was encountered, 0 otherwise.
	 */
	private int checkLoyal() {
		if(!this.moreInfoPane.isVisible() || !this.validORcampusCodeField.isVisible()) {
			return 0;
		}
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
	
	/**
	 Makes more fields visible based on the action done.
	 Any non-print command will go onto step 2 of the GUI and see the fields to input
	 their name, account type, and their date of birth.
	 @param event A button click on Select after selecting which action to do.
	 */
	public void getInfo(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		this.goodMessage.setText("");
		goodMessage.setStyle("-fx-text-fill: green ;");
		if(choice == null) {
			goodMessage.setStyle("-fx-text-fill: orange ;");
			this.goodMessage.setText("Please Select an Action");
			return;
		}
		switch (choice) {
			case "Open Account":
				this.actionPane.setDisable(true);
				this.basicInfoPane.setVisible(true);
				break;
			case "Close Account":
				this.actionPane.setDisable(true);
				this.basicInfoPane.setVisible(true);
				break;
			case "Deposit Money":
				this.actionPane.setDisable(true);
				this.basicInfoPane.setVisible(true);
				break;
			case "Withdraw Money":
				this.actionPane.setDisable(true);
				this.basicInfoPane.setVisible(true);
				break;
			case "Display Database":
				this.printArea.clear();
				this.printArea.setText(this.mainDatabase.print());
				break;
			case "Display Database by account type":
				this.printArea.clear();
				this.printArea.setText(this.mainDatabase.printByAccountType());
				break;
			case "Display accounts with fees and montly interst":
				this.printArea.clear();
				this.printArea.setText(this.mainDatabase.printFeeAndInterest());
				break;
			case "Update Balances":
				this.printArea.clear();
				this.printArea.setText(this.mainDatabase.updateDatabase());
				break;
			case "Quit":
				this.quit();
				break;
			default:
				
				break;
		}
		
	}
	
	/**
	 Sets the account type based on the button selected.
	 @param event One of the four buttons clicked on to select.
	 */
	public void selectedAccountType(ActionEvent event) {

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
	
	/**
	 Decides whether more information is needed based on the option selected.
	 Opening is one such action that requires more information. Any action requiring the
	 balance text field will be one that needs more information.
	 @param event A button click on the continue button.
	 */
	public void moreInfoNeeded(ActionEvent event) {
		String choice = selectChoiceBox.getValue();
		if(populateProfile() == 0 ) {
			if(choice.equals("Close Account") || choice.equals("Display Database") || choice.equals("Display Database by account type") 
					|| choice.equals("Display accounts with fees and montly interst")) {
				doAction(event);
				return;
			}
			this.setMoreInfoVisable();
			this.moreInfoButton.setVisible(false);
			this.actionPaneBackButton.setVisible(false);
			this.basicInfoPane.setDisable(true);
			
		}
	}
	
	/**
	 Goes back one step in terms of visuals, updating the user interface when the back button was clicked.
	 @param event A button click on the back button.
	 */
	public void backButton(ActionEvent event) {
		//Re-enables the first info pane
		this.moreInfoButton.setVisible(true);
		this.actionPaneBackButton.setVisible(true);
		this.basicInfoPane.setDisable(false);
		
		
		//Hides the More Info Pane
		this.moreInfoPane.setVisible(false);
		this.openAccountPaneError2.setText("");
	}
	
	/**
	 Goes back one step in terms of the actions able to do upon a button click.
	 @param event A button click on the back button.
	 */
	public void actionPaneBackButton(ActionEvent event) {
		//Re-enables the first info pane
		this.actionPane.setDisable(false);
		
		//Hides the Basic Info Pane
		this.basicInfoPane.setVisible(false);
		this.openAccountPaneError.setText("");
	}
	
	/**
	 Stops running the GUI upon hitting the quit option in the main window.
	 */
	public void quit() {
		this.primaryStage = (Stage)this.mainPane.getScene().getWindow();
		this.primaryStage.close();
	}
	
	/**
	 Creates a profile given the name and parameters given in the GUI.
	 Capable of catching errors, such as missing name, missing account type, or invalid birthday.
	 @return -1 if an invalid name is given, if an account type wasn't selected, or if the date of birth
	 		    is invalid along with an appropriate error message.
	 		 0 otherwise.
	 */
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
			this.openAccountPaneError.setText("Invalid Name. Must Be in format \"FirstName LastName\".");
			return -1;
		}
		if(!isButtonSelected()) {
			this.openAccountPaneError.setText("Must select account type.");
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
	
	/**
	 Checks to see if an account type button was selected or not.
	 Only when applicable, so for opening, closing, withdrawing, or depositing for the account.
	 @return false if a button was not selected, true otherwise.
	 */
	private boolean isButtonSelected() {
		if(!this.checkingButton.isSelected() && !this.collegeCheckingButton.isSelected() && !this.savingsButton.isSelected() && !this.moneyMarketButton.isSelected() ) {
			return false;
		}
	    return true;
	}
	
	/**
	 Lets specific fields become visible to be entered to create an account.
	 Deals with Campus Code and Loyal Customer values for college checking accounts and savings
	 accounts, respectively, and hides these fields to make sure that information cannot be filled
	 upon opening a regular account.
	 */
	private void setMoreInfoVisable() {
		
		String choice = selectChoiceBox.getValue();
		if(this.accountType.equals(COLLEGE_CHECKING) && choice.equals("Open Account")) {
			this.validOrCampusCodeLabel.setText("Campus Code");
			this.moneyLabel.setText("Initial Balance");
			this.validORcampusCodeField.setPromptText("Ex: \"0\", \"1\", or \"2\"");
			this.validOrCampusCodeLabel.setVisible(true);
			this.validORcampusCodeField.setVisible(true);
		}else if(this.accountType.equals(SAVINGS) && choice.equals("Open Account")) {
			this.validOrCampusCodeLabel.setText("Loyal Customer");
			this.moneyLabel.setText("Initial Balance");
			this.validORcampusCodeField.setPromptText("Ex: \"0\", or \"1\"");
			this.validOrCampusCodeLabel.setVisible(true);
			this.validORcampusCodeField.setVisible(true);
		}else {
			if(!choice.equals("Open Account"))
				this.moneyLabel.setText("Amount");
			this.validOrCampusCodeLabel.setVisible(false);
			this.validORcampusCodeField.setVisible(false);
		}
		
		this.moreInfoPane.setVisible(true);
		
	}
	
	/**
	 Starts and creates the GUI for the Bank Teller, giving it a window area and a title.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
			this.mainPane = FXMLLoader.load(getClass().getResource("guiFormat.fxml"));
			Scene scene = new Scene(mainPane,985,690);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Bank Teller");
			this.primaryStage.show();
			
		} catch(Exception e) {}
	}
	
		
	
	
}

