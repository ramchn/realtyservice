package com.tamkosoft.primerealty.signup;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyDao;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.Person;

@Component("signupDao")
public class SignupDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	PrimeRealtyDao primeRealtyDao;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
		
	public List<Map<String, Object>> getPersonTypes() {
		  
		return jdbcTemplate.queryForList(PERSONTYPE_QUERY);
	}
	
	public List<Map<String, Object>> getGenders() {
		  
		return jdbcTemplate.queryForList(GENDER_QUERY);
	}
	
	public Map<String, Object> createPropInfoPerson(Number PersonId, Number PropertyInformaitonId, Number UpdateByPerson) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {			
			Map<String, Object> PropInfoPersonParams = new HashMap<String, Object>();
			PropInfoPersonParams.put("Person_idPerson", PersonId);
			PropInfoPersonParams.put("PropertyInformation_idPropertyInformation", PropertyInformaitonId);	
			PropInfoPersonParams.put("UpdateBy_idPerson", UpdateByPerson);
			PropInfoPersonParams.put("UpdateDate", new Date());
			
			
			Number idPropInfoPerson = primeRealtyDao.tableInsert("PropertyInformationPerson",PropInfoPersonParams);
			
			if(idPropInfoPerson.intValue() > 0) {
				resultMap.put("idPropertyInformationPerson", idPropInfoPerson);
				
			} else {								
				// rollback Email record
				jdbcTemplate.update(DELETE_EMAIL, new Object[]{PersonId});
				// get email address before deleting person record
				String EmailAddress = jdbcTemplate.queryForObject(EMAILADDRESS_BYPERSON_QUERY, new Object[] {PersonId}, String.class);
				// rollback Person insert 
				jdbcTemplate.update(DELETE_PERSON, new Object[]{PersonId});
				// rollback User insert
				jdbcTemplate.update(DELETE_USER, new Object[]{EmailAddress});
			}
			
		} catch(DataAccessException dae) {			
			primeRealtyLogger.error(SignupDao.class, "createPropInfoPerson() -> unable to create PropertyInfoPerson : DataAccessException - " + dae.getMessage());
		}
		
		return resultMap;
	}

	public Map<String, Object> createPerson(Person signupPersonPojo, String VerificationToken) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();			
		Number userCreated = 0;
		Number idPerson = 0;
		Number idEmail = 0;
		Map<String, Object> userParams = new HashMap<String, Object>();
		Map<String, Object> personParams = new HashMap<String, Object>();
		Map<String, Object> emailParams = new HashMap<String, Object>();
		
		try {
			userParams.put("EmailAddress", signupPersonPojo.getEmailAddress());
			userParams.put("UserPassword", signupPersonPojo.getUserPassword()!= null ? new String(Base64.getEncoder().encode(signupPersonPojo.getUserPassword().getBytes())) : new String(Base64.getEncoder().encode("password".getBytes())));
			userParams.put("FirstName", signupPersonPojo.getFirstName());
			userParams.put("LastName", signupPersonPojo.getLastName());
			userParams.put("MiddleName", signupPersonPojo.getMiddleName());
			userParams.put("Gender_idGender", signupPersonPojo.getGenderId());	 	
			userParams.put("VerificationToken", VerificationToken);
			
			// create user
			userCreated = createUser("User", userParams, resultMap);	  
			
			if (userCreated.intValue() == 1) {
						
				personParams.put("PersonType_idPersonType", signupPersonPojo.getPersonTypeId());
				personParams.put("User_EmailAddress", signupPersonPojo.getEmailAddress());
				personParams.put("CreatedDate", new Date());
				idPerson = primeRealtyDao.tableInsert("Person",personParams);
				
				if (idPerson.intValue() > 0) {		
					resultMap.put("idPerson", idPerson);
					
					emailParams.put("Email", signupPersonPojo.getEmailAddress());
					emailParams.put("EmailType_idEmailType", 1); // email type is login
					emailParams.put("Person_idPerson", idPerson);
					idEmail = primeRealtyDao.tableInsert("Email",emailParams);
					
					if (idEmail.intValue() > 0) {						
						resultMap.put("idEmail", idEmail);		
												
					} else {
						// rollback Person insert 
						jdbcTemplate.update(DELETE_PERSON, new Object[]{idPerson});
						// rollback User insert
						jdbcTemplate.update(DELETE_USER, new Object[]{signupPersonPojo.getEmailAddress()});
					}
				
				} else {
					// rollback User insert
					jdbcTemplate.update(DELETE_USER, new Object[]{signupPersonPojo.getEmailAddress()});
				}
			} 
			
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(SignupDao.class, "createPerson() ->  - unable to create Person - DataAccessException : " + dae.getMessage());		
			
		}
		return resultMap;
	}
	
	
	public Map<String, Object> verifyUserAndCreateAccess(String token) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			resultMap = jdbcTemplate.queryForMap(USER_BYTOKEN_QUERY, token);
			
			String EmailAddress = resultMap.get("EmailAddress").toString();
			Object Enabled = resultMap.get("Enabled");
	  
			if (Enabled == null) {
				Object[] updateParams = { 1, EmailAddress};
				Number updated  = jdbcTemplate.update(USER_ENABLED_UPDATE, updateParams);
				
				if (updated.intValue() == 1) {
					String PersonType = jdbcTemplate.queryForObject(PERSONTYPE_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
					Map<String, Object> accessParams = new HashMap<String, Object>();		  
					accessParams.put("Authority", "ROLE_"+ PersonType.toUpperCase());
					accessParams.put("User_EmailAddress", EmailAddress);
					Number idAccess = primeRealtyDao.tableInsert("Access", accessParams);
					
					if (idAccess.intValue() > 0) {						
						resultMap.put("idAccess", idAccess);		
												
					} else {
						// rollback User update
						Object[] updateParams1 = { null, EmailAddress};
						jdbcTemplate.update(USER_ENABLED_UPDATE, updateParams1);
					}
					
				} else {
					resultMap.put("message", "user ("+EmailAddress+") verification failed");					
				}
				
			} else {
				resultMap.put("message", "user ("+EmailAddress+") already verified");
			}
			
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("message", "user not exist, verification failed");
			  
		} catch (DataAccessException dae) {
			primeRealtyLogger.error(SignupDao.class, "verifyUserAndCreateAccess() ->  - unable to verify user - DataAccessException : " + dae.getMessage());		
			
		}
		
		return resultMap;
		
	}
	
	public Map<String, Object> loginUser(String EmailAddress, String UserPassword) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		  
		try {	  
			Object[] params = {EmailAddress, UserPassword != null ? new String(Base64.getEncoder().encode(UserPassword.getBytes())) : null};				
			Map<String, Object> columns = jdbcTemplate.queryForMap(LOGIN_QUERY, params);			
			
			String FirstName = columns.get("FirstName") != null ? columns.get("FirstName").toString() : "";
			String LastName = columns.get("LastName") != null ? columns.get("LastName").toString() : "";
			
			resultMap.put("FirstName", FirstName);
			resultMap.put("LastName", LastName);			
					
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(SignupDao.class, "loginUser() ->  - username/password incorrect - EmptyResultDataAccessException : " + dae.getMessage());			
			resultMap.put("Authenticated", false);
			return resultMap;
		}	
		
		try {
			String Authority = jdbcTemplate.queryForObject(ACCESS_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
			Number idPerson = jdbcTemplate.queryForObject(PERSONID_BYUSER_QUERY, new Object[] {EmailAddress}, Integer.class);
			
			resultMap.put("Authority", Authority);
			resultMap.put("idPerson", idPerson);			
						
		} catch (EmptyResultDataAccessException dae) {
			primeRealtyLogger.error(SignupDao.class, "loginUser() -> unable to fetch authority or person id or property ids - EmptyResultDataAccessException : " + dae.getMessage());			
			resultMap.put("Authenticated", false);
			return resultMap;
		}	
		
		List<Map<String, Object>> PropertyInformationPersonIds = new ArrayList<Map<String, Object>>();				
		PropertyInformationPersonIds = jdbcTemplate.queryForList(PROPERTYINFOPERSON_EXIST_QUERY, new Object[] {EmailAddress});

		// check if the user exist in PropertyInformationPerson
		if(PropertyInformationPersonIds.size() > 0) {
			// yes, user exist in PropertyInformationPerson
			// so, the user is either a tenant (assigned by a owner/property manager or property manager) or a project manager (assigned by a owner or company owner)} and
			// trying to sign in, so need to check either (start date > current date and no end date) or (start date > current date and end data < current date)
			// start date and end date set by owner/company owner/property manager						
			PropertyInformationPersonIds = jdbcTemplate.queryForList(PROPERTYINFOPERSON_ACTIVE_QUERY, new Object[] {EmailAddress});
			
			if(PropertyInformationPersonIds.size() > 0) {
				// yes, user has valid start date and end date				
				resultMap.put("Authenticated", true);		
			} else {
				// no, user is exist in PropertyInformationPerson but don't have valid start date and end date
				resultMap.put("Authenticated", false);
			}
			  
		} else { 
			// user is neither a tenant (assigned by a owner/property manager or property manager) nor a project manager (assigned by a owner or company owner)}
			// so, no need to check a start or end date because the user wont exist in PropertyInformationPerson
			resultMap.put("Authenticated", true);
			
		} 
		
		return resultMap;
		
	}
	
	public Map<String, Object> getPassword(String EmailAddress) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String UserPassword = jdbcTemplate.queryForObject(PASSWORD_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
			resultMap.put("UserPassword", UserPassword);			
			
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("message", "Password not found for this email ("+ EmailAddress +"), please try with registered email address");
		  
		}
		
		return resultMap;
	}
	
	private Number createUser(String table, Map<String, Object> params, Map<String, Object> resultMap) {
		  
		Number userCreated = 0;
		String EmailAddress = params.get("EmailAddress").toString();
		  
		try {	  
			  jdbcTemplate.queryForObject(USER_QUERY, new Object[] {EmailAddress}, String.class);
			  resultMap.put("message", "user ("+ EmailAddress +") already exist : unable to create");
			  
		} catch (EmptyResultDataAccessException e) {
			  userCreated = new SimpleJdbcInsert(jdbcTemplate).withTableName("User").execute(params);
			  
		}	 		  
		return userCreated;
	}
		
	private static String USER_QUERY = "SELECT FirstName FROM User WHERE EmailAddress = ?";
	private static String PERSONTYPE_QUERY = "select idPersonType, PersonType from PersonType";
	private static String GENDER_QUERY = "select idGender, Gender from Gender";
	private static String USER_BYTOKEN_QUERY = "SELECT EmailAddress, Enabled FROM User WHERE VerificationToken = ?";
	private static String USER_ENABLED_UPDATE = "update User set Enabled = ? WHERE EmailAddress = ?";
	private static String PERSONTYPE_BYUSER_QUERY = "select PersonType from Person, PersonType where Person.PersonType_idPersonType = PersonType.idPersonType and Person.User_EmailAddress = ?";
	private static String LOGIN_QUERY = "SELECT FirstName, LastName FROM User WHERE EmailAddress = ? and UserPassword = ? and Enabled = 1";
	private static String PERSONID_BYUSER_QUERY = "SELECT idPerson FROM Person WHERE User_EmailAddress = ?";
	private static String ACCESS_BYUSER_QUERY = "SELECT Authority FROM Access WHERE User_EmailAddress = ?";
	private static String PASSWORD_BYUSER_QUERY = "SELECT UserPassword FROM User WHERE EmailAddress = ?";
	private static String PROPERTYINFOPERSON_EXIST_QUERY = "SELECT idPropertyInformationPerson FROM Person, PropertyInformationPerson WHERE PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.User_EmailAddress = ?";
	private static String PROPERTYINFOPERSON_ACTIVE_QUERY = "SELECT PropertyInformationPerson.idPropertyInformationPerson, PropertyInformationPerson.PropertyInformation_idPropertyInformation as idPropertyInformation FROM Person, PropertyInformationPerson WHERE PropertyInformationPerson.Person_idPerson = Person.idPerson AND Person.User_EmailAddress = ? AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP))";
	private static String EMAILADDRESS_BYPERSON_QUERY = "SELECT User_EmailAddress FROM Person WHERE idPerson = ?";
	private static String DELETE_PERSON = "DELETE FROM Person WHERE idPerson = ?";
	private static String DELETE_USER = "DELETE FROM User WHERE Email_Address = ?";
	private static String DELETE_EMAIL = "DELETE FROM Email WHERE Person_idPerson = ?";
	
}
