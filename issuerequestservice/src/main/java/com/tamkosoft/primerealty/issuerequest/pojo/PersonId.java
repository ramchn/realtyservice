package com.tamkosoft.primerealty.issuerequest.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PersonId {

	@NotNull
	@Positive
	private Integer personId;
	
	public PersonId() {
		
	}

	public PersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
}
