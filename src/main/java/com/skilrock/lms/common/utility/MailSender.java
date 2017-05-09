package com.skilrock.lms.common.utility;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.scheduler.MailBean;

/**
 * This class is used to send mail
 * 
 * @author SkilRockTechnologies
 * 
 */
public class MailSender extends Thread {
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			if (LMSFilterDispatcher.isMailSend.equalsIgnoreCase("yes")) {
				return new PasswordAuthentication(username, password);
			} else {
				logger.debug("Mailing is disabled from Property file");
				return new PasswordAuthentication("", "");

			}

		}
	}

	static Log logger = LogFactory.getLog(MailSender.class);
	private static final String SMTP_HOST_NAME = LMSFilterDispatcher.mailSmtpIPAddress;
	private static final String SMTP_PORT = "465";
	private String bodyText;
	private int count;

	private String filename;

	private File fileObj;

	boolean flag = false;
	private List<MailBean> mailbean = null;
	private Properties props = null;
	private String realFileName;
	private String SMTP_AUTH_PWD;
	private String SMTP_AUTH_USER;

	private String subject;

	private List<String> to;

	/*
	 * Default Constructor set the host smtp address detail into property file
	 */
	public MailSender(String FROM, String PASSWORD) {
		setProperties(FROM, PASSWORD);
	}

	public MailSender(String FROM, String PASSWORD, List<MailBean> mailbean) {
		setProperties(FROM, PASSWORD);
		this.mailbean = mailbean;
	}

	public MailSender(String FROM, String PASSWORD, List<String> to,
			String subject, String bodyText, File fileObj, String realFileName) {
		setProperties(FROM, PASSWORD);
		this.to = to;
		this.subject = subject;
		this.bodyText = bodyText;
		this.filename = null;
		this.fileObj = fileObj;
		this.realFileName = realFileName;
		props = System.getProperties();
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
	}

	public MailSender(String FROM, String PASSWORD, List<String> to,
			String subject, String bodyText, String filename) {
		setProperties(FROM, PASSWORD);
		this.to = to;
		this.subject = subject;
		this.bodyText = bodyText;
		this.filename = filename;
		this.fileObj = null;
		props = System.getProperties();
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
	}

	@Override
	public void run() {
		if (mailbean == null) {
			logger.debug(SMTP_HOST_NAME);
			if (fileObj == null) {
				sendEmail(to, subject, bodyText, filename);
			} else {
				sendEmail(to, subject, bodyText, fileObj, realFileName);
			}

		} else {
			logger.debug(SMTP_HOST_NAME);
			sendEmailToAgent(mailbean);
		}
	}

	// Added by Vaibhav for file Object
	public void sendEmail(List<String> to, String subject, String bodyText,
			File fileObj, String realFileName) {
		count = count + 1;
		Authenticator auth = new SMTPAuthenticator();
		try {
			Session session = Session.getInstance(props, auth);
			Transport tr = session.getTransport("smtp");
			tr.connect(SMTP_HOST_NAME, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			logger.debug("connected***********");
			Address addressTo[] = new Address[to.size()];
			for (int i = 0; i < to.size(); i++) {
				addressTo[i] = new InternetAddress(to.get(i));
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SMTP_AUTH_USER));

			// set the message subject
			message.setSubject(subject);
			message.setSentDate(new Date());

			// Set the email message text.
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setContent(bodyText, "text/html");
//			messagePart.setText(bodyText);
			Multipart multipart = new MimeMultipart();
			// Set the email attachment file
			if (fileObj != null) {
				MimeBodyPart attachmentPart = new MimeBodyPart();
				FileDataSource fileDataSource = new FileDataSource(fileObj);

				logger.debug("asfdhgfgh    " + fileDataSource.getName());
				logger.debug("asfdhgfgh    " + fileObj.getAbsolutePath());
				attachmentPart.attachFile(fileObj);
				attachmentPart.setFileName(realFileName);
				multipart.addBodyPart(attachmentPart);
			}
			multipart.addBodyPart(messagePart);

			// set the all parts into message
			message.setContent(multipart);

			message.setSender(new InternetAddress(SMTP_AUTH_USER));
			// addressTo[i]=new InternetAddress(emailAdd);
			message.setRecipients(RecipientType.TO, addressTo);
			Transport.send(message);
			logger.debug("mail sending completed to "
					+ Arrays.asList(addressTo));

			tr.close();
			logger.debug("trnasport closed successfully ....");
		}catch(AuthenticationFailedException AFE){
			LMSException LMSe = new LMSException("Mailing May be disbaled from property file",AFE);
			logger.debug(LMSe.getMessage());
			logger.debug(AFE.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace();
			flag = true;
		} finally {
			if (flag && count == 1) {
				flag = false;
				sendEmail(this.to, this.subject, this.bodyText, this.filename);
			}
		}
	}

	public void sendEmail(List<String> to, String subject, String bodyText,
			String filename) {
		count = count + 1;
		Authenticator auth = new SMTPAuthenticator();
		try {
			Session session = Session.getInstance(props, auth);
			Transport tr = session.getTransport("smtp");
			tr.connect(SMTP_HOST_NAME, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			logger.debug("connected***********");
			Address addressTo[] = new Address[to.size()];
			for (int i = 0; i < to.size(); i++) {
				addressTo[i] = new InternetAddress(to.get(i));
			}

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SMTP_AUTH_USER));

			// set the message subject
			message.setSubject(subject);
			message.setSentDate(new Date());

			// Set the email message text.
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setContent(bodyText, "text/html");

			Multipart multipart = new MimeMultipart();
			// Set the email attachment file
			if (filename != "") {
				MimeBodyPart attachmentPart = new MimeBodyPart();

				FileDataSource fileDataSource = new FileDataSource(filename) {
					@Override
					public String getContentType() {
						return "application/octet-stream";
					}
				};
				attachmentPart.setDataHandler(new DataHandler(fileDataSource));
				attachmentPart.setFileName(filename);
				multipart.addBodyPart(attachmentPart);
			}
			multipart.addBodyPart(messagePart);

			// set the all parts into message
			message.setContent(multipart);

			message.setSender(new InternetAddress(SMTP_AUTH_USER));
			// addressTo[i]=new InternetAddress(emailAdd);
			message.setRecipients(RecipientType.TO, addressTo);
			Transport.send(message);
			logger.debug("mail sending completed to "
					+ Arrays.asList(addressTo));

			tr.close();
			logger.debug("trnasport closed successfully ....");
		}catch(AuthenticationFailedException AFE){
			LMSException LMSe =  new LMSException("Mailing May Be Disabled From Property File!", AFE);
			logger.debug(LMSe.getMessage());
			logger.debug(AFE.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace();
			flag = true;
		} finally {
			if (flag && count == 1) {
				flag = false;
				sendEmail(this.to, this.subject, this.bodyText, this.filename);
			}
		}
	}

	public void sendEmailToAgent(List<MailBean> mailbean) {
		count = count + 1;
		Authenticator auth = new SMTPAuthenticator();
		this.mailbean = mailbean;
		try {
			Session session = Session.getInstance(props, auth);
			Transport tr = session.getTransport("smtp");
			tr.connect(SMTP_HOST_NAME, SMTP_AUTH_USER, SMTP_AUTH_PWD);
			logger.debug("connected***********");

			for (MailBean mail : mailbean) {

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(SMTP_AUTH_USER));

				message.setSubject(mail.subject);
				message.setSentDate(new Date());

				// Set the email message text.
				MimeBodyPart messagePart = new MimeBodyPart();
				messagePart.setContent(mail.boTextBody, "text/html");

				// Set the email attachment file
				MimeBodyPart attachmentPart = new MimeBodyPart();
				FileDataSource fileDataSource = new FileDataSource(
						mail.AGENT_FILE_NAME) {

					@Override
					public String getContentType() {
						return "application/octet-stream";
					}
				};

				attachmentPart.setDataHandler(new DataHandler(fileDataSource));
				attachmentPart.setFileName(mail.AGENT_FILE_NAME);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messagePart);
				multipart.addBodyPart(attachmentPart);
				message.setSender(new InternetAddress(SMTP_AUTH_USER));

				// set the all parts into message
				message.setContent(multipart);

				message.setRecipient(RecipientType.TO, new InternetAddress(
						mail.reciepient.toLowerCase()));
				Transport.send(message);
				logger.debug("mail sending completed to "
						+ mail.reciepient.toLowerCase());
			}

			tr.close();
			logger.debug("tranasport closed successfully ....");

		}catch(AuthenticationFailedException AFE){
			LMSException LMSe = new LMSException("Mailing may be disabled from property file",AFE);
			logger.debug(LMSe.getLocalizedMessage());
			logger.debug(AFE.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			flag = true;
		} finally {
			if (flag && count == 1) {
				flag = false;
				sendEmailToAgent(this.mailbean);

			}
		}

	}

	private void setProperties(String FROM, String PASSWORD) {
		SMTP_AUTH_USER = FROM;
		SMTP_AUTH_PWD = PASSWORD;
		props = System.getProperties();
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
	}

}
