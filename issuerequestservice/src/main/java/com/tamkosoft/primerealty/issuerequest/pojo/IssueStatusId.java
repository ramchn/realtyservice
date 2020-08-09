package com.tamkosoft.primerealty.issuerequest.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class IssueStatusId {

	@NotNull
	@Positive
	private Integer issueId;
	
	@NotNull
	@Positive
	private Integer issueStatusId;
	
	@NotNull
	@Positive
	private Integer personId;
	
	public IssueStatusId() {
		
	}

	public IssueStatusId(Integer issueId, Integer issueStatusId, Integer personId) {
		this.issueId = issueId;
		this.issueStatusId = issueStatusId;
		this.personId = personId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
	public Integer getIssueStatusId() {
		return issueStatusId;
	}

	public void setIssueStatusId(Integer issueStatusId) {
		this.issueStatusId = issueStatusId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
		
}
