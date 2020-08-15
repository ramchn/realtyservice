package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CountryId {

	@NotNull
	@Positive
	@Max(239)
	private Integer countryId;
	
	public CountryId() {
		
	}

	public CountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
}
