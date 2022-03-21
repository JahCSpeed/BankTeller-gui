package date;

import java.util.Calendar;

/**
 The date class creates valid dates in the mm/dd/yyyy format.
 Used for knowing past and future dates by comparing the date in question to today's date.
 @author Jah C Speed,
 */
public class Date implements Comparable<Date> {
	private int year;
	private int month;
	private int day;

	public static final int QUADRENNIAL = 4;
	public static final int CENTENNIAL = 100;
	public static final int QUATERCENTENNIAL = 400;

	/**
	 A constructor to create a date from a mm/dd/yyyy format date.
	 @param date the mm/dd/yyyy format that will be turned into a date object.
	 */
	public Date(String date) {//take mm/dd/yyyy and create a Date object
		parseDate(date);
	}

	/**
	 Constructs a Date object with today's date without any parameters needed.
	 */
	public Date() {//create an object with todays date (see Calendar class)
		Calendar cal = Calendar.getInstance();
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH);
		this.day = cal.get(Calendar.DATE);
	}

	/**
	 Constructs a new object with the same day and month, but a later year.
	 @param futureIncrease number of years added onto the current year.
	 */
	public Date(int futureIncrease) {//create an object with a date a year in advance (see Calendar class)
		Calendar cal = Calendar.getInstance();
		this.year = cal.get(Calendar.YEAR) + futureIncrease;
		this.month = cal.get(Calendar.MONTH);
		this.day = cal.get(Calendar.DATE);
	}

	/**
	 Checks this date and sees if this date is a valid date on the calendar.
	 @return false if the date is not valid, true otherwise.
	 */
	public boolean isValid() {
		
		if(this.month == -1 || this.day == -1 || this.year == -1) {
			return false;
		}
		
		if(!checkRange(this.month,0,11) || !checkRange(this.year,1,9999)) {
			return false;
		}

		
		try {
			if(isLeapYear(this.year)) {
				if(!checkRange(this.day,1,getMonth(this.month).leapYearDays)) {
					return false;
				}
			}else {
				if(!checkRange(this.day,1,getMonth(this.month).days)) {
					return false;
				}
			}
		}catch(NullPointerException e) {
			return false;
		}
		
		return true;
	}

	/**
	 Compare this date with a date in question and returns the type of date it is.
	 @param date The date being compared to this date.
	 @return 0 if the dates are the same
	 		-1 if this date is before the param date
	 		1 if this date is later than the param date
	 */
	@Override
	public int compareTo(Date date) {
		if((this.month == date.month) && (this.day == date.day) && (this.year == date.year)) {
			return 0;
		}
		if(this.year > date.year) {
			return 1;	
		}else {
			if(this.month > date.month && this.year == date.year) {
				return 1;
			}else {
				if((this.day > date.day) && (this.month == date.month) && (this.year == date.year)) {
					return 1;
				}
			}
		}
		return -1;
	}
	
	
	
	/**
	 Checks if the year is a leap year or not based on conditions for a leap year.
	 @param year the year being checked.
	 @return true if the year is a leap year, false otherwise.
	 */
	private boolean isLeapYear(int year) {
		if(year % QUADRENNIAL != 0) {
			return false;
		}else if(year % CENTENNIAL != 0) {
			return true;
		}else if(year % QUATERCENTENNIAL != 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 Checks if an int is in between a specific range of numbers
	 @param input the int being checked.
	        lowerBound the lowest number the int can be.
	        upperBound the highest number the int can be.
	 @return true if the int falls within range, false otherwise
	 */
	private boolean checkRange(int input, int lowerBound, int upperBound) {
		if(input > upperBound || input < lowerBound) {
			return false;
		}
		return true;
	}
	
	/**
	 Takes a mm/dd/yyyy date and turns it into legible tokens to create a date object.
	 @param date a mm/dd/yyyy date to find the specific values of.
	 */
	public void parseDate(String date) {
		String[] tokens = date.split("/");
		try {
			this.month = Integer.parseInt(tokens[0]) - 1;
			this.day = Integer.parseInt(tokens[1]);
			this.year = Integer.parseInt(tokens[2]);
		}catch(NumberFormatException e) {}
		
		
	}
	
	/**
	 Get the correct month from Months class for days in said month
	 @param id the identification code for a specific month.
	 @return month the correct month being asked for
	 */
	public Months getMonth(int id) {
		for(Months month : Months.values()) {
			if(month.id == id) {
				return month;
			}
		}
		return null;
	}

	/**
	 Prints out the specific date into a mm/dd/yyyy format.
	 @return a string of the object's date in "mm/dd/yyyy" format.
	 */
	@Override
	public String toString() {
		return (this.month + 1) + "/" + this.day + "/" + this.year;
	}

}