package com.tamkosoft.primerealty.signup;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tamkosoft.primerealty.common.PrimeRealtyEmailer;
import com.tamkosoft.primerealty.common.PrimeRealtyLogger;

@Component("signupEmailer")
public class SignupEmailer {
	
	@Autowired
	Environment env;
	
	@Autowired
    private PrimeRealtyLogger primeRealtyLogger;
	
	@Autowired
    private PrimeRealtyEmailer primeRealtyEmailer;
		
	public Map<String, Object> emailVerificationLink(String EmailAddress, 
			 String VerificationToken) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String FromAddress = env.getProperty("email.address");
		String Subject = env.getProperty("verification.email.subject");
		String Content = env.getProperty("verification.email.content").replace("<VerificationToken>", VerificationToken);
		
		try {
			primeRealtyEmailer.sendEmail(FromAddress, EmailAddress, Subject, Content);
			resultMap.put("emailmessage", "verification link sent to email address");
			
		}  catch(AddressException ae) {
			resultMap.put("emailmessage", "unable to send verification link - due to invalid address");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : AddressException - " + ae.getMessage());
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "unable to send verification link - due to server issue");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : MessagingException - " + me.getMessage());
		
		} 
		
		return resultMap;
	}
		
	public Map<String, Object> emailPasswordVerificationLink(String EmailAddress, 
		String VerificationToken) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String FromAddress = env.getProperty("email.address");
		String Subject = env.getProperty("passwordverification.email.subject");
		String Content = env.getProperty("passwordverification.email.content").replace("<VerificationToken>", VerificationToken);
				
		try {
			primeRealtyEmailer.sendEmail(FromAddress, EmailAddress, Subject, Content);
			resultMap.put("emailmessage", "default password and verification link sent to email address");
		
		} catch(AddressException ae) {
			resultMap.put("emailmessage", "unable to send verification link - due to invalid address");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : AddressException - " + ae.getMessage());
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "unable to send verification link - due to server issue");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : MessagingException - " + me.getMessage());
		
		} 
		
		return resultMap;
	}
	
	public Map<String, Object> emailPassword(String EmailAddress, String UserPassword) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String FromAddress = env.getProperty("email.address");
		String Subject = env.getProperty("forgetpassword.email.subject");
		String Content = env.getProperty("forgetpassword.email.content").replace("<UserPassword>", UserPassword);
		
		
		try {
			primeRealtyEmailer.sendEmail(FromAddress, EmailAddress, Subject, Content);			
			resultMap.put("emailmessage", "password sent to email address");
		
		} catch(AddressException ae) {
			resultMap.put("emailmessage", "unable to send password - due to invalid address");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : AddressException - " + ae.getMessage());
		
		} catch(MessagingException me) {
			resultMap.put("emailmessage", "unable to send password - due to server issue");
			primeRealtyLogger.error(SignupEmailer.class, "Exception : MessagingException - " + me.getMessage());
			
		} 
		
		return resultMap;
	}
	
}
