package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailAddress {
	
	public EmailAddress() {
		
	}

	public EmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@NotNull
	@Email
	@Size(min = 3, max = 45)
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}	
	
	
}
