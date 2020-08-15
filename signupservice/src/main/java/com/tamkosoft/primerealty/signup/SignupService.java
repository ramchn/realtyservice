package com.tamkosoft.primerealty.signup;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tamkosoft.primerealty.common.PrimeRealtyLogger;
import com.tamkosoft.primerealty.common.pojo.EmailAddress;
import com.tamkosoft.primerealty.common.pojo.Person;
import com.tamkosoft.primerealty.common.pojo.PropInfoPerson;

import com.tamkosoft.primerealty.signup.pojo.Login;
import com.tamkosoft.primerealty.signup.pojo.VerificationToken;

@RestController 
public class SignupService {
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private SignupDao signupDao;
	
	@Autowired
    private SignupEmailer signupEmailer;
	
	@GetMapping("/persontypes")
	private List<Map<String, Object>> getPersonTypes() {
		  
		return signupDao.getPersonTypes();
	}
	  
	@GetMapping("/genders")
	private List<Map<String, Object>> getGenders() {
		  
		return signupDao.getGenders();
	}
	
	//sign up service
	@PostMapping("/signupbypropertyinfo")
	private Map<String, Object> signUpByPropInfo(@Valid @RequestBody PropInfoPerson propInfoPerson) {
		
		primeRealtyLogger.debug(SignupService.class, "signUpByPropInfo() -> propInfoPerson: " + propInfoPerson.getEmailAddress());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> pipResultMap = new HashMap<String, Object>();
			
		String VerificationToken = UUID.randomUUID().toString();
		
		// database entry upto Person
		resultMap = signupDao.createPerson(propInfoPerson, VerificationToken);
		
		primeRealtyLogger.debug(SignupService.class, "signUpByPropInfo() -> database entry upto Person: " + resultMap);
		
		// database entry for PropertyInformationPerson
		if (resultMap.containsKey("idPerson")) {		
			
			pipResultMap = signupDao.createPropInfoPerson((Number)resultMap.get("idPerson"), propInfoPerson.getPropertyInformationId(), propInfoPerson.getUpdateByPersonId());

			primeRealtyLogger.debug(SignupService.class, "signUpByPropInfo() -> database entry for PropertyInformationPerson: " + pipResultMap);
			
		}		
		
		// send verification link to email address only when all database entry is success
		if(resultMap.containsKey("idPerson") && resultMap.containsKey("idEmail") && pipResultMap.containsKey("idPropertyInformationPerson")) {
			
			resultMap.put("message", "user ("+ propInfoPerson.getEmailAddress() +") created successfully");	
			
			Map<String, Object> emailResultMap = signupEmailer.emailPasswordVerificationLink(propInfoPerson.getEmailAddress(), VerificationToken);	
			
			primeRealtyLogger.debug(SignupService.class, "signUpByPropInfo() -> email sent: " + emailResultMap);
			
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create user ("+ propInfoPerson.getEmailAddress() +")");
		}	
		
		return resultMap;
	}
		
	@PostMapping("/signup")
	private Map<String, Object> signUp(@Valid @RequestBody Person person) {
		
		primeRealtyLogger.debug(SignupService.class, "signUp() -> person: " + person.getEmailAddress());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String VerificationToken = UUID.randomUUID().toString();
					
		// database entry
		resultMap = signupDao.createPerson(person, VerificationToken);
		
		primeRealtyLogger.debug(SignupService.class, "signUp() -> database entry finished: " + resultMap);
		
		// send verification link to email address only when database entry is success
		if(resultMap.containsKey("idPerson") && resultMap.containsKey("idEmail")) {	
			
			resultMap.put("message", "user ("+ person.getEmailAddress() +") created successfully");	
			
			Map<String, Object> emailResultMap = signupEmailer.emailVerificationLink(person.getEmailAddress(), VerificationToken);				
			
			primeRealtyLogger.debug(SignupService.class, "signUp() -> email sent: " + emailResultMap);
			
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		if(!resultMap.containsKey("message")) {
			resultMap.put("message", "unable to create user ("+ person.getEmailAddress() +")");
		}
			
		return resultMap;
	}
	
	@PostMapping("/signup/userverification")
	private Map<String, Object> userVerification(@Valid @RequestBody VerificationToken verificationToken) {
		
		primeRealtyLogger.debug(SignupService.class, "userVerification() -> verificationToken: " + verificationToken.getToken());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// database entry
		resultMap = signupDao.verifyUserAndCreateAccess(verificationToken.getToken());
		
		if(resultMap.containsKey("idAccess")) {				
			
			primeRealtyLogger.debug(SignupService.class, "userVerification() -> database entry finished: " + resultMap);
			
			resultMap.put("message", "user ("+ resultMap.get("EmailAddress") +") verified successfully");	
			
		}
			
		return resultMap;
	}
	
	
	//sign in service
	@PostMapping("/signin")
	private Map<String, Object> signIn(@Valid @RequestBody Login login) {
		
		primeRealtyLogger.debug(SignupService.class, "signIn() -> request body > Email Address : " + login.getEmailAddress() + ", User Password : " + login.getUserPassword());
		
		Map<String, Object> resultMap = signupDao.loginUser(login.getEmailAddress(), login.getUserPassword());
		
		primeRealtyLogger.debug(SignupService.class, "signIn() -> database fetch finished: " + resultMap);
				
		return resultMap;
	}
	
	@PostMapping("/signin/retrievepassword")
	private Map<String, Object> retrievePassword(@Valid @RequestBody EmailAddress emailAddress) {
	  
		primeRealtyLogger.debug(SignupService.class, "retrievePassword() -> emailAddress: " + emailAddress.getEmailAddress());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
			
		resultMap = signupDao.getPassword(emailAddress.getEmailAddress());
		
		if(resultMap.containsKey("UserPassword")) {			
			
			resultMap.put("message", "Password found for this email ("+ emailAddress.getEmailAddress() +")");
			
			String UserPassword = resultMap.get("UserPassword").toString();			
			byte[] decodedBytes = Base64.getDecoder().decode(UserPassword);
			String decodedUserPassword = new String(decodedBytes);
									
			Map<String, Object> emailResultMap = signupEmailer.emailPassword(emailAddress.getEmailAddress(), decodedUserPassword);
			
			primeRealtyLogger.debug(SignupService.class, "retrievePassword() -> email sent: " + emailResultMap);
			
			resultMap.put("emailmessage", emailResultMap.get("emailmessage"));
		}
		
		return resultMap;
	}
		
}
