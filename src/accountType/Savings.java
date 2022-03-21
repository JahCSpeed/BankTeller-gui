package accountType;

import accounts.Account;
import accounts.Profile;

/**
 The Savings class extends from the Account class to give specifics for Savings Accounts.
 Holds information such as the holder, the account balance, and whether it is closed or not. 
 It can give information such as fees or monthly interest for a Savings Account as well as
 state if the holder is a loyal customer or not.
 @author Jah C. Speed, Abe Vitangcol
 */
public class Savings extends Account {
	
	private boolean loyalCustomer;
	private static final double BALANCE_THRESHOLD = 300;
	private static final double ANNUAL_INTEREST_RATE = 0.003;
	private static final double LOYAL_ANNUAL_INTEREST_RATE = 0.0045;
	
	/**
	 Constructs a Savings Account with a holder, balance, and details if this is closed or loyal or not.
	 See the parent class, Account, for the list of abstract methods and instance variables.
	 @param holder The person who is holding this account.
	        balance The balance of this account.
	        loyalCustomer Whether the customer is loyal or not, which affects interest rates.
	 */
	public Savings(Profile holder,double balance,boolean loyalCustomer) {
		this.holder = holder;
		this.closed = false;
		this.balance = balance;
		this.loyalCustomer = loyalCustomer;
	}
	
	/**
	 Returns the monthly interest rate of the account.
	 @return The loyal monthly interest rate if the holder is loyal, the regular interest rate otherwise.
	 */
	@Override
	public double monthlyInterest() {
		return (this.loyalCustomer?LOYAL_ANNUAL_INTEREST_RATE:ANNUAL_INTEREST_RATE) / 12;
	}

	/**
	 Returns the fee of the account.
	 @return 0 if the account is at least the balance threshold, 6 otherwise.
	 */
	@Override
	public double fee() {
		if(this.balance >= BALANCE_THRESHOLD) {
			return 0;
		}else {
			return 6;
		}
	}

	/**
	 Returns the kind of account this account is.
	 @return "Savings" in a string format.
	 */
	@Override
	public String getType() {
		return "Savings";
	}
	
	/**
	 Returns whether this account has a loyal customer or not.
	 @return The boolean instance variable, loyalCustomer.
	 */
	public boolean isLoyalCustomer() {
		return this.loyalCustomer;
	}
	
	/**
	 Changes the status of this account to be a loyal customer or not a loyal one.
	 @param value Whether this customer is a loyal customer or not.
	 */
	public void setLoyalCustomer(boolean value) {
		this.loyalCustomer = value;
	}
	
	/**
	 Uses the Account method to make this account into a readable string.
	 @return The account type in the form of "Type::Holder::Balance $####.00::(CLOSED if closed)"
	 		 along with whether this account is loyal or not.
	 */
	public String toString() {
		return super.toString() + (this.isLoyalCustomer()? "::Loyal":"");
	}
	

}
