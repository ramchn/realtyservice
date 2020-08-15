package com.tamkosoft.primerealty.propertyinformation.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class PropertyInformationDeleted {

	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	@NotNull
	@Size(min = 1, max = 45)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")
	private String deletedReason;
	
	@NotNull
	@Positive
	private Integer personId;
	
	public PropertyInformationDeleted () {
		
	}

	public PropertyInformationDeleted(Integer propertyInformationId,
			String deletedReason,
			Integer personId) {
		this.propertyInformationId = propertyInformationId;
		this.deletedReason = deletedReason;
		this.personId = personId;
	}

	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}

	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}

	public String getDeletedReason() {
		return deletedReason;
	}

	public void setDeletedReason(String deletedReason) {
		this.deletedReason = deletedReason;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	
	
}
