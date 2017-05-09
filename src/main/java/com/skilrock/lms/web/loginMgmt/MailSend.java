package com.skilrock.lms.web.loginMgmt;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class is used to send email
 * 
 * @author gauravk
 * 
 */
public class MailSend {

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
		}
	}

	private static String emailFromAddress = "igsbackoffice@gmail.com";
	private static String SMTP_AUTH_PWD = "skilrock";
	private static String SMTP_AUTH_USER = "igsbackoffice@gmail.com";

	private static String SMTP_HOST_NAME = "smtp.gmail.com";

	private static String SMTP_PORT = "465";

	/**
	 * Used for Invite Friend to send mail to 1 User only : WORKING
	 * 
	 * @param emailAddress
	 * @param pMessage
	 * @param pSubject
	 */
	public static void sendMail(String emailAddress, String pMessage,
			String pSubject) {

		MailSend smtpMailSender = new MailSend();
		try {
			smtpMailSender.postMail(emailAddress, pSubject, pMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param msg
	 * @param str
	 * @throws MessagingException
	 */
	// A simple, single-part text/plain e-mail.
	public static void setTextContent(Message msg, String str)
			throws MessagingException {
		// Set message content
		String mytxt = "This is a test of sending a "
				+ "plain text e-mail through Java.\n" + "Here is line 2.";
		msg.setText(mytxt);

		// Alternate form
		msg.setContent(mytxt, "text/plain");

	}

	/**
	 * @return
	 */
	private Session getSessionForSendingEmail() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.user", SMTP_AUTH_USER);
		props.setProperty("mail.password", SMTP_AUTH_PWD);
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.setProperty("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.auth", "true");
		props.put("javax.activation.debug", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		return session;
	}

	/**
	 * 
	 * @param recipient
	 * @param subject
	 * @param message
	 * @param from
	 * @throws Exception
	 */

	private Boolean postMail(String recipient, String subject, String message) {
		Session session = null;
		session = getSessionForSendingEmail();
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom;
		try {
			addressFrom = new InternetAddress(emailFromAddress);
			msg.setFrom(addressFrom);
			InternetAddress addressTo = new InternetAddress(recipient);
			msg.setRecipient(Message.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * send Email to multiple user
	 */

	public Boolean sendEmailMany(String[] to, String subject, String bodyText) {

		System.out
				.println(" *************   MAIL TO SEND TO INVITE FRIEND **************************");
		Session session = getSessionForSendingEmail();
		Message msg = new MimeMessage(session);
		Address[] address = new InternetAddress[to.length];
		int i = 0;
		for (String addressTo : to) {
			try {
				address[i++] = new InternetAddress(addressTo);
			} catch (AddressException e) {
				e.printStackTrace();
				return false;

			}
		}
		try {
			msg.setFrom(new InternetAddress(SMTP_AUTH_USER));
			msg.setSubject(subject);
			msg.setText(bodyText);
			msg.setRecipients(Message.RecipientType.TO, address);
			Transport.send(msg);
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param emailAddress
	 * @param pMessage
	 * @param pSubject
	 * @return
	 */
	public Boolean sendMailInviteFriend(String emailAddress, String pMessage,
			String pSubject) {
		System.out.println("To send mail to one friend");
		return postMail(emailAddress, pSubject, pMessage);
	}

}
