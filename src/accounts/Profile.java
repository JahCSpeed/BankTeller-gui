package accounts;
import date.Date;

/**
 The Profile class holds all the identifications of a person needed to open accounts.
 Holds the person's first name, last name, and date-of-birth and is able to return these values.
 Can also check if two profiles are the same.
 @author Jah C. Speed, Abe Vitangcol
 */
public class Profile {
	private String fname;
	private String lname;
	private Date dob;
	
	/**
	 Constructs a profile given the person's first name, last name, and date-of-birth.
	 @param fname The person's first name.
	        lname The person's last name.
	        dob The date at which they were born.
	 */
	public Profile(String fname, String lname, Date dob) {
		this.fname = fname;
		this.lname = lname;
		this.dob = dob;
	}
	
	/**
	 Gets the first name of this profile.
	 @return The first name of this profile.
	 */
	public String getfName() {
		return this.fname;
	}
	
	/**
	 Gets the last name of this profile.
	 @return The last name of this profile.
	 */
	public String getlName() {
		return this.lname;
	}
	
	/**
	 Gets the date-of-birth of this profile.
	 @return The date of birth of this profile.
	 */
	public Date getDOB() {
		return this.dob;
	}
	
	/**
	 Compares this person to another person and sees if they are the same person or not.
	 @param person2 The other person to check if they are the same as this one.
	 @return 0 if they are the same person, -1 otherwise.
	 */
	public int compareTo(Profile person2) {
		if(this.fname.toLowerCase().equals(person2.getfName().toLowerCase())) {
			if(this.lname.toLowerCase().equals(person2.getlName().toLowerCase())) {
				if(this.dob.compareTo(person2.getDOB()) == 0) {
					return 0;
				}
			}
		}
		return -1;
	}
	
	/**
	 Takes the identifications of the person and returns it in a readable string format.
	 @return The person's name and dob in "Fname Lname DOB" format
	 */
	public String toString() {
		return fname + " " + lname + " " + dob.toString();
	}
}