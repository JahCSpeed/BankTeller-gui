package application;

import date.Date;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public abstract class accountScene  {
	private String name;
	private String accountType;
	private Date dateOfBirth;
	private BorderPane mainPane;
	
	public accountScene(BorderPane mainPane, String name, String accountType, Date dateOfBirth  ) {
		this.mainPane = mainPane;
		this.name = name;
		this.accountType = accountType;
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getAccountName() {
		return this.name;
	}
	
	public String getAccountType() {
		return this.accountType;
	}
	public Date getDOB() {
		return this.dateOfBirth;
	}

}
