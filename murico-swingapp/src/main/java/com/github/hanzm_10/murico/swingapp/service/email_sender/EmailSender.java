package com.github.hanzm_10.murico.swingapp.service.email_sender;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailSender {
	public static void emailSender(String toEmail) {
		final String username = "aaronragudos.com";
		final String password = "";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.smtp2go.com");
		props.put("mail.smtp.port", "2525"); // 8025, 587 and 25 can also be used.
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			Multipart mp = new MimeMultipart("alternative");
			BodyPart textmessage = new MimeBodyPart();
			textmessage.setText("It is a text message n");
			BodyPart htmlmessage = new MimeBodyPart();
			htmlmessage.setContent("It is a html message.", "text/html");
			mp.addBodyPart(textmessage);
			mp.addBodyPart(htmlmessage);
			message.setFrom(new InternetAddress("murico@aaronragudos.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Java Mail using SMTP2GO");
			message.setContent(mp);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * Testing public static void main(String[] args) {
	 * emailSender("bballjaron@gmail.com"); }
	 */

	/*public static void main(String[] args) {
		emailSender("medinaaaronangelo@gmail.com");
	}*/
}
