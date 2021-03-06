package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PropInfoPerson extends Person {
	
	public PropInfoPerson() {
		
	}

	public PropInfoPerson(String emailAddress, 
			String userPassword, String firstName, 
			String lastName, String middleName, 
			Integer genderId, Integer personTypeId, 
			Integer propertyInformationId, Integer updateByPersonId) {
		
		super(emailAddress, userPassword, firstName, lastName, middleName, genderId, personTypeId);		
		this.propertyInformationId = propertyInformationId;
		this.updateByPersonId = updateByPersonId;
		
	}
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	@NotNull
	@Positive
	private Integer updateByPersonId;
		
	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}
	
	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}

	public Integer getUpdateByPersonId() {
		return updateByPersonId;
	}

	public void setUpdateByPersonId(Integer updateByPersonId) {
		this.updateByPersonId = updateByPersonId;
	}
	
}
