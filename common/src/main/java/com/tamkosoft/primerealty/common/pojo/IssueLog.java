package com.tamkosoft.primerealty.common.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class IssueLog {

	@NotNull
	@Size(min = 2, max = 250)
	@Pattern(regexp = "^[A-Za-z0-9!@#$%^&*?/,()\\.\\s]*$")
	private String log;
	
	private Attachment attachment;
	
	@NotNull
	@Positive
	private Integer issueId;
	
	@NotNull
	@Positive
	private Integer personId;
	
	
	public IssueLog () {
		
	}
	
	public IssueLog(String log, Attachment attachment, Integer issueId,
			Integer personId) {

		this.log = log;
		this.attachment = attachment;
		this.issueId = issueId;
		this.personId = personId;
	}
	
	public Integer getIssueId() {
		return issueId;
	}


	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}


	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

}
