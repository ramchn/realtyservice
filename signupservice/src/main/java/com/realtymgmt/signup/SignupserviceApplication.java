package com.realtymgmt.signup;

import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;

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
		map.put("name","GET Index");
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
	@PostMapping("/signup")
	private Map<String, Object> signUp(HttpServletRequest request, @RequestParam Integer PersonTypeId, 
								@RequestParam String EmailAddress, @RequestParam String UserPassword, 
		  						@RequestParam String FirstName, @RequestParam(required = false) String LastName, 
		  						@RequestParam(required = false) String MiddleName, @RequestParam(required = false) String DOB, 
		  						@RequestParam(required = false) Integer GenderId) {
		  
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		Number userCreated = 0;
		Number PersonId = 0;
		Number EmailId = 0;
		String VerificationToken = UUID.randomUUID().toString();		
		Map<String, Object> userParams = new HashMap<String, Object>();
		Map<String, Object> personParams = new HashMap<String, Object>();
		Map<String, Object> emailParams = new HashMap<String, Object>();
		
		userParams.put("EmailAddress", EmailAddress);
		userParams.put("UserPassword", UserPassword);
		userParams.put("FirstName", FirstName);
		userParams.put("LastName", LastName);
		userParams.put("MiddleName", MiddleName);
		userParams.put("DOB", DOB);
		userParams.put("Gender_idGender", GenderId);	 	
		userParams.put("VerificationToken", VerificationToken);
	 
		// create user
		userCreated = createUser("User", userParams, resultMap);	  
	  
		if (userCreated.intValue() == 1) {
			
			personParams.put("PersonType_idPersonType", PersonTypeId);
			personParams.put("User_EmailAddress", EmailAddress);				  
			//personParams.put("Role_idRole", RoleId);	
			PersonId = tableInsert("Person",personParams);
			
			if (PersonId.intValue() > 0 ) {				
				
				emailParams.put("Email", EmailAddress);
				emailParams.put("EmailType_idEmailType", 1); // email type is login
				emailParams.put("Person_idPerson", PersonId);
				EmailId = tableInsert("Email",emailParams);
			
				if (EmailId.intValue() > 0 ) {			
					
					try {
					
						sendVerificationLink(EmailAddress, VerificationToken);
						resultMap.put("message", "user ("+ EmailAddress +") created successfully, activation link sent to email address.");	
						
					} catch (AddressException ae) {
						// log the exception as error
					} catch (MessagingException me) {
						// log the exception as error
					} catch(IOException ioe) {
						// log the exception as error
					}
				
				} else {
					// rollback person record
					// rollback user record
				}
			
			} else {
				// rollback user record
			}
		} 
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create user ("+EmailAddress+")");
		}	
		 
		return resultMap;	  
	}
	  
	@GetMapping("/signup/verification")
	private Map<String, Object> userVerification(@RequestParam String token) {
		  
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
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
			
					resultMap.put("message", "user ("+EmailAddress+") activated successfully");
					  
				} else {
					resultMap.put("message", "user ("+EmailAddress+") activation failed");
					  
				}
				
			} else {
				resultMap.put("message", "user ("+EmailAddress+") already activated");
					  
			}
				  
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("message", "user not exist, activation failed");
			  
		}		  
		return resultMap;
	}
		
	//sign in service
	@PostMapping("/signin")
	private Map<String, Object> signIn(HttpServletRequest request, @RequestParam String EmailAddress, @RequestParam String UserPassword) 
	  						throws AddressException, MessagingException, IOException {
	  
		Map<String, Object> resultMap = new HashMap<String, Object>();
	  
		try {	  
			jdbcTemplate.queryForObject("SELECT FirstName FROM User WHERE EmailAddress = '"+ EmailAddress +"' and UserPassword = '"+ UserPassword +"' and Enabled = 1", String.class);
			resultMap.put("Authenticated", true);
		  
		} catch (EmptyResultDataAccessException e) {
			resultMap.put("Authenticated", false);
		  
		}		  
		return resultMap;
	}
	 		   
	private Number createUser(String table, Map<String, Object> params, Map<String, Object> resultMap) {
		  
		Number userCreated = 0;
		  
		try {	  
			  jdbcTemplate.queryForObject("SELECT FirstName FROM User WHERE EmailAddress = '"+params.get("EmailAddress")+"'", String.class);
			  resultMap.put("message", "user ("+params.get("EmailAddress")+") already exist : unable to create");
			  
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
	  
	private void sendVerificationLink(String EmailAddress, String VerificationToken) throws AddressException, MessagingException, IOException {
		  
		Message msg = new MimeMessage(getSession(getProperties()));
		msg.setFrom(new InternetAddress(env.getProperty("email.address"), false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailAddress));
		msg.setSubject("Email Verification");
		   
		msg.setContent("Email Verification Link: <a href='http://localhost:8071/signup/verification?token="+ VerificationToken +"'>Click</a> here to verify your email address.", "text/html");
		   
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
	
	private static String PERSONTYPE_QUERY = "select idPersonType, PersonType from PersonType";
	private static String GENDER_QUERY = "select idGender, Gender from Gender";
	
}
