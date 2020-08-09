package com.tamkosoft.primerealty.issuerequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.Issue;
import com.tamkosoft.primerealty.common.pojo.IssueLog;
import com.tamkosoft.primerealty.common.pojo.Attachment;

@Component("issuerequestDao")
public class IssuerequestDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	PrimeRealtyDao primeRealtyDao;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	public List<Map<String, Object>> getPropertyInformationsByIds(Integer[] propertyInformationIds) {
		
		Set<Integer> ids = new HashSet<Integer>();
		for(int i = 0; i < propertyInformationIds.length; i++) {
			ids.add(propertyInformationIds[i]);
		}
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);		
				
		return namedParameterJdbcTemplate.queryForList(PROPINFOS_BYIDS_QUERY, parameters);			
		
	}
	
	public List<Map<String, Object>> getIssueStatuses() {
		
		return jdbcTemplate.queryForList(ISSUESTATUS_QUERY);
	}
	
	public Map<String, Object> updateIssueStatus(Integer IssueStatusId, Integer IssueId, Integer PersonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {			
			Map<String, Object> issueDetailParams = new HashMap<String, Object>();
			issueDetailParams.put("Issue_idIssue", IssueId);
			issueDetailParams.put("Status_idIssueStatus", IssueStatusId);
			issueDetailParams.put("StatusUpdate_idPerson", PersonId);
			issueDetailParams.put("StatusUpdateDate", new Date());			

			Number idIssueDetail = primeRealtyDao.tableInsert("IssueDetail",issueDetailParams);
			
			if(idIssueDetail.intValue() > 1) {					
				resultMap.put("idIssueDetail", idIssueDetail);
			}	
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(IssuerequestDao.class, "updateIssueStatus() ->  - unable to update status - DataAccessException : " + dae.getMessage());		
			
		}
		return resultMap;
	}
	
	public Map<String,Object> getAttachmentByIssue(Integer IssueId) {
		
		Map<String,Object> attachment = new HashMap<String, Object>();
		
		try {			
			attachment = jdbcTemplate.queryForMap(ATTACHMENT_BYISSUE_QUERY, new Object[] {IssueId});
		
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getAttachment() -> no attachment for this issue : " + IssueId + " Exception : DataAccessException - " + dae.getMessage());
			
		}
		
		return attachment;
		
	}
	
	public List<Map<String, Object>> getIssuesByPerson(Integer personId) {
		
		List<Map<String, Object>> propertiesissues = new ArrayList<Map<String, Object>>();
		
		// issue created fetch
		Map<String, Object> propertyissues = new HashMap<String, Object>();		
		try {
			Map<String, Object> PropertyInformation = jdbcTemplate.queryForMap(PROPERTY_BYCREATEDPERSON_QUERY, new Object[] {personId});			 
			propertyissues.put("PropertyInformation", PropertyInformation);				
			List<Map<String, Object>> Issues = jdbcTemplate.queryForList(ISSUES_BYCREATEDPERSON_QUERY, new Object[] {personId});			
			for(Map<String, Object> issue : Issues) {
				
				Map<String,Object> attachment = getAttachmentByIssue(Integer.parseInt(issue.get("idIssue").toString()));
				if(!attachment.isEmpty()) {
					issue.put("AttachmentType", attachment.get("AttachmentType"));
				}
				
				Map<String, Object> status = getStatusByIssue(Integer.parseInt(issue.get("idIssue").toString()));
				issue.put("idIssueStatus", status.get("idIssueStatus"));
				issue.put("IssueStatus", status.get("IssueStatus"));
				
				Map<String, Object> assigned = getAssignedPersonForIssue(Integer.parseInt(issue.get("idIssue").toString()));
				if(!assigned.isEmpty()) {
					issue.put("AssignedPerson", assigned.get("idServiceProvider"));
				}
								
			}
			propertyissues.put("Issues", Issues);		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (issue created fetch) DataAccessException - " + dae.getMessage());			
		}		
		if(!propertyissues.isEmpty()) {
			propertiesissues.add(propertyissues);
		}
		
		// issue assigned fetch				
		try {			
			List<Map<String, Object>> issues = jdbcTemplate.queryForList(ISSUES_BYSERVICEPROVIDERPERSON_QUERY, new Object[] {personId});
			Set<Integer> issueIds = new HashSet<Integer>();
			Set<Integer> issuePropIds = new HashSet<>();
			
			for(Map<String, Object> issue : issues) {
				
				Integer IssueId = Integer.parseInt(issue.get("Issue_idIssue").toString());
				Integer PropInfoId = Integer.parseInt(issue.get("PropertyInformation_idPropertyInformation").toString());
				Integer ServiceProviderId = jdbcTemplate.queryForObject(LIVESERVICEPROVIDER_FORISSUE_QUERY, new Object[] {IssueId}, Integer.class);				
				
				if(ServiceProviderId == Integer.parseInt(issue.get("idServiceProvider").toString())) {
					issueIds.add(IssueId);
					issuePropIds.add(PropInfoId);						
				}				
			}
			
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (issue assigned fetch) - issuePropIds - " + issuePropIds);	
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (issue assigned fetch) - issueIds - " + issueIds);	
			
			//issuePropIds - [1, 3]
			//issueIds - [72, 69, 73]
			
			// where in query get distinct prop info
			for(Integer issuePropId : issuePropIds) {			
				Map<String, Object> propertyissues1 = new HashMap<String, Object>();
				Map<String, Object> PropertyInformation = jdbcTemplate.queryForMap(PROPERTY_BYASSIGNEDPERSON_QUERY, new Object[] {issuePropId});								
				propertyissues1.put("PropertyInformation", PropertyInformation);
				
				
				MapSqlParameterSource parameters = new MapSqlParameterSource();
				parameters.addValue("ids", issueIds);
				parameters.addValue("issuepropid", issuePropId);
				
				List<Map<String, Object>> Issues = namedParameterJdbcTemplate.queryForList(ISSUES_BYASSIGNEDPERSON_QUERY, parameters);
				for(Map<String, Object> issue : Issues) {
					Map<String,Object> attachment = getAttachmentByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!attachment.isEmpty()) {
						issue.put("AttachmentType", attachment.get("AttachmentType"));
					}
					
					Map<String, Object> status = getStatusByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					issue.put("idIssueStatus", status.get("idIssueStatus"));
					issue.put("IssueStatus", status.get("IssueStatus"));
					
					Map<String, Object> assigned = getAssignedPersonForIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!assigned.isEmpty()) {
						issue.put("AssignedPerson", assigned.get("idServiceProvider"));
					}
				}
				propertyissues1.put("Issues", Issues);				
				propertiesissues.add(propertyissues1);				
			}			
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (issue assigned fetch) DataAccessException - " + dae.getMessage());			
		}		
		
		//property info created fetch
		try {			
			List<Map<String, Object>> PropertyInformations = jdbcTemplate.queryForList(PROPERTIES_BYOWNERPERSON_QUERY, new Object[] {personId});			
			for (Map<String, Object> PropertyInformation : PropertyInformations) {				
				Map<String, Object> propertyissues1 = new HashMap<String, Object>();				
				propertyissues1.put("PropertyInformation", PropertyInformation);				
				Integer propertyInformationId = Integer.parseInt(PropertyInformation.get("idPropertyInformation").toString());				
				List<Map<String, Object>> Issues = jdbcTemplate.queryForList(ISSUES_BYOWNERPERSON_QUERY, new Object[] {personId, propertyInformationId});
				for(Map<String, Object> issue : Issues) {
					Map<String,Object> attachment = getAttachmentByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!attachment.isEmpty()) {
						issue.put("AttachmentType", attachment.get("AttachmentType"));
					}
					
					Map<String, Object> status = getStatusByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					issue.put("idIssueStatus", status.get("idIssueStatus"));
					issue.put("IssueStatus", status.get("IssueStatus"));
					
					Map<String, Object> assigned = getAssignedPersonForIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!assigned.isEmpty()) {
						issue.put("AssignedPerson", assigned.get("idServiceProvider"));
					}
				}				
				propertyissues1.put("Issues", Issues);				
				propertiesissues.add(propertyissues1);
			}		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (property info created fetch) DataAccessException - " + dae.getMessage());			
		}
		
		//property info person fetch
		try {			
			List<Map<String, Object>> PropertyInformations = jdbcTemplate.queryForList(PROPERTIES_BYPROPINFOPERSON_QUERY, new Object[] {personId, personId});			
			for (Map<String, Object> PropertyInformation : PropertyInformations) {				
				Map<String, Object> propertyissues1 = new HashMap<String, Object>();				
				propertyissues1.put("PropertyInformation", PropertyInformation);				
				Integer propertyInformationId = Integer.parseInt(PropertyInformation.get("idPropertyInformation").toString());				
				List<Map<String, Object>> Issues = jdbcTemplate.queryForList(ISSUES_BYPROPINFOPERSON_QUERY, new Object[] {personId, personId, propertyInformationId});
				for(Map<String, Object> issue : Issues) {
					Map<String,Object> attachment = getAttachmentByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!attachment.isEmpty()) {
						issue.put("AttachmentType", attachment.get("AttachmentType"));
					}
					
					Map<String, Object> status = getStatusByIssue(Integer.parseInt(issue.get("idIssue").toString()));
					issue.put("idIssueStatus", status.get("idIssueStatus"));
					issue.put("IssueStatus", status.get("IssueStatus"));
					
					Map<String, Object> assigned = getAssignedPersonForIssue(Integer.parseInt(issue.get("idIssue").toString()));
					if(!assigned.isEmpty()) {
						issue.put("AssignedPerson", assigned.get("idServiceProvider"));
					}
				}				
				propertyissues1.put("Issues", Issues);				
				propertiesissues.add(propertyissues1);
			}		
		} catch (DataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> (property info person fetch) DataAccessException - " + dae.getMessage());			
		}
		
		primeRealtyLogger.debug(IssuerequestDao.class, "getIssuesByPerson() -> propertiesissues : " + propertiesissues);
				
		return propertiesissues;
	}
	
	public Map<String, Object> getStatusByIssue(Integer issueId) {
		
		Map<String, Object> status = new HashMap<String, Object>();		
		try {			
			status = jdbcTemplate.queryForMap(STATUS_FORISSUE_QUERY, new Object[] {issueId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getStatusByIssue() -> Exception - " + dae.getMessage());
			
		}
		return status;
	}
	
	public Map<String, Object> getAssignedPersonForIssue(Integer issueId) {
		
		Map<String, Object> assignedPerson = new HashMap<String, Object>();		
		try {			
			assignedPerson = jdbcTemplate.queryForMap(ASSIGNEDPERSON_FORISSUE_QUERY, new Object[] {issueId, issueId});
			
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getAssignedPersonForIssue() -> Exception - " + dae.getMessage());
			
		}
		return assignedPerson;
	}
	
	public List<Map<String, Object>> getServiceProviders() {
		
		return jdbcTemplate.queryForList(SERVICE_PROVIDERS_LIST_QUERY);
		
	}	
		
	public Map<String, Object> assignPersonForIssue(Integer PersonId, Integer IssueId, Integer ServiceProviderId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {			
			Map<String, Object> spIssueParams = new HashMap<String, Object>();
			spIssueParams.put("Issue_idIssue", IssueId);
			spIssueParams.put("ServiceProvider_idServiceProvider", ServiceProviderId);
			spIssueParams.put("AssignBy_idPerson", PersonId);
			spIssueParams.put("AssignDate", new Date());			

			Number idIssueDetail = primeRealtyDao.tableInsert("ServiceProviderIssue",spIssueParams);
			
			if(idIssueDetail.intValue() > 1) {					
				resultMap.put("idIssueDetail", idIssueDetail);
			}			
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(IssuerequestDao.class, "assignPersonForIssue() ->  - unable to assign person - DataAccessException : " + dae.getMessage());				
		}
		
		return resultMap;
	}

	public List<Map<String, Object>> getIssueCategories() {
		
		return jdbcTemplate.queryForList(ISSUECATEGORY_QUERY);
		
	}

	public Map<String, Object> createIssue(Issue issue) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		Number idAttachment = 0;
		
		try {
			
			// create issue
			Map<String, Object> issueParams = new HashMap<String, Object>();
			
			if(issue.getAttachment() != null) {				
				Attachment attachment = issue.getAttachment();				
				idAttachment = createAttachment(attachment);									
				if(idAttachment.intValue() > 0) {
					issueParams.put("Attachment_idAttachment", idAttachment);										
				} 
			}
			
			issueParams.put("Issue", issue.getIssue());
			issueParams.put("IssueDescription", issue.getIssueDescription());				
			issueParams.put("IssueCategory_idIssueCategory", issue.getIssueCategoryId());
			issueParams.put("CreatePerson_idPerson", issue.getPersonId());
			issueParams.put("PropertyInformation_idPropertyInformation", issue.getPropertyInformationId());
			issueParams.put("CreatedDate", new Date());
			
			Number idIssue = primeRealtyDao.tableInsert("Issue",issueParams);
			
			if(idIssue.intValue() > 0) {
				resultMap.put("idIssue", idIssue);			
				
				// create issue detail
				Map<String, Object> issueDetailParams = new HashMap<String, Object>();
				issueDetailParams.put("Issue_idIssue", idIssue);
				issueDetailParams.put("Status_idIssueStatus", 1); // new issue (Initiated status hardcoded)
				issueDetailParams.put("StatusUpdate_idPerson", issue.getPersonId());
				issueDetailParams.put("StatusUpdateDate", new Date());
				
				Number idIssueDetail = primeRealtyDao.tableInsert("IssueDetail",issueDetailParams);
				
				if(idIssueDetail.intValue() > 0) {
					resultMap.put("idIssueDetail", idIssueDetail);			
					
				} else {
					// rollback attachment if it is there
					jdbcTemplate.update(DELETE_ATTACHMENT, new Object[]{idAttachment});
					
					//rollback issue if it is there
					jdbcTemplate.update(DELETE_ISSUE, new Object[]{idIssue});
					
				}				
				
			} else {
				// rollback attachment if it is there
				jdbcTemplate.update(DELETE_ATTACHMENT, new Object[]{idAttachment});
			}
		
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to create issue request - due to server issue");
			primeRealtyLogger.error(IssuerequestDao.class, "createIssue() -> Exception : DataAccessException - " + dae.getMessage());			
		}
				
		return resultMap;
	}
	
	public Map<String,Object> getAttachmentByIssueLog(Integer IssueLogId) {
		
		Map<String,Object> attachment = new HashMap<String, Object>();
		
		try {			
			attachment = jdbcTemplate.queryForMap(ATTACHMENT_BYISSUELOG_QUERY, new Object[] {IssueLogId});
		
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getAttachment() -> no attachment for this issuelog : " + IssueLogId + " Exception : DataAccessException - " + dae.getMessage());
			
		}
		
		return attachment;
		
	}
	
	public List<Map<String, Object>> getIssueLogsByIssue(Integer IssueId) {
		
		List<Map<String, Object>> IssueLogs = new ArrayList<Map<String, Object>>();
		
		IssueLogs = jdbcTemplate.queryForList(ISSUELOG_QUERY, new Object[] {IssueId});
		
		for(Map<String, Object> IssueLog : IssueLogs) {
			Map<String,Object> Attachment = getAttachmentByIssueLog(Integer.parseInt(IssueLog.get("idIssueLog").toString()));
			if(!Attachment.isEmpty()) {
				IssueLog.put("AttachmentType", Attachment.get("AttachmentType"));
			}
		}
		
		return IssueLogs;
		
	}

	public Map<String, Object> createIssueLog(IssueLog issueLog) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		Number idAttachment = 0;
		
		try {
			
			Map<String, Object> issuelogParams = new HashMap<String, Object>();
			
			if(issueLog.getAttachment() != null) {				
				Attachment attachment = issueLog.getAttachment();				
				idAttachment = createAttachment(attachment);									
				if(idAttachment.intValue() > 0) {
					issuelogParams.put("Attachment_idAttachment", idAttachment);										
				} 
			}
			
			issuelogParams.put("Log", issueLog.getLog());
			issuelogParams.put("Issue_idIssue", issueLog.getIssueId());
			issuelogParams.put("CreatePerson_idPerson", issueLog.getPersonId());
			issuelogParams.put("CreatedDate", new Date());
			
			Number idIssueLog = primeRealtyDao.tableInsert("IssueLog",issuelogParams);
			
			if(idIssueLog.intValue() > 0) {
				resultMap.put("idIssueLog", idIssueLog);	
				
			} else if (idAttachment.intValue() > 0) {
				// rollback attachment if it is there
				jdbcTemplate.update(DELETE_ATTACHMENT, new Object[]{idAttachment});
			}
		
		} catch (DataAccessException dae) {			
			resultMap.put("message", "unable to create issue log - due to server issue");
			primeRealtyLogger.error(IssuerequestDao.class, "createIssueLog() -> Exception : DataAccessException - " + dae.getMessage());			
		}
		
		return resultMap;
	}
	
	private Number createAttachment(Attachment attachment) {
		
		Number idAttachment = 0;
		
		byte[] AttachBA = attachment.getAttachment();
		int AttachBALen = AttachBA.length;
		final InputStream attachIS = new ByteArrayInputStream(AttachBA);
		
		try {
			
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
			    @Override
			    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			        PreparedStatement statement = con.prepareStatement("INSERT INTO Attachment (Attachment, AttachmentName, AttachmentType) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			        statement.setBlob(1, attachIS, AttachBALen);
			        statement.setString(2, attachment.getAttachmentName());
			        statement.setString(3, attachment.getAttachmentType());
			        return statement;
			    }
			}, holder);
	
			idAttachment = holder.getKey();
			
		} catch (DataAccessException dae) {			
			primeRealtyLogger.error(IssuerequestDao.class, "createAttachment() -> Exception : DataAccessException - " + dae.getMessage());
			
		}		
		
		return idAttachment;
	}
	
	// email issue request
	public String getSenderEmailByPerson(Integer PersonId) {
		
		String EmailAddress = null;
		// see if the sender email is in PropertyInformationPerson table
		try {
			EmailAddress = jdbcTemplate.queryForObject(EMAIL_TO_PROPINFOPERSON_BYPERSON_QUERY, new Object[] {PersonId, PersonId}, String.class);
		
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.debug(IssuerequestDao.class, "getSenderEmailByPerson() -> To Email Address Not Found in PropInfoPerson table -> EmptyResultDataAccessException - " + dae.getMessage());
			
		}
		
		if(EmailAddress == null) { // see if sender email is in PropertyInformation table
			try {
				EmailAddress = jdbcTemplate.queryForObject(EMAIL_TO_PROPINFO_BYPERSON_QUERY, new Object[] {PersonId}, String.class);
			
			} catch (EmptyResultDataAccessException dae) {
				primeRealtyLogger.debug(IssuerequestDao.class, "getSenderEmailByPerson() -> To Email Address Not Found in PropInfo table -> EmptyResultDataAccessException - " + dae.getMessage());
				
			}
			
		}
				
		return EmailAddress;
	}
	
	public String getIssueCategoryById(Integer IssueCategoryId) {
		
		String IssueCategory = "";
				
		try {
			IssueCategory = jdbcTemplate.queryForObject(ISSUECATEGORY_BYID_QUERY, new Object[] {IssueCategoryId}, String.class);
					
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(IssuerequestDao.class, "getIssueCategoryById() -> EmptyResultDataAccessException - " + dae.getMessage());
			
		}
		
		return IssueCategory;
	}
	
	public Issue getIssueById(Integer IssueId) {
		
		Issue issue = new Issue();
				
		try {
			Map<String, Object> result = jdbcTemplate.queryForMap(ISSUE_BYID_QUERY, new Object[] {IssueId});
			
			issue.setIssue(result.get("Issue") != null ? result.get("Issue").toString() : null);
			issue.setIssueDescription(result.get("IssueDescription") != null ? result.get("IssueDescription").toString() : null);
			issue.setIssueCategoryId(result.get("IssueCategory_idIssueCategory") != null ? Integer.parseInt(result.get("IssueCategory_idIssueCategory").toString()) : 0);
					
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(IssuerequestDao.class, "getIssueById() -> EmptyResultDataAccessException - " + dae.getMessage());
			
		}
		
		return issue;
	}
	
	
	public Integer getPersonIdByServiceProviderId(Integer ServiceProviderId) {
		
		Integer PersonId = 0;
				
		try {
			PersonId = jdbcTemplate.queryForObject(PERSONID_BYSERVICEPROVIDERID_QUERY, new Object[] {ServiceProviderId}, Integer.class);
					
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(IssuerequestDao.class, "getPersonIdByServiceProviderId() -> EmptyResultDataAccessException - " + dae.getMessage());
			
		}
		
		return PersonId;
	}
			
	private static String ISSUECATEGORY_QUERY = "SELECT idIssueCategory, IssueCategory FROM IssueCategory";
	private static String ISSUESTATUS_QUERY = "SELECT idIssueStatus, IssueStatus FROM IssueStatus";
	private static String PROPINFOS_BYIDS_QUERY = "SELECT PropertyInformation.idPropertyInformation as PropertyInformationId, PropertyInformation.SquareFeet as SquareFeet, PropertyInformation.Beds as Beds, PropertyInformation.Baths as Baths, PropertyInformation.Stories as Stories, PropertyInformation.Lotsize as Lotsize, PropertyInformation.YearBuilt as YearBuilt, PropertyInformation.YearRenovated as YearRenovated, PropertyInformation.APN as APN, PropertyInformation.Community as Community, PropertyInformation.HOADues as HOADues, PropertyType.PropertyType as PropertyType, Address.Address1 as Address1, Address.Address2 as Address2, Address.Address3 as Address3, Address.Zip as Zip, Address.CityName as CityName, Address.StateCode as StateCode, Address.CountryCode as CountryCode FROM PropertyInformation, PropertyType, Address WHERE idPropertyInformation IN (:ids) AND PropertyType.idPropertyType = PropertyInformation.PropertyType_idPropertyType AND Address.idAddress = PropertyInformation.Address_idAddress";
	private static String EMAIL_TO_PROPINFOPERSON_BYPERSON_QUERY = "SELECT Person.User_EmailAddress FROM PropertyInformationPerson, Person, PersonType WHERE PropertyInformation_idPropertyInformation = (SELECT PropertyInformation_idPropertyInformation FROM `PropertyInformationPerson` WHERE Person_idPerson = ?) AND Person_idPerson != ? AND PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.PersonType_idPersonType = PersonType.idPersonType AND PersonType.idPersonType = 3";
	private static String EMAIL_TO_PROPINFO_BYPERSON_QUERY = "SELECT Person.User_EmailAddress FROM Person, PropertyInformation, PropertyInformationPerson WHERE PropertyInformationPerson.Person_idPerson = ? AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.OwnerPerson_idPerson = Person.idPerson";
	private static String ISSUECATEGORY_BYID_QUERY = "SELECT IssueCategory FROM IssueCategory WHERE idIssueCategory = ?";
	private static String ISSUE_BYID_QUERY = "SELECT Issue, IssueDescription, IssueCategory_idIssueCategory FROM Issue WHERE idIssue = ?";
	private static String PROPERTY_BYCREATEDPERSON_QUERY = "SELECT DISTINCT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.YearBuilt, Address.Address1, Address.Address2, Address.CityName, Address.StateCode FROM Issue, PropertyInformation, Address WHERE Issue.CreatePerson_idPerson = ? AND Issue.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.Address_idAddress = Address.idAddress";
	private static String ISSUES_BYCREATEDPERSON_QUERY = "SELECT Issue.idIssue as idIssue, Issue.Issue as Issue, Issue.IssueDescription as IssueDescription, IssueCategory.IssueCategory as IssueCategory FROM Issue, IssueCategory WHERE Issue.IssueCategory_idIssueCategory = IssueCategory.idIssueCategory AND Issue.CreatePerson_idPerson = ?";
	private static String ISSUES_BYSERVICEPROVIDERPERSON_QUERY = "SELECT ServiceProvider.idServiceProvider, ServiceProviderIssue.Issue_idIssue, Issue.PropertyInformation_idPropertyInformation FROM ServiceProviderIssue, ServiceProvider, Issue WHERE ServiceProviderIssue.ServiceProvider_idServiceProvider = ServiceProvider.idServiceProvider AND ServiceProviderIssue.Issue_idIssue = Issue.idIssue AND ServiceProvider.Person_idPerson = ?";
	private static String LIVESERVICEPROVIDER_FORISSUE_QUERY = "SELECT ServiceProvider_idServiceProvider FROM ServiceProviderIssue WHERE Issue_idIssue = ? ORDER BY AssignDate DESC LIMIT 1";
	private static String PROPERTY_BYASSIGNEDPERSON_QUERY="SELECT DISTINCT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.YearBuilt, Address.Address1, Address.Address2, Address.CityName, Address.StateCode FROM Issue, PropertyInformation, Address WHERE Issue.PropertyInformation_idPropertyInformation = ? AND Issue.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.Address_idAddress = Address.idAddress";
	private static String ISSUES_BYASSIGNEDPERSON_QUERY="SELECT Issue.idIssue as idIssue, Issue.Issue as Issue, Issue.IssueDescription as IssueDescription, IssueCategory.IssueCategory as IssueCategory FROM Issue, IssueCategory WHERE Issue.idIssue IN (:ids) AND Issue.IssueCategory_idIssueCategory = IssueCategory.idIssueCategory AND Issue.PropertyInformation_idPropertyInformation = :issuepropid";
	private static String PROPERTIES_BYOWNERPERSON_QUERY = "SELECT DISTINCT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.YearBuilt, Address.Address1, Address.Address2, Address.CityName, Address.StateCode FROM PropertyInformation, PropertyInformationPerson, Issue, Address WHERE PropertyInformation.OwnerPerson_idPerson = ? AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PropertyInformationPerson.Person_idPerson = Issue.CreatePerson_idPerson AND Issue.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.Address_idAddress = Address.idAddress";
	private static String ISSUES_BYOWNERPERSON_QUERY = "SELECT Issue.idIssue as idIssue, Issue.Issue as Issue, Issue.IssueDescription as IssueDescription, IssueCategory.IssueCategory as IssueCategory FROM PropertyInformation, PropertyInformationPerson, Issue, IssueCategory WHERE PropertyInformation.OwnerPerson_idPerson = ? AND PropertyInformationPerson.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP)) AND PropertyInformationPerson.Person_idPerson = Issue.CreatePerson_idPerson AND Issue.IssueCategory_idIssueCategory = IssueCategory.idIssueCategory AND Issue.PropertyInformation_idPropertyInformation = ?";
	private static String PROPERTIES_BYPROPINFOPERSON_QUERY = "SELECT DISTINCT PropertyInformation.idPropertyInformation, PropertyInformation.SquareFeet, PropertyInformation.YearBuilt, Address.Address1, Address.Address2, Address.CityName, Address.StateCode FROM Issue, PropertyInformation, Address WHERE Issue.CreatePerson_idPerson IN (SELECT Person_idPerson FROM PropertyInformationPerson WHERE PropertyInformation_idPropertyInformation IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationPerson WHERE Person_idPerson = ?) AND Person_idPerson != ? AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP))) AND Issue.PropertyInformation_idPropertyInformation = PropertyInformation.idPropertyInformation AND PropertyInformation.Address_idAddress = Address.idAddress";
	private static String ISSUES_BYPROPINFOPERSON_QUERY = "SELECT Issue.idIssue as idIssue, Issue.Issue as Issue, Issue.IssueDescription as IssueDescription, IssueCategory.IssueCategory as IssueCategory FROM Issue, IssueCategory WHERE Issue.CreatePerson_idPerson IN (SELECT Person_idPerson FROM PropertyInformationPerson WHERE PropertyInformation_idPropertyInformation IN (SELECT PropertyInformation_idPropertyInformation FROM PropertyInformationPerson WHERE Person_idPerson = ?) AND Person_idPerson != ? AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP))) AND Issue.IssueCategory_idIssueCategory = IssueCategory.idIssueCategory AND Issue.PropertyInformation_idPropertyInformation = ?";
	private static String ATTACHMENT_BYISSUE_QUERY = "SELECT Attachment, AttachmentName, AttachmentType FROM Issue, Attachment WHERE Issue.Attachment_idAttachment = Attachment.idAttachment AND Issue.idIssue = ?";
	private static String ISSUELOG_QUERY = "SELECT IssueLog.idIssueLog, IssueLog.Log, User.FirstName, User.LastName FROM IssueLog, Person, User WHERE IssueLog.Issue_idIssue = ? AND IssueLog.CreatePerson_idPerson = Person.idPerson AND Person.User_EmailAddress = User.EmailAddress";
	private static String STATUS_FORISSUE_QUERY = "SELECT IssueStatus.idIssueStatus as idIssueStatus, IssueStatus.IssueStatus as IssueStatus FROM IssueDetail, IssueStatus WHERE Issue_idIssue = ? AND IssueDetail.Status_idIssueStatus = IssueStatus.idIssueStatus AND IssueDetail.StatusUpdateDate ORDER BY IssueDetail.StatusUpdateDate DESC LIMIT 1";
	private static String ASSIGNEDPERSON_FORISSUE_QUERY = "SELECT ServiceProvider.idServiceProvider as idServiceProvider FROM ServiceProviderIssue, ServiceProvider, Person, User WHERE ServiceProviderIssue.ServiceProvider_idServiceProvider = ServiceProvider.idServiceProvider AND ServiceProvider.Person_idPerson = Person.idPerson AND Person.User_EmailAddress = User.EmailAddress AND ServiceProviderIssue.Issue_idIssue = ? AND ServiceProviderIssue.AssignDate >= (SELECT AssignDate FROM ServiceProviderIssue WHERE Issue_idIssue = ? ORDER BY AssignDate DESC LIMIT 1)";
	private static String SERVICE_PROVIDERS_LIST_QUERY = "SELECT ServiceProvider.idServiceProvider as idServiceProvider, User.FirstName as FirstName, User.LastName as LastName, ServiceProvider.YearsOfExperience as YearsOfExperience, ServiceProvider.AreasOfExpertise as AreasOfExpertise, ServiceProvider.AreaCoverage as AreaCoverage FROM ServiceProvider, Person, User, PersonType WHERE ServiceProvider.Person_idPerson = Person.idPerson AND Person.User_EmailAddress = User.EmailAddress AND Person.PersonType_idPersonType = PersonType.idPersonType AND PersonType.idPersonType = 5";
	private static String ATTACHMENT_BYISSUELOG_QUERY = "SELECT Attachment, AttachmentName, AttachmentType FROM IssueLog, Attachment WHERE IssueLog.Attachment_idAttachment = Attachment.idAttachment AND IssueLog.idIssueLog = ?";
	private static String PERSONID_BYSERVICEPROVIDERID_QUERY = "SELECT Person.idPerson FROM Person, ServiceProvider WHERE ServiceProvider.Person_idPerson = Person.idPerson AND ServiceProvider.idServiceProvider = ?";
	private static String DELETE_ISSUE = "DELETE FROM Issue WHERE idIssue = ?";
	private static String DELETE_ATTACHMENT = "DELETE FROM Attachment WHERE idAttachment = ?";
	
}
