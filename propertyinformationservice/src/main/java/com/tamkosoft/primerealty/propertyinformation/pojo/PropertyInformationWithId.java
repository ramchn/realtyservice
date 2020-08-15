package com.tamkosoft.primerealty.propertyinformation.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.tamkosoft.primerealty.common.pojo.PropertyInformation;

public class PropertyInformationWithId extends PropertyInformation {

	@NotNull
	@Positive
	private Integer addressId;
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	public PropertyInformationWithId() {
		
	}

	public PropertyInformationWithId(String squareFeet, String beds, String baths, String stories, String lotSize,
			String yearBuilt, String yearRenovated, String apn, String community, String hoaDues,
			Integer propertyTypeId, String address1, String address2, String address3, Integer zip, String cityName, Integer stateId,
			Integer countryId, Integer personId, Integer addressId, Integer propertyInformationId) {
		super(squareFeet, beds, baths, stories, lotSize,
				yearBuilt, yearRenovated, apn, community, hoaDues,
				propertyTypeId, address1, address2, address3, zip, cityName, stateId,
				countryId, personId);
		this.addressId = addressId;
		this.propertyInformationId = propertyInformationId;
	}
		
	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}

	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}
	
}
