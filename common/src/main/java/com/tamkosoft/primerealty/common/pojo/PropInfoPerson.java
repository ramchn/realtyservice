package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PropInfoPerson extends Person {

	public PropInfoPerson(String emailAddress, 
			String userPassword, String firstName, 
			String lastName, String middleName, 
			Integer genderId, Integer personTypeId, 
			Integer propertyInformationId) {
		
		super(emailAddress, userPassword, firstName, lastName, middleName, genderId, personTypeId);		
		this.propertyInformationId = propertyInformationId;
	}
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}
	
	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}
}
