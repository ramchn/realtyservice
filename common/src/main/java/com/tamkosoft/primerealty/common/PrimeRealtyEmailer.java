package com.tamkosoft.primerealty.common;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("primeRealtyEmailer")
public class PrimeRealtyEmailer {
	
	@Autowired
	Environment env;
	
	
	public void sendEmailWithAttachment(String FromAddress, String ToAddress, 
							String Subject, String Content, byte[] attachment, 
							String attachmentName, String attachmentType) throws AddressException, MessagingException {

		if(attachment != null) {
		
			Message msg = new MimeMessage(getSession(getProperties()));
			msg.setFrom(new InternetAddress(FromAddress, false));
			
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ToAddress));
			msg.setSubject(Subject);
			
			// Create the message part
	        BodyPart messageBodyPart = new MimeBodyPart();

	        // Now set the actual message
	        messageBodyPart.setContent(Content, "text/html");

	        // Create a multipart message
	        Multipart multipart = new MimeMultipart();

	        // Set text message part
	        multipart.addBodyPart(messageBodyPart);

	        // Part two is attachment
	        messageBodyPart = new MimeBodyPart();
	        BufferedDataSource bds = new BufferedDataSource(attachment, attachmentName, attachmentType);
	        messageBodyPart.setDataHandler(new DataHandler(bds));
	        messageBodyPart.setFileName(bds.getName());
	        multipart.addBodyPart(messageBodyPart);

	        // Send the complete message parts
	        msg.setContent(multipart);
	        
			Transport.send(msg); 
			
		} else {
			sendEmail(FromAddress, ToAddress, Subject, Content);
		}
	}


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
