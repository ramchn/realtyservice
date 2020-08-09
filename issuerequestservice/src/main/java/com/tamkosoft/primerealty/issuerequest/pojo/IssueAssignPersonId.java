package com.tamkosoft.primerealty.issuerequest.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class IssueAssignPersonId {

	@NotNull
	@Positive
	private Integer issueId;
	
	@NotNull
	@Positive
	private Integer personId;
	
	@NotNull
	@Positive
	private Integer serviceProviderId;
	
	public IssueAssignPersonId() {
		
	}

	public IssueAssignPersonId(Integer issueId, Integer personId, Integer serviceProviderId) {
		this.issueId = issueId;
		this.personId = personId;
		this.serviceProviderId = serviceProviderId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}	

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
}
