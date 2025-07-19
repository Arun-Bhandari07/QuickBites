package com.QuickBites.app.DTO;

public class PendingUserReapplyDTO {
	private String firstName;
	private String lastName;
	private String userName;
	private String email;

	//This DTO securely sends only the necessary data for reapply these are autofill fields
	
	public PendingUserReapplyDTO(String firstName, String lastName, String userName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
