package com.tamkosoft.primerealty.myinfo.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.tamkosoft.primerealty.common.pojo.ServiceProvider;

public class ServiceProviderWithId extends ServiceProvider {

	@NotNull
	@Positive
	private Integer serviceProviderId;
	
	public ServiceProviderWithId() {
		
	}

	public ServiceProviderWithId(Integer serviceCategoryId, String yearsOfExperience, String areasOfExpertise,
			String areaCoverage, Integer personId, Integer serviceProviderId) {
		super(serviceCategoryId, yearsOfExperience, areasOfExpertise, areaCoverage, personId);
		this.serviceProviderId = serviceProviderId;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
		
}
