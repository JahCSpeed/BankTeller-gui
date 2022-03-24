package accounts;

import java.text.DecimalFormat;

import accountType.Checking;
import accountType.CollegeChecking;
import accountType.MoneyMarket;
import accountType.Savings;

/**
 The AccountDatabase class manages the array of accounts in the system.
 It is responsible for returning account information, checking for duplicates, and various
 account actions such as opening, closing, depositing, and withdrawing. It is also capable of
 printing out its contents and growing when it runs out of space.
 @author Jah C. Speed, Abe Vitangcol
 */
public class AccountDatabase {
	private Account [] accounts;
	private int numAcct;
	private static final int INCREASESIZE = 4;
	
	/**
	 Constructs the Account Database with an empty array and no accounts in it.
	 */
	public AccountDatabase() {
		this.numAcct = 0;
		this.accounts = new Account[numAcct];
	}
	
	/**
	 Finds a specific account in this array of accounts and returns the index.
	 @param account The specific account to be found.
	 @return The index of the account found, -1 otherwise.
	 */
	private int find(Account account) { //return the index, or NOT_FOUND
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].equals(account)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 Finds the specific account in this array of accounts and returns the index.
	 Can be used for purposes outside of this class.
	 @param account The specific account to be found.
	 @return The index of the account found, null otherwise.
	 */
	public Account getAccount(Account account) { //return the index, or NOT_FOUND
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].equals(account)) {
				return this.accounts[i];
			}
			
		}
		
		return null;
	}
	
	/**
	 Checks if the current database has a duplicate of the account of interest.
	 @param account The account of interest to see if there is a duplicate or not.
	 @return The index "i" of where the duplicate is located in the array, -1 otherwise.
	 */
	public int checkDupChecking(Account account) {
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].equals(account)) {
				return i;
			}
			if(this.accounts[i].getHolder().compareTo(account.getHolder()) == 0) {
				if(this.accounts[i] instanceof Checking && account instanceof CollegeChecking) {
					return i;
				}
				if(this.accounts[i] instanceof CollegeChecking && account instanceof Checking) {
					return i;
				}
				if(this.accounts[i] instanceof Checking && account instanceof Checking) {
					return i;
				}	
				
			}	
		}
		return -1;
	}
	
	/**
	 Grows the capacity of the accounts array by 4.
	 Helpful especially when the old array is out of space.
	 */
	private void grow() {
		Account [] prevList = this.accounts;
		this.accounts = new Account[this.numAcct + INCREASESIZE];
		for(int i = 0; i < prevList.length; i++) {
			this.accounts[i] = prevList[i];
		}
	}
	/**
	 Adds an account into the array if there is room, and grows the array if there is no room.
	 @param account The specific account to be added to this array.
	 @return true if this operation was successful, false otherwise
	 */
	public boolean open(Account account) {
		if(this.numAcct == this.accounts.length) {
			this.grow();
		}
		if(account == null) {
			return false;
		}
		this.accounts[this.numAcct++] = account;
		return true;
	}
	
	/**
	 Closes a specific account, making it unable to be used.
	 @param account The specific account to be closed.
	 @return True if this action was successful, false otherwise.
	 */
	public boolean close(Account account) {
		int index = this.find(account);
		if(index == -1) {
			return false;
		}
		if(this.accounts[index].closed) {
			return false;
		}
		if(this.accounts[index] instanceof Savings || this.accounts[index] instanceof MoneyMarket) {
			((Savings)this.accounts[index]).setLoyalCustomer(false);
		}
		this.accounts[index].balance = 0;
		return (this.accounts[index].closed = true);
	}
	
	/**
	 Reopens a specific account, making able to be used again.
	 @param account The specific account to be reopened
	 @return True if this action was successful, false otherwise.
	 */
	public boolean reopen(Account account) {
		
		int index = this.find(account);
		if(index == -1) {
			return false;
		}
		this.accounts[index].closed = false;
		this.accounts[index].balance = account.getBalance();
		if(account instanceof Savings || account instanceof MoneyMarket) {
			((Savings)this.accounts[index]).setLoyalCustomer(((Savings)account).isLoyalCustomer());
		}
		return true;
	}
	
	/**
	 Finds the account to deposit into and deposits into it, if open.
	 @param account The account to deposit into.
	 */
	public void deposit(Account account) { 
		int index = this.find(account);
		if(index == -1) {
			return;
		}
		if(this.accounts[index].isClosed()) {
			return;
		}
		this.accounts[index].deposit(account.balance);
	}
	
	/**
	 Finds the account to withdraw from and take out money from it, if able to.
	 @param account The account to withdraw from.
	 @return False if the account was not found, is closed, or has insufficient balance, true otherwise.
	 */
	//return false if insufficient fund
	public boolean withdraw(Account account) {
		int index = this.find(account);
		if(index == -1) {
			return false;
		}
		if(this.accounts[index].isClosed()) {
			return false;
		}
		double bal = this.accounts[index].balance;
		if(bal < account.balance) {
			return false;
		}
		this.accounts[index].withdraw(account.balance);
		return true;
	} 
	
	/**
	 Prints the accounts in the database in the current order.
	 */
	public String print() {
		String returnString = "";
		if(this.numAcct == 0) {
			returnString += ("Account Database is empty!");
			return returnString;
		}

		returnString += ("*list of accounts in the database*\n");
		for(int i = 0; i < this.numAcct; i++) {
			returnString += (this.accounts[i].toString()) + "\n";
		}
		returnString += ("*end of list*" + "\n");
		return returnString;
	}
	
	/**
	 Prints the accounts in this database sorted by their account type.
	 */
	public String printByAccountType() {
		String returnString = "";
		if(this.numAcct == 0) {
			returnString += ("Account Database is empty!\n");
			return returnString;
		}
		returnString += ("*list of accounts by account type.\n");
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].getType().equals("Checking")) {
				returnString += (this.accounts[i].toString()) + "\n";
			}
		}
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].getType().equals("College Checking")) {
				returnString += (this.accounts[i].toString()) + "\n";
			}		
		}
		
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].getType().equals("Money Market Savings")) {
				returnString += (this.accounts[i].toString()) + "\n";
			}
		}
		
		for(int i = 0; i < this.numAcct; i++) {
			if(this.accounts[i].getType().equals("Savings")) {
				returnString += (this.accounts[i].toString()) + "\n";
			}		
		}
		returnString += ("*end of list.\n");
		return returnString;
	}
	
	/**
	 Prints the accounts and shows the fees and monthly interest of each account.
	 */
	public String printFeeAndInterest() { 
		String returnString = "";
		if(this.numAcct == 0) {
			returnString += ("Account Database is empty!");
			return returnString;
		}
		returnString += ("*list of accounts with fee and monthly interest\n");
		DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
		double fee;
		double montlyInterst;
		for(int i = 0; i < this.numAcct; i++) {
			fee = this.accounts[i].fee();
			montlyInterst = ( (this.accounts[i].getBalance() * (this.accounts[i].monthlyInterest())));
			if(montlyInterst <= 0) {
				montlyInterst = 0;
			}
			returnString += (this.accounts[i].toString() + "::fee $" + numberFormat.format(fee) + "::monthly interest $" + numberFormat.format(montlyInterst) + "\n");
		}
		return returnString;
	}
	
	/**
	 Updates the balance of each account and prints out the account's updated information.
	 */
	public String updateDatabase() {
		String returnString = "";
		if(this.numAcct == 0) {
			returnString += ("Account Database is empty!");
			return returnString;
		}
		returnString += ("*list of accounts with updated balance\n");
		double fee;
		double montlyInterst;
		for(int i = 0; i < this.numAcct; i++) {
			fee = this.accounts[i].fee();
			montlyInterst = ( (this.accounts[i].getBalance() * (this.accounts[i].monthlyInterest())));
			if(montlyInterst <= 0) {
				montlyInterst = 0;
			}
			this.accounts[i].balance+=montlyInterst;
			this.accounts[i].balance-=fee;
			returnString += (this.accounts[i].toString()) + "\n";
		}
		return returnString;
	}
}