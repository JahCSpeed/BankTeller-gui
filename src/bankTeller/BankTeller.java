package bankTeller;

import java.util.Scanner;

import accountType.Checking;
import accountType.CollegeChecking;
import accountType.MoneyMarket;
import accountType.Savings;
import accounts.Account;
import accounts.AccountDatabase;
import accounts.Profile;
import date.Date;

/**
 The BankTeller class is the user interface class to take in inputs and output the results.
 Wherever there are invalid or errors in the inputs, it is capable of displaying an error
 message and state the error. Also able to do various exception handling.
 @author Jah C Speed, Abe Vitangcol
 */
public class BankTeller {
	private boolean isRunning;
	private Scanner scanInput;
	private AccountDatabase mainDatabase;
	
	/**
	 Constructs and initializes the variables to enable the BankTeller to run.
	 */
	public BankTeller() {
		this.isRunning = false;
		this.scanInput = new Scanner(System.in);
		this.mainDatabase = new AccountDatabase();
	}
	
	/**
	 Runs the Bank Teller to continuously allow inputs until it has been stopped.
	 Takes batch commands or single commands.
	 */
	public void run() {
		//Runs the Bank Teller
		System.out.println("Bank Teller is running.\n");
		this.isRunning = !this.isRunning; 
		while(isRunning) {
			captureInput(scanInput);
		}
		scanInput.close();
	}
	
	/**
	 Scans the user input(s) and does command(s) based on the inputs given.
	 Able to capture single or multiple lines of input.
	 @param sc The scanner that scans the user inputs and parses them.
	 */
	private void captureInput(Scanner sc) {
		while(sc.hasNextLine()) {
			String input = sc.nextLine();
			if(input.equals("")) {return;}
			String[] tokens = input.split("\\s+");
			String accountType = null, code = null, amount = null;
			Profile profile = null;
			int intendingCommand = -1;
			if(tokens.length < 0) {
				break;
			}
			intendingCommand = getCommand(tokens[0]);
			if(this.doNoNameCommand(intendingCommand) == 0) {return;}
			int i = 0;
			try {
				intendingCommand = getCommand(tokens[i++]);
				if(this.doNoNameCommand(intendingCommand) == 0) {return;}
				
				accountType = tokens[i++];
			    profile = new Profile(tokens[i++],tokens[i++],new Date(tokens[i++]));
				if(!profile.getDOB().isValid() || profile.getDOB().compareTo(new Date()) > 0 || profile.getDOB().compareTo(new Date())  == 0) {
					System.out.println("Date of birth invalid.");
					return;
				}
				amount = tokens[i++];
				code = tokens[i++];
			}catch(ArrayIndexOutOfBoundsException e) {}
				doCommand(intendingCommand, accountType, profile, amount,code, tokens.length);
			break;
			}
	}
	
	/**
	 Performs a command that requires additional params to execute.
	 Deals with Opening, Closing, Depositing, and Withdrawing from accounts.
	 @param intendingCommand The command to be done after obtaining the id number
	        accountType The type of account to be used (for deposit, withdrawal, opening or closing)
	        profile The identifications of a person (fname, lname, dob).
	        amount The amount to withdraw or deposit in the account.
	        code The campus code for a college account, if applicable.
	        paramSize The amount of parameters a given line has.
	 */
	private void doCommand(int intendingCommand, String accountType, Profile profile, String amount, String code, int paramSize) {
		Account tempAccount;
		switch(intendingCommand) {
		case 1:
			//O command
			this.mainDatabase.open(this.openAccountCommand(accountType, profile, amount, code, paramSize));
			break;
		case 2:
			//C command
			tempAccount = this.closeAccountCommand(accountType, profile, paramSize);
			if(tempAccount == null) {
				return;
			}
			if(this.mainDatabase.getAccount(tempAccount) == null) {
				System.out.println(tempAccount.getHolder().toString() + " " + tempAccount.getType() + " is not in the database.");
			}
			if(!this.mainDatabase.close(tempAccount)) {
				System.out.println("Account is closed already.");
			}else {
				System.out.println("Account closed.");
			}
			break;
		case 3:
			//D command
			tempAccount = this.depositCommand(accountType, profile, amount,paramSize);
			if(tempAccount == null) {
				return;
			}
			if(this.mainDatabase.getAccount(tempAccount) == null) {
				System.out.println(tempAccount.getHolder().toString() + " " + tempAccount.getType() + " is not in the database.");
				return;
			}
			this.mainDatabase.deposit(tempAccount);
			System.out.println("Deposit - balance updated.");
			break;
		case 4:
			//W command
			tempAccount = this.withdrawCommand(accountType, profile, amount,paramSize);
			if(tempAccount == null) {
				return;
			}
			if(this.mainDatabase.getAccount(tempAccount) == null) {
				System.out.println(tempAccount.getHolder().toString() + " " + tempAccount.getType() + " is not in the database.");
				return;
			}
			if(this.mainDatabase.withdraw(tempAccount)) {
				System.out.println("Withdraw - balance updated.");
				return;
			}else {
				System.out.println("Withdraw - insufficient fund.");
				return;
			}
			
		default:
			break;
		}
		
	}

	/**
	 Ends the BankTeller and stops taking in commands.
	 */
	private void endBankTeller() {
		System.out.println("Bank Teller is terminated.");
		this.isRunning = !this.isRunning;
	}
	
	
	/**
	 Performs a command, primarily printing commands, updating commands, or the stop command.
	 None of these commands require further params.
	 @param command The command typed in that wants to be executed.
	 @return 0 if the command has been done successfully or if invalid, -1 otherwise.
	 */
	private int doNoNameCommand(int command) {
		switch(command) {
			case 5:
				this.mainDatabase.print();
				return 0;
			case 6:
				this.mainDatabase.printByAccountType();
				return 0;
			case 7:
				this.mainDatabase.printFeeAndInterest();
				return 0;
			case 8:
				this.mainDatabase.updateDatabase();
				return 0;
			case 9:
				this.endBankTeller();
				return 0;
			case -1:
				System.out.println("Invalid command!");
				return 0;
			default:
				return -1;
		}
	}
	
	/**
	 Gets the id of the command from the string command.
	 @param input The command string inputted into the system
	 @return The proper id of the command, -1 otherwise.
	 */
	private int getCommand(String input) {
		switch(input) {
			case("O"):
				return Commands.O.id;
			case("C"):
				return Commands.C.id;
			case("D"):
				return Commands.D.id;
			case("W"):
				return Commands.W.id;
			case("P"):
				return Commands.P.id;
			case("PT"):
				return Commands.PT.id;
			case("PI"):
				return Commands.PI.id;
			case("UB"):
				return Commands.UB.id;
			case("Q"):
				return Commands.Q.id;
			default:
				break;
		}
		return -1;
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
	private Account openAccountCommand(String accountType, Profile profile, String balance, String code, int paramSize) {
		switch(accountType) {
			case "C":
				if(paramSize != 6) {
					System.out.println("Missing data for opening an account.");
					return null;
				}
				return this.checkingAccount(profile, balance);
			case "CC":
				if(paramSize != 7) {
					System.out.println("Missing data for opening an account.");
					return null;
				}
				return this.collageCheckingAccount(profile, balance,code);
			case "MM":
				if(paramSize != 6) {
					System.out.println("Missing data for opening an account.");
					return null;
				}
				return this.moneyMarketAccount(profile, balance);
			case "S":
				if(paramSize != 7) {
					System.out.println("Missing data for opening an account.");
					return null;
				}
				return this.savingsAccount(profile, balance, code);
			default:
				return null;
				
		}
		
	}
	
	/**
	 Attempts to close an account based on the type to be closed and the person holding the account.
	 @param accountType The type of account to be closed.
	        profile The identifications of a person using the account (fname, lname, dob)
	 	 	paramSize The number of parameters of the given line.
	 @return The account that was closed, null otherwise.
	 */
	private Account closeAccountCommand(String accountType, Profile profile, int paramSize) {
		if(paramSize != 5) {
			System.out.println("Missing data for closing an account.");
			return null;
		}
		switch(accountType) {
			case "C":
				return this.closeCheckingAccount(profile);
			case "CC":
				return this.closeCollegeCheckingAccount(profile);
			case "MM":
				return this.closeMoneyMarketAccount(profile);
			case "S":
				return this.closeSavingsAccount(profile);
			default:
				return null;
				
		}
	}
	
	/**
	 Tries to deposit an amount for a specific account with a specific profile and type of account used.
	 @param accountType The type of account to deposit into (savings, checking, etc.)
	        profile The identifications of a person using the account (fname, lname, dob)
	        balance The amount to deposit into the account.
	        paramSize The number of parameters of the given line.
	 @return null if the balance was invalid or missing in the command or if the account type was invalid.
	 		 The new account created to find and deposit into otherwise.
	 */
	private Account depositCommand(String accountType, Profile profile, String balance,int paramSize) {
		if(paramSize != 6) {
			System.out.println("Missing data for depositing into account.");
			return null;
		}
		double bal;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Deposit - amount cannot be 0 or negative.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		Account tempAccount = null;
		switch(accountType) {
		case "C":
			tempAccount = new Checking(profile,bal);
			break;
		case "CC":
			tempAccount = new CollegeChecking(profile,bal,-1);
			break;
		case "MM":
			tempAccount = new MoneyMarket(profile,bal);
			break;
		case "S":
			tempAccount =  new Savings(profile,bal,false);
			break;
		default:
			return tempAccount;
		}
		return tempAccount;	
	}
	
	/**
	 Tries to withdraw money from a specific account with a specific profile and account type.
	 @param accountType The type of account to be withdrawn from (savings, checking, etc.)
	        profile The identifications of the person using the account (fname, lname, dob)
	        balance The amount of money to withdraw from the account.
	        paramSize The number of parameters in the given line.
	 @return null if the balance was invalid or missing in the command or if the account type was invalid.
	 		 The new account created to find and withdraw from otherwise.
	 */
	private Account withdrawCommand(String accountType, Profile profile, String balance,int paramSize) {
		if(paramSize != 6) {
			System.out.println("Missing data for withdraing from account.");
			return null;
		}
		double bal;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Withdraw - amount cannot be 0 or negative.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		Account tempAccount = null;
		switch(accountType) {
		case "C":
			tempAccount = new Checking(profile,bal);
			break;
		case "CC":
			tempAccount = new CollegeChecking(profile,bal,-1);
			break;
		case "MM":
			tempAccount = new MoneyMarket(profile,bal);
			break;
		case "S":
			tempAccount =  new Savings(profile,bal,false);
			break;
		default:
			return tempAccount;
		}
		return tempAccount;	
	}
	
	/**
	 Closes a checking account given a profile.
	 @param profile The identifications of the person using the account (fname, lname, dob)
	 @return null if the account was not found in the database
	 		 The newly created account to find and close otherwise.
	 */
	private Account closeCheckingAccount(Profile profile) {
		Checking acct = (Checking)(this.mainDatabase.getAccount(new Checking(profile,-1)));
		if(this.mainDatabase.getAccount(acct) == null) {
			System.out.println(acct.getHolder().toString() + " " + acct.getType() + " is not in the database.");
			return null;
		}
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
	 Opens a checking account given a starting balance and a profile of the person using it.
	 @param profile The identifications of a person (fname, lname, dob)
	 	 	balance The starting amount of money to open the account.
	 @return null if an invalid deposit was made, a duplicate account is in the database, 
	 		      or the account was reopened.
	 		 The newly created account, newAcc, otherwise.
	 */
	private Account checkingAccount(Profile profile, String balance) {
		double bal;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Initial deposit cannot be 0 or negative.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		Checking newAcc = new Checking(profile,bal);
		if(this.mainDatabase.getAccount(newAcc) == null && this.mainDatabase.checkDupChecking(newAcc) == -1) {
			System.out.println("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null && this.mainDatabase.checkDupChecking(newAcc) != -1) {
				System.out.println(profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				System.out.println("Account reopened.");
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
	private Account collageCheckingAccount(Profile profile, String balance, String code) {
		double bal;
		int cd;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Initial deposit cannot be 0 or negative.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		try {
			cd = Integer.parseInt(code);
			if(cd > 2 || cd < 0) {
				System.out.println("Invalid campus code.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Invalid campus code.");
			return null;
		}
		CollegeChecking newAcc = new CollegeChecking(profile,bal,cd);
		if(this.mainDatabase.checkDupChecking(newAcc) == -1) {
			System.out.println("Account opened.");
			return newAcc;
		}else {
			Account acct = this.mainDatabase.getAccount(newAcc);
			if(acct == null ||this.mainDatabase.checkDupChecking(newAcc) == -1) {
				System.out.println(profile.toString() + " same account(type) is in the database.");
				return null;
			}
			if(acct.isClosed()) {
				this.mainDatabase.reopen(newAcc);
				System.out.println("Account reopened.");
				return null;
			}
			System.out.println(profile.toString() + " same account(type) is in the database.");
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
	private Account savingsAccount(Profile profile, String balance, String code) {
		double bal;
		int cd;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Initial deposit cannot be 0 or negative.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		try {
			cd = Integer.parseInt(code);
			if(cd > 1 || cd < 0) {
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Invalid loyalty code.");
			return null;
		}
		Savings newAcc = new Savings(profile,bal,(cd == 1?true:false));
		if(this.mainDatabase.getAccount(newAcc) == null) {
			System.out.println("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				System.out.println("Account reopened.");
				return null;
			}
			System.out.println(profile.toString() + " same account(type) is in the database.");
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
	private Account moneyMarketAccount(Profile profile, String balance) {
		double bal;
		try {
			bal = Double.parseDouble(balance);
			if(bal <= 0) {
				System.out.println("Initial deposit cannot be 0 or negative.");
				return null;
			}
			if(bal < 2500) {
				System.out.println("Minimum of $2500 to open a MoneyMarket account.");
				return null;
			}
		}catch(NumberFormatException e) {
			System.out.println("Not a valid amount.");
			return null;
		}
		MoneyMarket newAcc = new MoneyMarket(profile,bal);
		if(this.mainDatabase.getAccount(newAcc) == null) {
			System.out.println("Account opened.");
			return newAcc;
		}else {
			if(this.mainDatabase.getAccount(newAcc).isClosed()) {
				this.mainDatabase.reopen(newAcc);
				System.out.println("Account reopened.");
				return null;
			}
			System.out.println(profile.toString() + " same account(type) is in the database.");
			return null;
		}		
		
	}
	
	
	
	/**
	 The main method to run the BankTeller.
	 @param args any arguments used along with the call for BankTeller. Unused.
	 */
	public static void main(String[] args) {
		new BankTeller().run();
	}

}
