package com.tamkosoft.primerealty.issuerequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.Attachment;
import com.tamkosoft.primerealty.common.pojo.Issue;
import com.tamkosoft.primerealty.common.pojo.IssueLog;
import com.tamkosoft.primerealty.issuerequest.pojo.IssueId;
import com.tamkosoft.primerealty.issuerequest.pojo.IssueAssignPersonId;
import com.tamkosoft.primerealty.issuerequest.pojo.IssueStatusId;
import com.tamkosoft.primerealty.common.pojo.PersonId;

@RestController 
public class IssuerequestService {

	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private IssuerequestDao issuerequestDao;
	
	@Autowired
    private IssuerequestEmailer issuerequestEmailer;
	
	// view issuelogs	
	@GetMapping(value = "/viewattachmentbyissuelog")
	private ResponseEntity<byte[]> getAttachmentByIssueLog(@Valid @RequestParam Integer issuelogId) {
		
		Map<String,Object> attachment =  issuerequestDao.getAttachmentByIssueLog(issuelogId);
				
		if(!attachment.isEmpty()) {
			    
	    	byte[] contents = (byte[])attachment.get("Attachment");
	    	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.parseMediaType(attachment.get("AttachmentType").toString()));	 
	        headers.setContentDispositionFormData(attachment.get("AttachmentName").toString(), attachment.get("AttachmentName").toString());
	        
	        return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	                
		} else {			
			
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			
		}		
	}
	
	@PostMapping("/issuelogs")
	private List<Map<String, Object>> getIssueLogsByIssue(@Valid @RequestBody IssueId issueId) {
		
		return issuerequestDao.getIssueLogsByIssue(issueId.getIssueId());
		
	}
	
	// create issuelog
	@PostMapping(value = "/createissuerequestlogmp", headers = "Content-Type=multipart/form-data")
	private Map<String, Object> createIssueRequestLogMP(@RequestParam String log, 
											@RequestParam MultipartFile attachment, 
											@RequestParam Integer issueId, 
											@RequestParam Integer personId) throws IOException {
		
		primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequestLogMP() -> attachment type: " + attachment.getContentType());
		
		String AttachmentType = attachment.getContentType();
		
		if(AttachmentType.indexOf("image") != -1 || AttachmentType.indexOf("pdf") != -1 || AttachmentType.indexOf("video") != -1) {
			
			return createIssueRequestLog(new IssueLog(log, new Attachment(attachment.getBytes(), attachment.getOriginalFilename(), attachment.getContentType()), issueId, personId));
			
		} else {
			
			return new HashMap<String, Object>(Map.of("message", "unsupported file format"));
			
		}
		
		
	}
			
	@PostMapping("/createissuerequestlog")
	private Map<String, Object> createIssueRequestLog(@Valid @RequestBody IssueLog issuelog) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequestLog() -> issuelog: " + issuelog.getLog());
		
		// database entry
		resultMap = issuerequestDao.createIssueLog(issuelog);
		
		if (resultMap.containsKey("idIssueLog")) { // db entry success		

			primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequestLog() -> database entry: " + resultMap);			
			//Number idIssueLog = (Number)resultMap.get("idIssueLog");
			
			// send email (optional)
			/*
			Map<String, Object> emailResultMap = issuerequestEmailer.emailIssueRequest(issuelog, idIssueLog);			
			primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequest() -> email sent: " + emailResultMap);			
						
			resultMap.put("message", emailResultMap.get("emailmessage"));	
			*/
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create issue request, please try after sometime");
		}
		
		return resultMap;
	}
	
	// view issue request
	@GetMapping("/issuestatuses")
	private List<Map<String, Object>> getIssueStatuses() {
		  
		return issuerequestDao.getIssueStatuses();
	}
	
	@PostMapping("/updateissuestatus")
	private Map<String, Object> updateIssueStatus(@Valid @RequestBody IssueStatusId issueStatusId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap =  issuerequestDao.updateIssueStatus(issueStatusId.getIssueStatusId(), issueStatusId.getIssueId(), issueStatusId.getPersonId());
		
		if (resultMap.containsKey("idIssueDetail")) { // db entry success	
			
			primeRealtyLogger.debug(IssuerequestService.class, "assignPersonForIssue() -> database entry: " + resultMap);			
			
			resultMap.put("message", "issue status updated successfully!");	
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to update issue status, please try after sometime");
		}
		
		return resultMap;
	}
	
	@GetMapping(value = "/viewattachmentbyissue")
	private ResponseEntity<byte[]> getAttachmentByIssue(@Valid @RequestParam Integer issueId) {
		
		Map<String,Object> attachment =  issuerequestDao.getAttachmentByIssue(issueId);
				
		if(!attachment.isEmpty()) {
			    
	    	byte[] contents = (byte[])attachment.get("Attachment");
	    	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.parseMediaType(attachment.get("AttachmentType").toString()));	 
	        headers.setContentDispositionFormData(attachment.get("AttachmentName").toString(), attachment.get("AttachmentName").toString());
	        
	        return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	                
		} else {			
			
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			
		}
		
	}
	
	@PostMapping("/issuesbyperson")
	private List<Map<String, Object>> getIssuesByPerson(@Valid @RequestBody PersonId personId) {
		
		return issuerequestDao.getIssuesByPerson(personId.getPersonId());
		
	}
	
	@PostMapping("/assignedpersonforissue")
	private Map<String, Object> getAssignedPersonForIssue(@Valid @RequestBody IssueId issueId) {
		
		return issuerequestDao.getAssignedPersonForIssue(issueId.getIssueId());
		
	}
	
	@GetMapping("/getserviceproviders")
	private List<Map<String, Object>> getServiceProviders() {
		
		return issuerequestDao.getServiceProviders();
		
	}
	
	@PostMapping("/assignpersonforissue")
	private Map<String, Object> assignPersonForIssue(@Valid @RequestBody IssueAssignPersonId issuePersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = issuerequestDao.assignPersonForIssue(issuePersonId.getPersonId(), issuePersonId.getIssueId(), issuePersonId.getServiceProviderId());
		
		if (resultMap.containsKey("idIssueDetail")) { // db entry success	
			
			primeRealtyLogger.debug(IssuerequestService.class, "assignPersonForIssue() -> database entry: " + resultMap);			
			
			// send email		
			Map<String, Object> emailResultMap = issuerequestEmailer.emailServiceProvider(issuePersonId.getPersonId(), issuePersonId.getIssueId(), issuePersonId.getServiceProviderId());
			primeRealtyLogger.debug(IssuerequestService.class, "assignPersonForIssue() -> email sent: " + emailResultMap);			
			
			resultMap.put("message", emailResultMap.get("emailmessage"));	
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to assign issue request, please try after sometime");
		}
		
		return resultMap;
		 
	}
	
	// create issue request	
	//getPropertyInformationForTenant
	@PostMapping("/propinfofortenant")
	private Map<String, Object> getPropertyInformationForTenant(@Valid @RequestBody PersonId personId) {
				
		return issuerequestDao.getPropertyInformationForTenant(personId.getPersonId());
		
	}
	
	@GetMapping("/issuecategories")
	private List<Map<String, Object>> getIssueCategories() {
		  
		return issuerequestDao.getIssueCategories();
	}
		
	@PostMapping(value = "/createissuerequestmp", headers = "Content-Type=multipart/form-data")
	private Map<String, Object> createIssueRequestMP(@RequestParam String issue, 
											@RequestParam(required = false) String issueDescription, 
											@RequestParam MultipartFile attachment, 
											@RequestParam Integer issueCategoryId, 
											@RequestParam Integer personId,
											@RequestParam Integer propertyInformationId) throws IOException {
		
		primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequestMP() -> attachment type: " + attachment.getContentType());
		
		String AttachmentType = attachment.getContentType();
		
		if(AttachmentType.indexOf("image") != -1 || AttachmentType.indexOf("pdf") != -1 || AttachmentType.indexOf("video") != -1) {
			
			return createIssueRequest(new Issue(issue, issueDescription, new Attachment(attachment.getBytes(), attachment.getOriginalFilename(), attachment.getContentType()), issueCategoryId, personId, propertyInformationId));
			
		} else {
			
			return new HashMap<String, Object>(Map.of("message", "unsupported file format"));
			
		}
		
		
	}
			
	@PostMapping("/createissuerequest")
	private Map<String, Object> createIssueRequest(@Valid @RequestBody Issue issue) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequest() -> issue: " + issue.getIssue());
		
		// database entry
		resultMap = issuerequestDao.createIssue(issue);
		
		if (resultMap.containsKey("idIssue") && resultMap.containsKey("idIssueDetail")) { // db entry success		

			primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequest() -> database entry: " + resultMap);			
			Number idIssue = (Number)resultMap.get("idIssue");
			
			// send email
			Map<String, Object> emailResultMap = issuerequestEmailer.emailIssueRequest(issue, idIssue);			
			primeRealtyLogger.debug(IssuerequestService.class, "createIssueRequest() -> email sent: " + emailResultMap);			
			
			resultMap.put("message", emailResultMap.get("emailmessage"));	
			
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create issue request, please try after sometime");
		}
		
		return resultMap;
	}
		
}
