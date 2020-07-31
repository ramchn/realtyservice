package com.tamkosoft.primerealty.signup;

import java.sql.Types;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.pojo.Person;

@Component("signupDao")
public class SignupDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getPersonTypes() {
		  
		return jdbcTemplate.queryForList(PERSONTYPE_QUERY);
	}
	
	public List<Map<String, Object>> getGenders() {
		  
		return jdbcTemplate.queryForList(GENDER_QUERY);
	}
	
	public Map<String, Object> createPropInfoPerson(Number PersonId, Number PropertyInformaitonId) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pipParams = new HashMap<String, Object>();
		pipParams.put("Person_idPerson", PersonId);
		pipParams.put("PropertyInformation_idPropertyInformation", PropertyInformaitonId);	
		
		Number idPropertyInformationPerson = tableInsert("PropertyInformationPerson",pipParams);
		
		if(idPropertyInformationPerson.intValue() > 0) {
			resultMap.put("idPropertyInformationPerson", idPropertyInformationPerson);
			
		} else {								
			// rollback Email record
			// rollback Person record
			// rollback User record
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
		
		userParams.put("EmailAddress", signupPersonPojo.getEmailAddress());
		userParams.put("UserPassword", new String(Base64.getEncoder().encode(signupPersonPojo.getUserPassword().getBytes())));
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
			idPerson = tableInsert("Person",personParams);
			
			if (idPerson.intValue() > 0) {		
				resultMap.put("idPerson", idPerson);
				
				emailParams.put("Email", signupPersonPojo.getEmailAddress());
				emailParams.put("EmailType_idEmailType", 1); // email type is login
				emailParams.put("Person_idPerson", idPerson);
				idEmail = tableInsert("Email",emailParams);
				
				if (idEmail.intValue() > 0) {						
					resultMap.put("idEmail", idEmail);		
											
				} else {
					// rollback Person insert
					// rollback User insert
				}
			
			} else {
				// rollback User insert
			}
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
				int[] types = {Types.TINYINT, Types.VARCHAR};		  
				Number updated  = jdbcTemplate.update(USER_ENABLED_UPDATE, updateParams, types);
				
				if (updated.intValue() == 1) {
					String PersonType = jdbcTemplate.queryForObject(PERSONTYPE_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
					Map<String, Object> accessParams = new HashMap<String, Object>();		  
					accessParams.put("Authority", "ROLE_"+ PersonType.toUpperCase());
					accessParams.put("User_EmailAddress", EmailAddress);
					Number idAccess = tableInsert("Access", accessParams);
					
					if (idAccess.intValue() > 0) {						
						resultMap.put("idAccess", idAccess);		
												
					} else {
						// rollback User update
					}
					
				} else {
					resultMap.put("message", "user ("+EmailAddress+") verification failed");					
				}
				
			} else {
				resultMap.put("message", "user ("+EmailAddress+") already verified");
			}
			
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("message", "user not exist, verification failed");
			  
		}
		
		return resultMap;
		
	}
	
	public Map<String, Object> loginUser(String EmailAddress, String UserPassword) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		  
		try {	  
			
			Object[] params = {EmailAddress, UserPassword != null ? new String(Base64.getEncoder().encode(UserPassword.getBytes())) : null};
			Map<String, Object> columns = jdbcTemplate.queryForMap(LOGIN_QUERY, params);
			
			String FirstName = columns.get("FirstName").toString();
			String LastName = columns.get("LastName").toString();
			
			Number idPerson = jdbcTemplate.queryForObject(PERSON_BYUSER_QUERY, new Object[] {EmailAddress}, Integer.class);
			
			String Authority = jdbcTemplate.queryForObject(ACCESS_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
			
			String PersonType = jdbcTemplate.queryForObject(PERSONTYPE_BYPERSON_QUERY, new Object[] {idPerson}, String.class);
			
			if("Tenant".equals(PersonType)) { // allow tenant to signin only after set the start date by owner/pm and end date is less than current date (live tenant) 
				jdbcTemplate.queryForObject(ACTIVE_TENANT_QUERY, new Object[] {idPerson}, Integer.class);
			}
			
			resultMap.put("Authenticated", true);			
			resultMap.put("idPerson", idPerson);
			resultMap.put("Authority", Authority);
			resultMap.put("FirstName", FirstName);
			resultMap.put("LastName", LastName);
			
		  
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("Authenticated", false);
		  
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
	
	
	private Number tableInsert(String table, Map<String, Object> params) {
		  
		return new SimpleJdbcInsert(jdbcTemplate)
					.withTableName(table)
					.usingGeneratedKeyColumns("id" + table)
					.executeAndReturnKey(params);	
	}
	
	private static String USER_QUERY = "SELECT FirstName FROM User WHERE EmailAddress = ?";
	private static String PERSONTYPE_QUERY = "select idPersonType, PersonType from PersonType";
	private static String GENDER_QUERY = "select idGender, Gender from Gender";
	private static String USER_BYTOKEN_QUERY = "SELECT EmailAddress, Enabled FROM User WHERE VerificationToken = ?";
	private static String USER_ENABLED_UPDATE = "update User set Enabled = ? WHERE EmailAddress = ?";
	private static String PERSONTYPE_BYUSER_QUERY = "select PersonType from Person, PersonType where Person.PersonType_idPersonType = PersonType.idPersonType and Person.User_EmailAddress = ?";
	private static String LOGIN_QUERY = "SELECT FirstName, LastName FROM User WHERE EmailAddress = ? and UserPassword = ? and Enabled = 1";
	private static String PERSON_BYUSER_QUERY = "SELECT idPerson FROM Person WHERE User_EmailAddress = ?";
	private static String ACTIVE_TENANT_QUERY = "SELECT idPropertyInformationPerson FROM PersonType, Person, PropertyInformationPerson WHERE PropertyInformationPerson.Person_idPerson = Person.idPerson AND PersonType.idPersonType = Person.PersonType_idPersonType AND Person.idPerson = ? AND PersonType.PersonType = 'Tenant' AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP))";
	private static String PERSONTYPE_BYPERSON_QUERY = "SELECT PersonType FROM PersonType, Person WHERE PersonType.idPersonType = Person.PersonType_idPersonType AND Person.idPerson = ?";
	private static String ACCESS_BYUSER_QUERY = "SELECT Authority FROM Access WHERE User_EmailAddress = ?";
	private static String PASSWORD_BYUSER_QUERY = "SELECT UserPassword FROM User WHERE EmailAddress = ?";
}
