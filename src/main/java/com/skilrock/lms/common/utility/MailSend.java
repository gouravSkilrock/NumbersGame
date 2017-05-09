package com.skilrock.lms.common.utility;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

/**
 * This class is used to send mail
 * 
 * @author SkilRockTechnologies
 * 
 */

public class MailSend extends Thread {
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			if (LMSFilterDispatcher.isMailSend.equalsIgnoreCase("yes")) {
				return new PasswordAuthentication(username, password);
			} else {
				if (LMSFilterDispatcher.loginMailAlert.equalsIgnoreCase("yes")) {
					return new PasswordAuthentication(username, password);
				} else {
					logger.debug("Mailing is disabled from Property file");
					return new PasswordAuthentication("", "");
				}
			}
		}
	}

	private static final String emailFromAddress = "lms.skilrocklotto@gmail.com";

	private static final String emailSubjectTxt = "Alert From "
			+ LMSFilterDispatcher.orgName;
	static Log logger = LogFactory.getLog(MailSend.class);
	private static final String SMTP_AUTH_PWD = "skilrock";
	private static final String SMTP_AUTH_USER = "lms.user@skilrock.com";
	private static final String SMTP_HOST_NAME = LMSFilterDispatcher.mailSmtpIPAddress;
	private static final String SMTP_PORT = "465";
	private String emailAddress;

	private String emailMsgTxt;

	// default constructor
	public MailSend() {

	}

	// argumented constructor
	public MailSend(String emailAddress, String emailMsgTxt) {
		logger.debug("emailSubjectTxt == " + emailSubjectTxt);
		this.emailAddress = emailAddress;
		this.emailMsgTxt = emailMsgTxt;
	}

	private void postMail(String recipient, String subject, String message,
			String from){
		// Set the host smtp address
		Properties props = System.getProperties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.user", SMTP_AUTH_USER);
		props.setProperty("mail.password", SMTP_AUTH_PWD);

		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.setProperty("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.auth", "true");
		// props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		try{
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);
			Message msg = new MimeMessage(session);
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
			InternetAddress addressTo = new InternetAddress(recipient);
			msg.setRecipient(Message.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setContent(message, "text/html");
			Transport tr = session.getTransport("smtp");
			tr.connect(SMTP_HOST_NAME, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			logger.debug("connected***********");
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			session = null;
			logger.debug("mail session after sending mail is " + session);
		}catch(javax.mail.AuthenticationFailedException AFE){
			LMSException LMSe = new LMSException("Mailing May be Disbaled from property file!", AFE);
			logger.debug(LMSe.getMessage());
			logger.debug(AFE.getMessage());
		}catch(javax.mail.MessagingException ME){
			LMSException LMSe = new LMSException("Mailing May Disbaled from property file!", ME);
			logger.debug(LMSe.getMessage());
			logger.debug(ME.getMessage());
		}
	}

	@Override
	public void run() {
		logger.debug(SMTP_HOST_NAME);
		// sendMail(emailAddress,emailMsgTxt);
		try {
			postMail(emailAddress, emailSubjectTxt, emailMsgTxt,
					emailFromAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
