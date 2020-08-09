package com.tamkosoft.primerealty.common.pojo;

public class Attachment {

	private byte[] attachment;
	private String attachmentName;
	private String attachmentType;
	
	public Attachment() {
		
	}

	public Attachment(byte[] attachment, String attachmentName, String attachmentType) {
		this.attachment = attachment;
		this.attachmentName = attachmentName;
		this.attachmentType = attachmentType;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
		
}
