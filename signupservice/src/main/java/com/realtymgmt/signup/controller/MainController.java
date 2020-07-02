package com.realtymgmt.signup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
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
  
  @GetMapping("/index")
  public String gindex() {		
	return "GET Index";
  }
  

  @GetMapping("/countries")
  private List<Map<String, Object>> getCountries() {
	  
	  return jdbcTemplate.queryForList("select idCountry, CountryName, CountryCode from Country");
  }
	  
  @GetMapping("/statesbycountry")
  private List<Map<String, Object>> getStatesByCountry() {
		  
	  return jdbcTemplate.queryForList("select idState, StateName, StateCode, CountryName, CountryCode from State, Country where State.Country_idCountry = Country.idCountry");
  }
  
  @GetMapping("/genders")
  private List<Map<String, Object>> getGenders() {
	  
	  return jdbcTemplate.queryForList("select idGender, Gender from Gender");
  }
  
  //sign up service	 
  @PostMapping(value={"/ownerpmsignup", "/ownersignup", "/pmsignup", "/spsignup", "/tenantsignup"})
  private String signup(HttpServletRequest request, @RequestParam String EmailAddress, @RequestParam String UserPassword, 
	  						@RequestParam String FirstName, @RequestParam(required = false) String LastName, 
	  						@RequestParam(required = false) String MiddleName, @RequestParam(required = false) String PhoneNumber, 
	  						@RequestParam(required = false) String PhoneType, @RequestParam(required = false) String DOB, 
	  						@RequestParam(required = false) Integer GenderId, @RequestParam(required = false) String Address1, 
	  						@RequestParam(required = false) String Address2, @RequestParam(required = false) String Address3,
	  						@RequestParam(required = false) String Zip, @RequestParam(required = false) String City, 
	  						@RequestParam(required = false) Integer StateId, @RequestParam(required = false) String ServiceCategory, 
	  						@RequestParam(required = false) String YearsOfExperience, @RequestParam(required = false) String AreasOfExpertise, 
	  						@RequestParam(required = false) String AreaCoverage, @RequestParam(required = false) Integer PropertyInfoId) {
	  
	  	  
	  Number PersonTypeId = 0;
	  Number PersonId = 0;
	 	  
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
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId);
		  
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
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId);
		  
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
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId);
		  
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
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId, ServiceCategory, YearsOfExperience, 
					AreasOfExpertise, AreaCoverage);
			  
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
					PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId, PropertyInfoId);
			  
		  }
	  }	  	  
	  
	  if (PersonId.intValue() > 0 ) {		  
		  result = "user created : " + PersonId;
		  
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
		  							Integer GenderId, String Address1, String Address2, String Address3, String Zip, String City, 
		  							Integer StateId, String ServiceCategory, String YearsOfExperience, String AreasOfExpertise, 
		  							String AreaCoverage) {
	  
	  Number ServiceProviderId = 0;
	  
	  Number PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId);
	  
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
			String Address2, String Address3, String Zip, String City, Integer StateId, Integer PropertyInfoId) {
	  
	  
	  Number TenantId = 0;
	  Number PersonId = createPerson(PersonTypeId, EmailAddress, UserPassword, FirstName, LastName, MiddleName, PhoneNumber, 
				PhoneType, DOB, GenderId, Address1, Address2, Address3, Zip, City, StateId);
	  
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
			String Address2, String Address3, String Zip, String City, Integer StateId) {
	  
	  Number CityId = 0;
	  Number AddressId = 0;
	  Number userCreated = 0;
	  Number PersonId = 0;
	  
	  
	  Map<String, Object> userParams = new HashMap<String, Object>();
	  Map<String, Object> cityParams = new HashMap<String, Object>();
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
	  // create user
	  userCreated = createUser("User", userParams);	  
	  
	  if (userCreated.intValue() == 1) {
		  
		  addressParams.put("Address1", Address1);
		  addressParams.put("Address2", Address2);
		  addressParams.put("Address3", Address3);
		  addressParams.put("Zip", Zip);
		  cityParams.put("CityName", City);
		  cityParams.put("State_idState", StateId);
		  
		  boolean AddressValuesAreAllNull = addressParams.values().stream().allMatch(Objects::isNull);
		  boolean CityValuesAreAllNull = cityParams.values().stream().allMatch(Objects::isNull);
		  
		  if (!AddressValuesAreAllNull && !CityValuesAreAllNull) {		  
			  CityId = cityInsert("City",cityParams);		  
			  addressParams.put("City_idCity", CityId);
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
  
  private Number cityInsert(String table, Map<String, Object> params) {
	  Number CityId = 0;
	  
	  try {	  
		  CityId = jdbcTemplate.queryForObject("SELECT idCity FROM City WHERE CityName = '"+params.get("CityName")+"' and State_idState = "+params.get("State_idState"), Number.class);
	  
	  } catch (EmptyResultDataAccessException e) {
		  CityId = tableInsert(table, params);
	  }	  
			  
	  return CityId;
  }
  
  private Number createUser(String table, Map<String, Object> params) {
	  
	  Number userCreated = 0;
	  
	  try {	  
		  jdbcTemplate.queryForObject("SELECT FirstName FROM User WHERE EmailAddress = '"+params.get("EmailAddress")+"'", String.class);
		  result = "user email already exist : unable to create user";
		  
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
  
}

