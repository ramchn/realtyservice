package com.tamkosoft.primerealty.issuerequest.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class IssueId {

	@NotNull
	@Positive
	private Integer issueId;
	
	public IssueId() {
		
	}

	public IssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
}
