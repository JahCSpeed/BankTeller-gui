package accountType;

import accounts.Profile;

/**
 The CollegeChecking class extends from the Checking class to give specifics for a College Checking account.
 Has its own fee and monthly interest separate from the checking accounts, but needs a campus code
 to open it. Invalid campus codes will not work.
 @author Jah C. Speed, Abe Vitangcol
 */
public class CollegeChecking extends Checking{
	private int campusCode;
	private static final double ANNUAL_INTEREST_RATE = 0.0025;
	public CollegeChecking(Profile holder,double balance, int campusCode) {
		super(holder, balance);
		this.campusCode = campusCode;
	}
	

	/**
	 Returns the monthly fee of this account.
	 @return The monthly fee of this account.
	 */
	@Override
	public double fee() {
		return 0;
	}
	
	/**
	 Returns the monthly interest of this account.
	 @return The monthly interest rate of this account.
	 */
	public double monthlyInterest() {
		return ANNUAL_INTEREST_RATE / 12;
	}
	
	/**
	 Gets the type of this checking account and returns it as a string.
	 @return The account type in a String format.
	 */
	@Override
	public String getType() {
		return "College Checking";
	}
	
	/**
	 Gets the campus code of this particular college checking.
	 Necessary to open a college checking.
	 @return The campus code for this account.
	 */
	public int getCampusCode() {
		return this.campusCode;
	}
	
	/**
	 Uses the Account method to make this account into a readable string.
	 @return The account type in the form of "Type::Holder::Balance $####.00::(CLOSED if closed)"
	         along with a campus location.
	 */
	@Override
	public String toString() {
		return super.toString() + this.getLocation();
	}
	
	/**
	 Gets the campus location given this account's code.
	 @return The campus location of this account, if applicable.
	 */
	private String getLocation() {
		switch(this.campusCode) {
			case 0:
				return "::NEW_BRUNSWICK";
			case 1:
				return "::NEWARK";
			case 2:
				return "::CAMDEN";
			default:
				return "";
		}
	}
}
