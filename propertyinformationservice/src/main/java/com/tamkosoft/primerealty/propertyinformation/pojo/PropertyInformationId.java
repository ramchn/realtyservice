package com.tamkosoft.primerealty.propertyinformation.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PropertyInformationId {
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	public PropertyInformationId() {
		
	}

	public PropertyInformationId(Integer propertyInformationId) {		
		this.propertyInformationId = propertyInformationId;
	}
	
	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}

	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}
	
}
