package accounts;

import java.text.DecimalFormat;

/**
 The Account class is an abstract class, which acts as a framework for the Savings and Checking classes.
 It is able to check if two accounts are the same or not as well as provide methods to print out
 the extending classes. It also deals with depositing or withdrawing money from the said accounts.
 @author Jah C. Speed, Abe Vitangcol
 */
public abstract class Account {
	protected Profile holder;
	protected boolean closed;
	protected double balance;
	
	/**
	 Checks if this account is the same as another account.
	 @param obj The object of interest to check if this account is the same as it.
	 @return true if the two are the same, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		Account compareAccount = (Account)obj;
		if(this.holder.compareTo(compareAccount.holder) == 0) {
			if(this.getType().equals(((Account)obj).getType())) {
				return true;
			}
			
		}
		return false; 
		
	}
	
	/**
	 Turns this account into a readable string.
	 Format: "Type :: Holder :: $Balance" :: "" or "CLOSED"
	 @return This account type in a readable string format.
	 */
	@Override
	public String toString() { 

		return getType() + "::" + holder.toString() + "::Balance $" + new DecimalFormat("#,##0.00").format(getBalance()) + (!this.closed? "":"::CLOSED");
	}
	
	/**
	 Remove the amount from the balance. Will not work if amount is < 0 or > balance.
	 @param amount The amount the user wants to withdraw.
	 */
	public void withdraw(double amount) {
		if(amount < 0 || amount > this.balance) {
			return;
		}
		this.balance-= amount;
		
	}
	
	/**
	 Deposit amount into the balance. Will not work if amount < 0.
	 @param amount The amount the user wants to deposit.
	 */
	public void deposit(double amount) { 
		if(amount < 0) {
			return;
		}
		this.balance+= amount;
	}
	
	/**
	 Gets the Balance of this account.
	 @return The balance of this account in a double format.
	 */
	public double getBalance() {
		return this.balance;
	}
	
	/**
	 Gets the Holder of this account.
	 @return The holder of this account.
	 */
	public Profile getHolder() {
		return this.holder;
	}
	
	/**
	 Gets the Closed value of the account. Used in children classes.
	 @return If the account is closed or not, false = open, true = closed.
	 */
	public boolean isClosed() {
		return this.closed;
	}
	/**
	 Get monthly interest for the account. Used in children classes.
	 @return monthly interest as a double
	 */
	public abstract double monthlyInterest(); //return the monthly interest
	
	/**
	 Get fee for the account. Used in children classes.
	 @return fee as a double.
	 */
	public abstract double fee(); //return the monthly fee
	
	/**
	 Get what type of account the object is. Used in children classes.
	 @return account type as a string.
	 */
	public abstract String getType(); //return the account type (class name)
}