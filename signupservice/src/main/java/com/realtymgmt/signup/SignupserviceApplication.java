package com.realtymgmt.signup;

import java.util.Base64;
import java.util.Date;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController 
public class SignupserviceApplication {

	@Autowired
	JdbcTemplate jdbcTemplate;
  
	@Autowired
	Environment env;
  
	public static void main(String[] args) {
		SpringApplication.run(SignupserviceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}
	
	@GetMapping("/index")
	private Map<String, Object> gIndex() {		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "GET Index");
		return map;
	}
		  
	@GetMapping("/persontypes")
	private List<Map<String, Object>> getPersonTypes() {
		  
		return jdbcTemplate.queryForList(PERSONTYPE_QUERY);
	}
	  
	@GetMapping("/genders")
	private List<Map<String, Object>> getGenders() {
		  
		return jdbcTemplate.queryForList(GENDER_QUERY);
	}
	  
	//sign up service
	@PostMapping("/signupbypropertyinfo")
	private Map<String, Object> tenantSignUp(@RequestParam Integer PersonTypeId, @RequestParam String EmailAddress, 
									@RequestParam String UserPassword, @RequestParam Integer idPropertyInformation, 
									@RequestParam String EmailSubject, @RequestParam String EmailBody) {
		
		Map<String, Object> pipParams = new HashMap<String, Object>();
		Number idPropertyInformationPerson = 0;
		String VerificationToken = UUID.randomUUID().toString();
		
		// database entry upto Person
		Map<String, Object> resultMap = createPerson(PersonTypeId, EmailAddress, 
						UserPassword, null, null, null, null, VerificationToken);
		
		// database entry for PropertyInformationPerson
		if (resultMap.get("idPerson") != null) {			
			
			pipParams.put("Person_idPerson", (Number)resultMap.get("idPerson"));
			pipParams.put("PropertyInformation_idPropertyInformation", idPropertyInformation);			
			idPropertyInformationPerson = tableInsert("PropertyInformationPerson",pipParams);
			
			if(idPropertyInformationPerson.intValue() > 0) {
				resultMap.put("idPropertyInformationPerson", idPropertyInformationPerson);
				
			} else {								
				// rollback Email record
				// rollback Person record
				// rollback User record
			}
		}		
		
		// send verification link to email address only when all database entry is success
		if(resultMap.containsKey("idPerson") && resultMap.containsKey("idEmail") && resultMap.containsKey("idPropertyInformationPerson")) {
			resultMap.put("message", "user ("+ EmailAddress +") created successfully");	
			
			EmailBody = EmailBody.replace("<VerificationToken>", VerificationToken);
			Map<String, Object> emailResultMap = emailVerificationLink(EmailAddress, EmailSubject, EmailBody);				
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create user ("+EmailAddress+")");
		}	
		
		return resultMap;
	}
	
	@PostMapping("/signup")
	private Map<String, Object> signUp(@RequestParam Integer PersonTypeId, @RequestParam String EmailAddress, 
								@RequestParam String UserPassword, @RequestParam String FirstName, 
								@RequestParam(required = false) String LastName, @RequestParam(required = false) String MiddleName, 
								@RequestParam(required = false) Integer GenderId, @RequestParam String EmailSubject,
								@RequestParam String EmailBody) {
	
		String VerificationToken = UUID.randomUUID().toString();
		
		// database entry
		Map<String, Object> resultMap = createPerson(PersonTypeId, EmailAddress, 
				UserPassword, FirstName, LastName, MiddleName, GenderId, VerificationToken);
		
		// send verification link to email address only when database entry is success
		if(resultMap.containsKey("idPerson") && resultMap.containsKey("idEmail")) {			
			resultMap.put("message", "user ("+ EmailAddress +") created successfully");	
			
			EmailBody = EmailBody.replace("<VerificationToken>", VerificationToken);
			Map<String, Object> emailResultMap = emailVerificationLink(EmailAddress, EmailSubject, EmailBody);				
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create user ("+EmailAddress+")");
		}
		
		return resultMap;
	}
		  
	@PostMapping("/signup/userverification")
	private Map<String, Object> userVerification(@RequestParam String token) {
		  
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		try {	  
			Map<String, Object> columns = jdbcTemplate.queryForMap(USER_BYTOKEN_QUERY, token);
	  
			String EmailAddress = columns.get("EmailAddress").toString();
			Object Enabled = columns.get("Enabled");
	  
			if (Enabled == null) {
				Object[] params = { 1, EmailAddress};
				int[] types = {Types.TINYINT, Types.VARCHAR};		  
				Number updated  = jdbcTemplate.update(ACCESS_BYUSER_UPDATE, params, types);
			
				if (updated.intValue() == 1) {
					String PersonType = jdbcTemplate.queryForObject(PERSONTYPE_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
			
					Map<String, Object> accessParams = new HashMap<String, Object>();		  
					accessParams.put("Authority", "ROLE_"+ PersonType.toUpperCase());
					accessParams.put("User_EmailAddress", EmailAddress);
					tableInsert("Access", accessParams);
			
					resultMap.put("message", "user ("+EmailAddress+") verified successfully");
					  
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
		
	//sign in service
	@PostMapping("/signin")
	private Map<String, Object> signIn(@RequestParam String EmailAddress, @RequestParam String UserPassword) {
	  
		Map<String, Object> resultMap = new HashMap<String, Object>();
	  
		try {	  
			Object[] params = {EmailAddress, UserPassword};
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
	
	@PostMapping("/signin/retrievepassword")
	private Map<String, Object> retrievePassword(@RequestParam String EmailAddress, @RequestParam String EmailSubject, 
												@RequestParam String EmailBody) {
	  
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String UserPassword = jdbcTemplate.queryForObject(PASSWORD_BYUSER_QUERY, new Object[] {EmailAddress}, String.class);
			resultMap.put("UserPassword", UserPassword);			
			
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("message", "Password not found for this email ("+ EmailAddress +"), check you entered the registered email address");
		  
		}
		
		if(resultMap.containsKey("UserPassword")) {			
			resultMap.put("message", "Password found for this email ("+ EmailAddress +")");
			
			String UserPassword = resultMap.get("UserPassword").toString();			
			byte[] decodedBytes = Base64.getDecoder().decode(UserPassword);
			String decodedUserPassword = new String(decodedBytes);
			
			EmailBody = EmailBody.replace("<UserPassword>", decodedUserPassword);			
			Map<String, Object> emailResultMap = emailPassword(EmailAddress, EmailSubject, EmailBody);
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		return resultMap;
	}
	
	private Map<String, Object> createPerson(Integer PersonTypeId, String EmailAddress, 
			String UserPassword, String FirstName, String LastName, 
			String MiddleName, Integer GenderId, String VerificationToken) {

		Map<String, Object> resultMap = new HashMap<String, Object>();			
		Number userCreated = 0;
		Number idPerson = 0;
		Number idEmail = 0;
		Map<String, Object> userParams = new HashMap<String, Object>();
		Map<String, Object> personParams = new HashMap<String, Object>();
		Map<String, Object> emailParams = new HashMap<String, Object>();
		
		userParams.put("EmailAddress", EmailAddress);
		userParams.put("UserPassword", UserPassword);
		userParams.put("FirstName", FirstName);
		userParams.put("LastName", LastName);
		userParams.put("MiddleName", MiddleName);
		userParams.put("Gender_idGender", GenderId);	 	
		userParams.put("VerificationToken", VerificationToken);
		
		// create user
		userCreated = createUser("User", userParams, resultMap);	  
		
		if (userCreated.intValue() == 1) {
		
			personParams.put("PersonType_idPersonType", PersonTypeId);
			personParams.put("User_EmailAddress", EmailAddress);
			personParams.put("CreatedDate", new Date());
			idPerson = tableInsert("Person",personParams);
			
			if (idPerson.intValue() > 0) {		
				resultMap.put("idPerson", idPerson);
				
				emailParams.put("Email", EmailAddress);
				emailParams.put("EmailType_idEmailType", 1); // email type is login
				emailParams.put("Person_idPerson", idPerson);
				idEmail = tableInsert("Email",emailParams);
				
				if (idEmail.intValue() > 0) {						
					resultMap.put("idEmail", idEmail);		
											
				} else {
					// rollback Person record
					// rollback User record
				}
			
			} else {
				// rollback User record
			}
		} 
		
		return resultMap;
	}
	
	private Map<String, Object> emailVerificationLink(String EmailAddress, 
									 String Subject, 
									 String Content) {
	
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		try {
			sendEmail(EmailAddress, Subject, Content);  
			resultMap.put("emailmessage", "verification link sent to email address");
		
		} catch(AddressException ae) {
			resultMap.put("emailmessage", "unable to send verification link - due to invalid address");
			
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "unable to send verification link - due to server issue");
			
		} 
		
		return resultMap;
	}
	
	private Map<String, Object> emailPassword(String EmailAddress, 
			 String Subject, 
			 String Content) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			sendEmail(EmailAddress, Subject, Content);  
			resultMap.put("emailmessage", "password sent to email address");
		
		} catch(AddressException ae) {
			resultMap.put("emailmessage", "unable to send password - due to invalid address");
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "unable to send password - due to server issue");
		
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
	
	private void sendEmail(String EmailAddress, String Subject, 
			 String Content) throws AddressException, MessagingException {
		
		Message msg = new MimeMessage(getSession(getProperties()));
		msg.setFrom(new InternetAddress(env.getProperty("email.address"), false));
		
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailAddress));
		msg.setSubject(Subject);
		
		msg.setContent(Content, "text/html");
		
		Transport.send(msg);   
	}
	
	private static String PERSONTYPE_QUERY = "select idPersonType, PersonType from PersonType";
	private static String GENDER_QUERY = "select idGender, Gender from Gender";
	private static String USER_QUERY = "SELECT FirstName FROM User WHERE EmailAddress = ?";
	private static String ACTIVE_TENANT_QUERY = "SELECT idPropertyInformationPerson FROM PersonType, Person, PropertyInformationPerson WHERE PropertyInformationPerson.Person_idPerson = Person.idPerson AND PersonType.idPersonType = Person.PersonType_idPersonType AND Person.idPerson = ? AND PersonType.PersonType = 'Tenant' AND ((PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate IS NULL) OR (PropertyInformationPerson.StartDate <= CURRENT_TIMESTAMP AND PropertyInformationPerson.EndDate > CURRENT_TIMESTAMP))";
	private static String PERSONTYPE_BYPERSON_QUERY = "SELECT PersonType FROM PersonType, Person WHERE PersonType.idPersonType = Person.PersonType_idPersonType AND Person.idPerson = ?";
	private static String ACCESS_BYUSER_QUERY = "SELECT Authority FROM Access WHERE User_EmailAddress = ?";
	private static String PERSON_BYUSER_QUERY = "SELECT idPerson FROM Person WHERE User_EmailAddress = ?";
	private static String LOGIN_QUERY = "SELECT FirstName, LastName FROM User WHERE EmailAddress = ? and UserPassword = ? and Enabled = 1";
	private static String PERSONTYPE_BYUSER_QUERY = "select PersonType from Person, PersonType where Person.PersonType_idPersonType = PersonType.idPersonType and Person.User_EmailAddress = ?";
	private static String ACCESS_BYUSER_UPDATE = "update User set Enabled = ? WHERE EmailAddress = ?";
	private static String USER_BYTOKEN_QUERY = "SELECT EmailAddress, Enabled FROM User WHERE VerificationToken = ?";
	private static String PASSWORD_BYUSER_QUERY = "SELECT UserPassword FROM User WHERE EmailAddress = ?";
}
