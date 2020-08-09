package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class Issue {

	private Integer issueId;
	
	@NotNull
	@Size(min = 2, max = 100)
	@Pattern(regexp = "^[A-Za-z0-9!@#$%^&*?/()\\.\\s]*$")
	private String issue;
	
	private Attachment attachment;
	
	@Size(min = 2, max = 255)
	@Pattern(regexp = "^[A-Za-z0-9!@#$%^&*?/()\\.\\s]*$")
	private String issueDescription;
	
	@NotNull
	@Positive
	@Max(10)
	private Integer issueCategoryId;
	
	@NotNull
	@Positive
	private Integer personId;
	
	@NotNull
	@Positive
	private Integer propertyInformationId;
	
	public Issue () {
		
	}
	
	public Issue(String issue, String issueDescription, Attachment attachment, Integer issueCategoryId,
			Integer personId, Integer propertyInformationId) {

		this.issue = issue;
		this.issueDescription = issueDescription;
		this.attachment = attachment;
		this.issueCategoryId = issueCategoryId;
		this.personId = personId;
		this.propertyInformationId = propertyInformationId;
	}

	
	public Integer getIssueId() {
		return issueId;
	}


	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}


	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	
	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}


	public Integer getIssueCategoryId() {
		return issueCategoryId;
	}

	public void setIssueCategoryId(Integer issueCategoryId) {
		this.issueCategoryId = issueCategoryId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getPropertyInformationId() {
		return propertyInformationId;
	}

	public void setPropertyInformationId(Integer propertyInformationId) {
		this.propertyInformationId = propertyInformationId;
	}
		
}
