package com.codesimple.security.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Mail {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Value("${mail.from}")
	private  String fromEmail;	// = "example@gmail.com";  requires valid gmail id
	
	@Value("${mail.password}")
	private  String password;	// = "password";  correct password for gmail id

	@Value("${mail.smtp.host}")
	private String host;
	
	@Value("${mail.smtp.port}")
	private String port;
	
	/**
	 * Outgoing Mail (SMTP) Server requires TLS or SSL: smtp.gmail.com (use
	 * authentication) Use Authentication: Yes Port for TLS/STARTTLS: 587
	 */
	public String sendMail(String toEmail, String subject, String body) throws Exception{

//		final String toEmail = "example@gmail.com"; // can be any email id 
		logger.info("**********Drafting Email**********");
		Properties props = new Properties();
		
		props.put("mail.smtp.host", host); // SMTP Host
		props.put("mail.smtp.port", port); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		return sendMailUtil(session, toEmail, subject, body);

	}

	public String sendMailUtil(Session session, String toEmail, String subject, String body) throws Exception{
		
			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@gmail.com", "NoReply"));
			msg.setReplyTo(InternetAddress.parse("no_reply@gmail.com", false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			
			logger.info("**********Message is ready**********");
			Transport.send(msg);

			return "EMail Sent Successfully!!";		
	}
}
