package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class Address {

	@NotNull
	@Size(max = 45)
	@Pattern(regexp = "^[A-Za-z0-9\\.\\s]*$")	
	private String address1;
	
	@Size(max = 45)
	@Pattern(regexp = "^[A-Za-z0-9#\\s]*$")	
	private String address2;
	
	@Size(max = 45)
	@Pattern(regexp = "^[A-Za-z0-9\\.\\s]*$")	
	private String address3;
	
	@Positive
	private Integer zip;
	
	@Size(max = 45)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")		
	private String cityName;
	
	@Positive
	private Integer stateId;
	
	@Positive
	private Integer countryId;
	
	@Positive
	private Integer personId;
	
	public Address() {
		
	}
	
	public Address(String address1, String address2, String address3, Integer zip, String cityName, Integer stateId,
			Integer countryId, Integer personId) {
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.zip = zip;
		this.cityName = cityName;
		this.stateId = stateId;
		this.countryId = countryId;
		this.personId = personId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountry(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
}
