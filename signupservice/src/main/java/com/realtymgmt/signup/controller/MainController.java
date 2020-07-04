package com.realtymgmt.signup.controller;

import java.io.IOException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController 
public class MainController {
  
  private String result = "";
	
  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  Environment env;
  
  @GetMapping("/index")
  public String gindex() {		
	return "GET Index";
  }
  
  @GetMapping("/genders")
  private List<Map<String, Object>> getGenders() {
	  
	  return jdbcTemplate.queryForList("select idGender, Gender from Gender");
  }
  
  @GetMapping("/signup/verification")
  private String userVerification(@RequestParam String token) {
	  
	  String result = "";

	  try {	  
		  Map<String, Object> columns = jdbcTemplate.queryForMap("SELECT EmailAddress, Enabled FROM User WHERE VerificationToken = '"+token+"'");
		  
		  String EmailAddress = columns.get("EmailAddress").toString();
		  Object Enabled = columns.get("Enabled");
		  
		  if (Enabled == null) {
			  Object[] params = { 1, EmailAddress};
			  int[] types = {Types.TINYINT, Types.VARCHAR};		  
			  Number updated  = jdbcTemplate.update("update User set Enabled = ? WHERE EmailAddress = ?", params, types);
	
			  if (updated.intValue() == 1) {
				  String PersonType = jdbcTemplate.queryForObject("select PersonType from Person, PersonType where Person.PersonType_idPersonType = PersonType.idPersonType and Person.User_EmailAddress = '"+EmailAddress+"'", String.class);
	
				  Map<String, Object> accessParams = new HashMap<String, Object>();		  
				  accessParams.put("Authority", "ROLE_"+ PersonType.toUpperCase());
				  accessParams.put("User_EmailAddress", EmailAddress);
				  tableInsert("Access", accessParams);
	
				  result = "user ("+EmailAddress+") activated successfully";
				  
			  } else {
				  result = "user activation failed";
				  
			  } 
		  } else {
			  result = "user already activated";
			  
		  }
		  
	  } catch (EmptyResultDataAccessException e) {
		  result = "user activation failed";
		  
	  }	 
	  
	  return result;
  }
  
  //sign up service	 
  @PostMapping(value={"/ownerpmsignup", "/ownersignup", "/pmsignup", "/spsignup", "/tenantsignup"})
  private String signup(HttpServletRequest request, @RequestParam String EmailAddress, @RequestParam String UserPassword, 
	  						@RequestParam String FirstName, @RequestParam(required = false) String LastName, 
	  						@RequestParam(required = false) String MiddleName, @RequestParam(required = false) String PhoneNumber, 
	  						@RequestParam(required = false) String PhoneType, @RequestParam(required = false) String DOB, 
	  						@RequestParam(required = false) Integer GenderId, @RequestParam(required = false) String Address1, 
	  						@RequestParam(required = false) String Address2, @RequestParam(required = false) String Address3,
	  						@RequestParam(required = false) String Zip, @RequestParam(required = false) String CityName, 
	  						@RequestParam(required = false) String StateCode, @RequestParam(required = false) String CountryCode, 
	  						@RequestParam(required = false) String ServiceCategory, @RequestParam(required = false) String YearsOfExperience, 
	  						@RequestParam(required = false) String AreasOfExpertise, @RequestParam(required = false) String AreaCoverage, 
	  						@RequestParam(required = false) Integer PropertyInfoId) 
	  						throws AddressException, MessagingException, IOException {
	  
	  	  
	  Number PersonTypeId = 0;
	  Number PersonId = 0;
	  String VerificationToken = UUID.randomUUID().toString();
	  	  
	  if("/ownerpmsignup".equals(request.getRequestURI())) {
		  for (Map<String, Object> personType : getPersonType()) {
			  BidiMap<String, Object> bidiPersonType = new DualHashBidiMap<>(personType);			  
			  if(bidiPersonType.containsValue("Owner/PropertyManager")) {       	  
				  PersonTypeId = (Integer)bidiPersonType.get("idPersonType");
	        	  break;
	          }
	      }		 
		  // create owner and he acting as a property manger
		  PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
				StateCode, CountryCode, VerificationToken);
		  
	  } else if("/ownersignup".equals(request.getRequestURI())) {
		  for (Map<String, Object> personType : getPersonType()) {
			  BidiMap<String, Object> bidiPersonType = new DualHashBidiMap<>(personType);			  
			  if(bidiPersonType.containsValue("Owner")) {       	  
				  PersonTypeId = (Integer)bidiPersonType.get("idPersonType");
	        	  break;
	          }
	      }		
		  // create owner
		  PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
					StateCode, CountryCode, VerificationToken);
		  
	  } else if ("/pmsignup".equals(request.getRequestURI())) {
		  for (Map<String, Object> personType : getPersonType()) {
			  BidiMap<String, Object> bidiPersonType = new DualHashBidiMap<>(personType);			  
			  if(bidiPersonType.containsValue("PropertyManager")) {       	  
				  PersonTypeId = (Integer)bidiPersonType.get("idPersonType");
	        	  break;
	          }
	      }		  
		  // create property manger
		  PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
					StateCode, CountryCode, VerificationToken);
		  
	  } else if ("/spsignup".equals(request.getRequestURI())) {
		  for (Map<String, Object> personType : getPersonType()) {
			  BidiMap<String, Object> bidiPersonType = new DualHashBidiMap<>(personType);			  
			  if(bidiPersonType.containsValue("ServiceProvider")) {       	  
				  PersonTypeId = (Integer)bidiPersonType.get("idPersonType");
	        	  break;
	          }
	      }		  
		  if (ServiceCategory == null) {
			  result = "ServiceCategory field is mandatory : unable to create user";			  
		  
		  } else {
			  // create service provider
			  PersonId = createServiceProvider(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
					StateCode, CountryCode, ServiceCategory, YearsOfExperience, 
					AreasOfExpertise, AreaCoverage, VerificationToken);
			  
		  }
		  
	  } else if ("/tenantsignup".equals(request.getRequestURI())) {
		  for (Map<String, Object> personType : getPersonType()) {
			  BidiMap<String, Object> bidiPersonType = new DualHashBidiMap<>(personType);
			  
			  if(bidiPersonType.containsValue("Tenant")) {       	  
				  PersonTypeId = (Integer)bidiPersonType.get("idPersonType");
	        	  break;
	          }
	      }		  
		  if (PropertyInfoId == null) {
			  result = "Property Information is mandatory : unable to create user";			  
			  
		  } else {
			  // create tenant
			  PersonId = createTenant(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
					StateCode, CountryCode, PropertyInfoId, VerificationToken);
			  
		  }
	  }	  	  
	  
	  if (PersonId.intValue() > 0 ) {		  
		  result = "user created : " + PersonId;
		  sendActivationLink(EmailAddress, VerificationToken);
		  
	  } else {		  
		  result = "unable to create user";
		  
	  }
	 
	  
	  return result;
  }
  
    
  private List<Map<String, Object>> getPersonType() {
	  
	  return jdbcTemplate.queryForList("select idPersonType, PersonType from PersonType");
  }
  
  private Number createServiceProvider(Number PersonTypeId, String EmailAddress, String UserPassword, String FirstName, 
		  							String LastName, String MiddleName, String PhoneNumber, String PhoneType, String DOB, 
		  							Integer GenderId, String Address1, String Address2, String Address3, String Zip, String CityName, 
		  							String StateCode, String CountryCode, String ServiceCategory, String YearsOfExperience, String AreasOfExpertise, 
		  							String AreaCoverage, String VerificationToken) {
	  
	  Number ServiceProviderId = 0;
	  
	  Number PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
				StateCode, CountryCode, VerificationToken);
	  
	  if(PersonId.intValue() > 0) {
		  Map<String, Object> spParams = new HashMap<String, Object>();		  
		  spParams.put("ServiceCategory", ServiceCategory);
		  spParams.put("YearsOfExperience", YearsOfExperience);
		  spParams.put("AreasOfExpertise", AreasOfExpertise);
		  spParams.put("AreaCoverage", AreaCoverage);
		  spParams.put("Person_idPerson", PersonId);
		  ServiceProviderId = tableInsert("ServiceProvider",spParams);	 		  
	  }
	  return ServiceProviderId;
	  
  }
  
  private Number createTenant(Number PersonTypeId, String EmailAddress, String UserPassword, 
			String FirstName, String LastName, String MiddleName, String PhoneNumber, 
			String PhoneType, String DOB, Integer GenderId, String Address1, 
			String Address2, String Address3, String Zip, String CityName, 
			String StateCode, String CountryCode,  
			Integer PropertyInfoId, String VerificationToken) {
	  
	  
	  Number TenantId = 0;
	  Number PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, CityName, 
				StateCode, CountryCode, VerificationToken);
	  
	  if(PersonId.intValue() > 0) {
		  Map<String, Object> tenantParams = new HashMap<String, Object>();		  
		  tenantParams.put("Person_idPerson", PersonId);
		  tenantParams.put("PropertyInformation_idPropertyInformation", PropertyInfoId);
		  TenantId = tableInsert("Tenant",tenantParams);	
	  }
	  return TenantId;
  }
  
  private Number createPerson(Number PersonTypeId, String EmailAddress, String UserPassword, 
			String FirstName, String LastName, String MiddleName, String PhoneNumber, 
			String PhoneType, String DOB, Integer GenderId, String Address1, 
			String Address2, String Address3, String Zip, String CityName, 
			String StateCode, String CountryCode, String VerificationToken) {

	  Number AddressId = 0;
	  Number userCreated = 0;
	  Number PersonId = 0;
	  
	  
	  Map<String, Object> userParams = new HashMap<String, Object>();
	  Map<String, Object> addressParams = new HashMap<String, Object>();
	  Map<String, Object> ownerParams = new HashMap<String, Object>();
	  
	  
	  userParams.put("EmailAddress", EmailAddress);
	  userParams.put("UserPassword", new BCryptPasswordEncoder().encode(UserPassword));
	  userParams.put("FirstName", FirstName);
	  userParams.put("LastName", LastName);
	  userParams.put("MiddleName", MiddleName);
	  userParams.put("PhoneNumber", PhoneNumber);
	  userParams.put("PhoneType", PhoneType);
	  userParams.put("DOB", DOB);
	  userParams.put("Gender_idGender", GenderId);	 	
	  userParams.put("VerificationToken", VerificationToken);
	 
	  // create user
	  userCreated = createUser("User", userParams);	  
	  
	  if (userCreated.intValue() == 1) {
		  
		  addressParams.put("Address1", Address1);
		  addressParams.put("Address2", Address2);
		  addressParams.put("Address3", Address3);
		  addressParams.put("Zip", Zip);
		  addressParams.put("CityName", CityName);
		  addressParams.put("StateCode", StateCode);
		  addressParams.put("CountryCode", CountryCode);
		  
		  boolean AddressValuesAreAllNull = addressParams.values().stream().allMatch(Objects::isNull);
		  
		  if (!AddressValuesAreAllNull) {		  
			  AddressId = tableInsert("Address",addressParams);
		  }
		  
		  ownerParams.put("PersonType_idPersonType", PersonTypeId);
		  ownerParams.put("User_EmailAddress", EmailAddress);	
		  if(AddressId.intValue() != 0 ) {
			  ownerParams.put("Address_idAddress", AddressId);	
		  }
		  //ownerParams.put("Role_idRole", RoleId);	
		  PersonId = tableInsert("Person",ownerParams);	 		  
		  
	  } 
	  
	  return PersonId;
  }
   
  private Number createUser(String table, Map<String, Object> params) {
	  
	  Number userCreated = 0;
	  
	  try {	  
		  jdbcTemplate.queryForObject("SELECT FirstName FROM User WHERE EmailAddress = '"+params.get("EmailAddress")+"'", String.class);
		  result = "user ("+params.get("EmailAddress")+") already exist : unable to create user";
		  
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
  
  private void sendActivationLink(String EmailAddress, String VerificationToken) throws AddressException, MessagingException, IOException {
	  
	   Message msg = new MimeMessage(getSession(getProperties()));
	   msg.setFrom(new InternetAddress(env.getProperty("email.address"), false));

	   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailAddress));
	   msg.setSubject("User Activation");
	   
	   msg.setContent("User Activation Link: <a href='http://localhost:8071/signup/verification?token="+ VerificationToken +"'>Click</a> here to activate user.", "text/html");
	   
	   Transport.send(msg);   
	}
  
  private Properties getProperties() {	
	  Properties props = new Properties();
	  props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
	  props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
	  props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
	  props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
	  return props;
  }
		 
  private Session getSession(Properties props) {
	  Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		  protected PasswordAuthentication getPasswordAuthentication() {
			  return new PasswordAuthentication(env.getProperty("email.address"), env.getProperty("email.password"));
		  }
	  });
	  return session;
  }
  
}

