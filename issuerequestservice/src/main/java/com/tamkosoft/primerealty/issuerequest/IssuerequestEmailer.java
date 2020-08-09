package com.tamkosoft.primerealty.issuerequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyEmailer;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.Issue;

@Component("issuerequestEmailer")
public class IssuerequestEmailer {
	
	@Autowired
	Environment env;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private PrimeRealtyEmailer primeRealtyEmailer;
	
	@Autowired
    private IssuerequestDao issuerequestDao;
	
	@Autowired
	PrimeRealtyDao primeRealtyDao;
		
	public Map<String, Object> emailIssueRequest(Issue issue, Number idIssue) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String FromAddress = env.getProperty("email.address");	
		
		String ToAddress = issuerequestDao.getSenderEmailByPerson(issue.getPersonId());		
		Map<String, Object> user = primeRealtyDao.getUserByPerson(issue.getPersonId());
		String CreatedBy = user.get("Name") != null ? user.get("Name").toString() : "none";				
		String IssueCategory = issuerequestDao.getIssueCategoryById(issue.getIssueCategoryId());
		
		String Subject = env.getProperty("issuerequest.email.subject").replace("<IR>", "(#IR"+idIssue+")").replace("<Tenant>", CreatedBy);
		String Content = env.getProperty("issuerequest.email.content").replace("<IR>", "(#IR"+idIssue+")").replace("<Tenant>", CreatedBy).replace("<Date>", new Date().toString()).replace("<IssueCategory>", IssueCategory).replace("<Issue>", issue.getIssue()).replace("<IssueDescription>", issue.getIssueDescription() != null ? issue.getIssueDescription() : "none");
										
		try {
			if(ToAddress != null && issue.getAttachment() != null) {
				primeRealtyEmailer.sendEmailWithAttachment(FromAddress, ToAddress, Subject, Content, issue.getAttachment().getAttachment(), issue.getAttachment().getAttachmentName(), issue.getAttachment().getAttachmentType());
				resultMap.put("emailmessage", "issue (#IR"+ idIssue +") requested successfully. Please note this number for future reference");
				
			} else if(ToAddress != null) {
				primeRealtyEmailer.sendEmail(FromAddress, ToAddress, Subject, Content);
				resultMap.put("emailmessage", "issue (#IR"+ idIssue +") requested successfully. Please note this number for future reference");
				
			} else {
				resultMap.put("emailmessage", "issue (#IR"+ idIssue +") created successfully, but unable to send email - Sender EmailAddress not found");
				
			}
			
		}  catch(AddressException ae) {
			resultMap.put("emailmessage", "issue (#IR"+ idIssue +") created successfully, but unable to send issue request - due to invalid address");
			primeRealtyLogger.error(IssuerequestEmailer.class, "emailIssueRequest() -> AddressException - " + ae.getMessage());
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "issue (#IR"+ idIssue +") created successfully, but unable to send issue request - due to server issue");
			primeRealtyLogger.error(IssuerequestEmailer.class, "emailIssueRequest() -> MessagingException - " + me.getMessage());
		
		} 
		
		return resultMap;
	}
	
	public Map<String, Object> emailServiceProvider(Integer PersonId, Integer IssueId, Integer ServiceProviderId) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String FromAddress = env.getProperty("email.address");	
		
		Integer SpPersonId = issuerequestDao.getPersonIdByServiceProviderId(ServiceProviderId);
		
		Map<String, Object> user = primeRealtyDao.getUserByPerson(SpPersonId);
		String ToAddress = user.get("EmailAddress") != null ? user.get("EmailAddress").toString() : null;		
		
		Issue issue = issuerequestDao.getIssueById(IssueId);
		String IssueCategory = issuerequestDao.getIssueCategoryById(issue.getIssueCategoryId());
		
		Map<String, Object> user1 = primeRealtyDao.getUserByPerson(PersonId);
		String AssignedBy = user1.get("Name") != null ? user1.get("Name").toString() : "none";				
		
		
		String Subject = env.getProperty("serviceprovider.email.subject").replace("<IR>", "(#IR"+IssueId+")").replace("<Person>", AssignedBy);
		String Content = env.getProperty("serviceprovider.email.content").replace("<IR>", "(#IR"+IssueId+")").replace("<Person>", AssignedBy).replace("<Date>", new Date().toString()).replace("<IssueCategory>", IssueCategory).replace("<Issue>", issue.getIssue()).replace("<IssueDescription>", issue.getIssueDescription() != null ? issue.getIssueDescription() : "none");
										
		try {
			if(ToAddress != null) {
				primeRealtyEmailer.sendEmail(FromAddress, ToAddress, Subject, Content);
				resultMap.put("emailmessage", "issue assigned successfully, email sent to Service Provider");
				
			} else {
				resultMap.put("emailmessage", "issue assigned successfully, but unable to send email - Service Provider EmailAddress not found");
				
			}
			
		}  catch(AddressException ae) {
			resultMap.put("emailmessage", "issue assigned successfully, but unable to send issue request - due to invalid address");
			primeRealtyLogger.error(IssuerequestEmailer.class, "emailServiceProvider() -> AddressException - " + ae.getMessage());
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "issue assigned successfully, but unable to send issue request - due to server issue");
			primeRealtyLogger.error(IssuerequestEmailer.class, "emailServiceProvider() -> MessagingException - " + me.getMessage());
		
		} 
		
		return resultMap;
	}
	
}
