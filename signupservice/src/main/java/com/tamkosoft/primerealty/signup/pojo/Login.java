package com.tamkosoft.primerealty.signup.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tamkosoft.primerealty.common.pojo.EmailAddress;

public class Login extends EmailAddress {
	
	@NotNull
	@Size(min = 3, max = 45)
	@Pattern(regexp = "^[A-Z|a-z|0-9|!@#$%^&*]*$")
	private String userPassword;
	
	public Login() {
		
	}
	
	public Login(String emailAddress, String userPassword) {
		super(emailAddress);
		this.userPassword = userPassword;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
		
}
