package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class User extends EmailAddress {
	
	public User() {
		
	}
	
	public User(String emailAddress, String userPassword, String firstName, 
			String lastName, String middleName, Integer genderId) {
		super(emailAddress);
		this.userPassword = userPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.genderId = genderId;
	}
		
	@Size(min = 3, max = 45)
	@Pattern(regexp = "^[A-Za-z0-9!@#$%^&*]*$")
	private String userPassword;
	
	@Size(min = 2, max = 45)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")
	private String firstName;
	
	@Size(min = 2, max = 45)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")
	private String lastName;
	
	@Size(min = 1, max = 45)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")
	private String middleName;
	
	@Positive
	@Max(3)
	private Integer genderId;
	
	public String getUserPassword() {
		return userPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	
}
