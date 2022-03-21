package accountType;

import accounts.Profile;

/**
 The MoneyMarket class extends from the Savings class to give and hold specifics for a Money Market account.
 Maintains the functions of the Savings class while having its own interest rate, fee
 and loyalty check.
 @author Jah C. Speed, Abe Vitangcol
 */
public class MoneyMarket extends Savings {
	private int withdrawCounter;
	private static final double BALANCE_THRESHOLD = 2500;
	private static final double ANNUAL_INTEREST_RATE = 0.008;
	private static final double LOYAL_ANNUAL_INTEREST_RATE = 0.0095;
	private static final int MAX_WITHDRAW_AMOUNT = 2;
	
	/**
	 Constructs a Money Market account using the same parameters as the Savings class.
	 The loyalty value is always set to be true.
	 @param holder The person holding this account.
	        balance The balance to make the account start with.
	 */
	public MoneyMarket(Profile holder,double balance) {
		super(holder,balance, true);
	}
	
	/**
	 Finds and returns the monthly interest of this account, depending on loyalty.
	 @return The loyal monthly interest rate if the profile is loyal, the regular otherwise.
	 */
	@Override
	public double monthlyInterest() {
		return (this.isLoyalCustomer()?LOYAL_ANNUAL_INTEREST_RATE:ANNUAL_INTEREST_RATE) / 12;
	}

	/**
	 Finds and returns the fee of this account.
	 @return The monthly fee of this account given its loyalty.
	 */
	@Override
	public double fee() {
		if(this.balance >= BALANCE_THRESHOLD && !(this.withdrawCounter >= MAX_WITHDRAW_AMOUNT)) {
			return 0;
		}else {
			return 10;
		}
	}

	/**
	 Gets the Type of account this account is and returns it.
	 @return The account type in a String format.
	 */
	@Override
	public String getType() {
		return "Money Market Savings";
	}
	
	/**
	 Withdraws a specific amount from this account, then updates its loyalty.
	 @param amount The amount to withdraw from this account.
	 */
	@Override
	public void withdraw(double amount) {
		super.withdraw(amount);
		this.withdrawCounter++;
		this.checkLoyalty();
	}
	
	/**
	 Checks if this account is still considered loyal.
	 */
	public void checkLoyalty() {
		if(this.getBalance()< 2500) {
			this.setLoyalCustomer(false);
		}
	}
	
	/**
	 Uses the Account method to make this account into a readable string.
	 @return The account type in the form of "Type::Holder::Balance $####.00::(CLOSED if closed)"
	 		 along with the amount of times this account has done a withdrawal.
	 */
	public String toString() {
		return super.toString() + "::withdrawl: " + this.withdrawCounter;
	}

}
