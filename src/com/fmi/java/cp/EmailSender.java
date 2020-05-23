package com.fmi.java.cp;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {
	
	private static final String emailAddress = "authenticationserver55@gmail.com";
    private static final String emailPassword = "serverJavaProject";
	
	public static void sendEmail(String emailRecipient) {
		
        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(emailProperties,
        		new javax.mail.Authenticator() {
            		protected PasswordAuthentication getPasswordAuthentication() {
            			return new PasswordAuthentication(emailAddress, emailPassword);
            		}
          			});

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("authenticationserver55@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecipient));
            message.setSubject("Automatically generated registration email");
            message.setText("You have successfully registrated in our authentication server. "
            		+ "Thank you for choosing us.");

            Transport.send(message);

            System.out.println("Registration email has been sent to " + emailRecipient);

        } catch (MessagingException e) {
        	e.printStackTrace();
            System.out.println("Issue while sending the registration email");
        }
    }
}
