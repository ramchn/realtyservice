package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class PropertyInformation extends Address {

	@Size(max = 5)
	@Pattern(regexp = "^[0-9]*$")
	private String squareFeet;
	
	@Size(max = 4)
	private String beds;
	
	@Size(max = 45)
	private String baths;
	
	@Size(max = 45)
	private String stories;
	
	@Size(max = 45)
	private String lotSize;
	
	@Size(max = 4)
	@Pattern(regexp = "^[0-9]*$")
	private String yearBuilt;
	
	@Size(max = 4)
	@Pattern(regexp = "^[0-9]*$")
	private String yearRenovated;
	
	@Size(max = 45)
	private String apn;
	
	@Size(max = 45)
	private String community;
	
	@Size(max = 45)
	private String hoaDues;
	
	@NotNull
	@Positive
	@Max(4)
	private Integer propertyTypeId;
	
	@NotNull
	@Positive
	private Integer personId;
	
	public PropertyInformation() {
		
	}
	
	public PropertyInformation(String squareFeet, String beds, String baths, String stories, String lotSize,
			String yearBuilt, String yearRenovated, String apn, String community, String hoaDues,
			Integer propertyTypeId, String address1, String address2, String address3, Integer zip, String cityName, Integer stateId,
			Integer countryId, Integer personId) {
		
		super(address1, address2, address3, zip, cityName, stateId,
				countryId, null);
		
		this.squareFeet = squareFeet;
		this.beds = beds;
		this.baths = baths;
		this.stories = stories;
		this.lotSize = lotSize;
		this.yearBuilt = yearBuilt;
		this.yearRenovated = yearRenovated;
		this.apn = apn;
		this.community = community;
		this.hoaDues = hoaDues;
		this.propertyTypeId = propertyTypeId;
		this.personId = personId;
	}
	public String getSquareFeet() {
		return squareFeet;
	}
	public void setSquareFeet(String squareFeet) {
		this.squareFeet = squareFeet;
	}
	public String getBeds() {
		return beds;
	}
	public void setBeds(String beds) {
		this.beds = beds;
	}
	public String getBaths() {
		return baths;
	}
	public void setBaths(String baths) {
		this.baths = baths;
	}
	public String getStories() {
		return stories;
	}
	public void setStories(String stories) {
		this.stories = stories;
	}
	public String getLotSize() {
		return lotSize;
	}
	public void setLotSize(String lotSize) {
		this.lotSize = lotSize;
	}
	public String getYearBuilt() {
		return yearBuilt;
	}
	public void setYearBuilt(String yearBuilt) {
		this.yearBuilt = yearBuilt;
	}
	public String getYearRenovated() {
		return yearRenovated;
	}
	public void setYearRenovated(String yearRenovated) {
		this.yearRenovated = yearRenovated;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getHoaDues() {
		return hoaDues;
	}
	public void setHoaDues(String hoaDues) {
		this.hoaDues = hoaDues;
	}
	public Integer getPropertyTypeId() {
		return propertyTypeId;
	}
	public void setPropertyTypeId(Integer propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
}
