package com.tamkosoft.primerealty.propertyinformation.pojo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PropInfoPersonWithId {
	
	public PropInfoPersonWithId() {
		
	}

	public PropInfoPersonWithId(Integer propertyInformationId, Integer updateByPersonId, Integer personId, Date startDate, Date endDate) {
		
		this.propertyInformationId = propertyInformationId;
		this.updateByPersonId = updateByPersonId;
		this.personId = personId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	@NotNull
	@Positive
	private Integer updateByPersonId;
	
	@NotNull
	@Positive
	private Integer personId;	
	
	private Date startDate;	
	private Date endDate;
	
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

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
