package com.github.hanzm_10.murico.swingapp.service.email_sender;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class EmailSender {
		public static void emailSender(String toEmail) {
        final String username = "";
        final String password = "";
        //String toEmail = "qpmdelacruz@tip.edu.ph";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setReplyTo(InternetAddress.parse("support@murico.com"));
            message.setHeader("X-Mailer", "JavaMailer");
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setHeader("X-Priority", "1");
            message.setHeader("X-MSMail-Priority", "High");
            message.setHeader("Importance", "High");

            message.setSubject("Welcome to Murico!");
            message.setText(
                "Hi there,\n\n" +
                "Thank you for signing up with Murico. Your account has been successfully activated.\n" +
                "You can now log in to the app and explore its features.\n\n" +
                "If you have any questions, feel free to reach out.\n\n" +
                "Best regards,\n" +
                "Murico Support Team"
            );

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
		/*
		 * Testing
		 * public static void main(String[] args) {
			emailSender("bballjaron@gmail.com");
		}*/
}
