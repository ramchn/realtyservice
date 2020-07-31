package com.tamkosoft.primerealty.signup.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class VerificationToken {

	public VerificationToken() {
		
	}

	public VerificationToken(String token) {
		this.token = token;
	}
	
	@NotNull
	@Size(min = 35, max = 45)
	@Pattern(regexp = "^[a-z|0-9-]*$")
	private String token;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}


}
