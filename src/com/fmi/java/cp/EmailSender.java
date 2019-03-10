package com.fmi.java.cp;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {
	
	public static void sendEmail(String to)
    {
        final String username = "authenticationserver55@gmail.com";
        final String password = "serverJavaProject";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props,
        		new javax.mail.Authenticator() {
            		protected PasswordAuthentication getPasswordAuthentication() {
            			return new PasswordAuthentication(username, password);
            		}
          			});

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("authenticationserver55@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Automatically generated registration email");
            message.setText("You have successfully registrated in our authentication server. "
            		+ "Thank you for choosing us.");

            Transport.send(message);

            System.out.println("Registration email has been sent to " + to);

        } catch (MessagingException e) {
        	e.printStackTrace();
            System.out.println("Issue while sending the registration email");
        }
    }
}
