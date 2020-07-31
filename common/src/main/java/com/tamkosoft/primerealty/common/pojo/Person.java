package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Person extends User {

	public Person() {
		
	}
	
	public Person(String emailAddress, 
			String userPassword, String firstName, 
			String lastName, String middleName, 
			Integer genderId, Integer personTypeId) {
		
		super(emailAddress, userPassword, firstName, lastName, middleName, genderId);		
		this.personTypeId = personTypeId;
		
	}

	@NotNull
	@Positive
	@Max(7)
	private Integer personTypeId;
	
	public Integer getPersonTypeId() {
		return personTypeId;
	}
	
	public void setPersonTypeId(Integer personTypeId) {
		this.personTypeId = personTypeId;
	}
	
}
