package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ServiceProvider extends PersonId {

	@NotNull
	@Positive
	@Max(10)
	private Integer serviceCategoryId;
	
	@NotNull
	@Size(max = 4)
	@Pattern(regexp = "^[0-9]*$")
	private String yearsOfExperience;
	
	@NotNull
	@Size(max = 45)
	private String areasOfExpertise;
	
	@NotNull
	@Size(max = 45)
	private String areaCoverage;
	
	public ServiceProvider() {
		
	}
	
	public ServiceProvider(Integer serviceCategoryId, String yearsOfExperience, String areasOfExpertise,
			String areaCoverage, Integer personId) {
		super(personId);
		this.serviceCategoryId = serviceCategoryId;
		this.yearsOfExperience = yearsOfExperience;
		this.areasOfExpertise = areasOfExpertise;
		this.areaCoverage = areaCoverage;
	}

	public Integer getServiceCategoryId() {
		return serviceCategoryId;
	}

	public void setServiceCategoryId(Integer serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}

	public String getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(String areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public String getAreaCoverage() {
		return areaCoverage;
	}

	public void setAreaCoverage(String areaCoverage) {
		this.areaCoverage = areaCoverage;
	}
		
}
