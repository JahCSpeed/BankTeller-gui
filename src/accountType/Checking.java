package accountType;

import accounts.Account;
import accounts.Profile;

/**
 The Checking class extends from the Account class to give specifics for Checking Accounts.
 Holds information such as the holder, the account balance, and whether it is closed or not
 and can give information such as fees or monthly interest for a Checking Account.
 @author Jah C. Speed, Abe Vitangcol
 */
public class Checking extends Account{
	private static final double BALANCE_THRESHOLD = 1000;
	private static final double ANNUAL_INTEREST_RATE = 0.001; 
	/**
	 Constructs a Checking Account with a holder, balance, and a statement of whether it is closed or not.
	 See the parent class, Account, for the list of abstract methods and instance variables.
	 @param holder The person who is holding this account.
	        balance The balance the account has.
	 */
	public Checking(Profile holder,double balance) {
		this.holder = holder;
		this.closed = false;
		this.balance = balance;
	}
	
	/**
	 Returns the monthly interest rate of the account.
	 @return The monthly interest rate in a double format.
	 */
	public double monthlyInterest() {
		return ANNUAL_INTEREST_RATE / 12;
	}

	/**
	 Returns the fee of the account.
	 @return 0 if the account is at least the balance threshold, 25 otherwise.
	 */
	public double fee() {
		if(this.balance >= BALANCE_THRESHOLD) {
			return 0;
		}else {
			return 25;
		}
	}

	/**
	 Returns the kind of account this account is.
	 @return "Checking" in a string format.
	 */
	public String getType() {
		return "Checking";
	}
	
	/**
	 Uses the Account method to make this account into a readable string.
	 @return The account type in the form of "Type::Holder::Balance $####.00::(CLOSED if closed)"
	 */
	public String toString() {
		return super.toString();
	}

}
