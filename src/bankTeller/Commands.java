package bankTeller;

/**
 The commands class holds all the valid command information for the BankTeller to use.
 It lists all the valid commands as ids for use in the BankTeller, where further
 actions will be done depending on the command.
 @author Abe Vitangcol, Jah C. Speed
 */
public enum Commands {
	O(1),
	C(2),
	D(3),
	W(4),
	P(5),
	PT(6),
	PI(7),
	UB(8),
	Q(9);
	public final int id;
	
	/**
	 Constructs the identification code for each command.
	 @param id The identification code for the given command.
	 */
	private Commands(int id) {
		this.id = id;
	}
}
