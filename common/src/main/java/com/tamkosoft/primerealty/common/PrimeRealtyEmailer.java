package com.tamkosoft.primerealty.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("primeRealtyEmailer")
public class PrimeRealtyEmailer {
	
	@Autowired
	Environment env;

	public void sendEmail(String FromAddress, String ToAddress, String Subject, String Content) throws AddressException, MessagingException {

		Message msg = new MimeMessage(getSession(getProperties()));
		msg.setFrom(new InternetAddress(FromAddress, false));
		
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ToAddress));
		msg.setSubject(Subject);
		
		msg.setContent(Content, "text/html");
		
		Transport.send(msg);   

	}

	public Session getSession(Properties props) {
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			  protected PasswordAuthentication getPasswordAuthentication() {
				  return new PasswordAuthentication(env.getProperty("email.address"), env.getProperty("email.password"));
			  }
		});
		return session;
	}
	
	public Properties getProperties() {	
		Properties props = new Properties();
		props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
		props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
		return props;
	}
}
