package com.fmi.java.cp;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	private static final String emailAddress = "authenticationserver68@gmail.com";
	private static final String emailPassword = "serverJavaProject";

	public static void sendEmail(String emailRecipient) {

		Session session = createEmailSession();

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecipient));
			message.setSubject("Automatically generated registration email");
			message.setText(
					"You have successfully registrated in our authentication server. " + "Thank you for choosing us.");

			Transport.send(message);

			System.out.println("Registration email has been sent to " + emailRecipient);

		} catch (MessagingException e) {
			System.out.println("There has been an issue while sending the registration email to " + emailRecipient
					+ " The issue is: ");
			System.out.println(e.getMessage());
		}
	}

	private static Session createEmailSession() {

		Properties emailProperties = createEmailProperties();
		Authenticator emailAuthenticator = createAuthenticator();

		Session session = javax.mail.Session.getInstance(emailProperties, emailAuthenticator);
		return session;
	}

	private static Properties createEmailProperties() {

		Properties emailProperties = new Properties();
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.host", "smtp.gmail.com");
		emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		emailProperties.put("mail.smtp.port", "587");

		return emailProperties;
	}

	private static Authenticator createAuthenticator() {
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailAddress, emailPassword);
			}
		};
	}
}
