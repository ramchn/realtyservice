package com.tamkosoft.primerealty.propertyinformation.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.tamkosoft.primerealty.common.pojo.PersonId;

public class PersonPropertyInformationId extends PersonId {
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	public PersonPropertyInformationId() {
		
	}

	public PersonPropertyInformationId(Integer propertyInformationId, Integer personId) {
		super(personId);
		this.propertyInformationId = propertyInformationId;
	}
	
	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}

	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}
	
}
