package net.jadefisher.monkeystatus.alert;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.jadefisher.monkeystatus.exception.AlertSendingException;
import net.jadefisher.monkeystatus.model.alert.Alert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlertSenderImpl implements AlertSender {

	@Value("${monkeystatus.smtp.host}")
	private String smtpHost;

	@Value("${monkeystatus.smtp.fromAddress}")
	private String fromAddress;

	private Session session;

	@PostConstruct
	public void init() {
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHost);

		// Get the default Session object.
		session = Session.getDefaultInstance(properties);
	}

	@Override
	public void sendAlert(Alert alert, List<String> recipients)
			throws AlertSendingException {
		Message msg = createEmail(this.session, recipients, this.fromAddress,
				alert.getSubject(), alert.getMessage());
		send(msg);
	}

	private static MimeMessage createEmail(final Session session,
			final List<String> recipients, final String from,
			final String subject, final String body)
			throws AlertSendingException {
		MimeMessage message = null;
		try {
			// Create a default MimeMessage object.
			message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			for (String recipient : recipients)
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(body);
		} catch (MessagingException mex) {
			throw new AlertSendingException("Couldn't create email", mex);
		}
		return message;
	}

	private static void send(final Message message)
			throws AlertSendingException {
		try {
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			throw new AlertSendingException("Couldn't send email", mex);
		}
	}
}
