package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Location {

	@Size(max = 50)
	@Pattern(regexp = "^[A-Za-z\\.\\s]*$")
	private String cityName;
	
	@Pattern(regexp = "^[A-Z][A-Z]$")
	private String stateCode;
	
	@Size(min = 3, max = 5)
	@Pattern(regexp = "^[0-9]*$")
	private String zipCode;
	
	public Location() {
		
	}
	
	public Location(String cityName, String stateCode, String zipCode) {
		this.cityName = cityName;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}
