/**
 * @fileName MailServiceImpl.java
 * @author shashi
 */

package com.quadratyx.dispatcher.service.impl;

import com.quadratyx.dispatcher.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	@Autowired
	private JavaMailSender emailSender;
	@Value("${spring.mail.username}")
	private String senderMail;

	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		try {
			String[] recipients=to.split(",");
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderMail);
			message.setTo(recipients);
			message.setSubject(subject);
			message.setText(text);
			emailSender.send(message);
		} catch ( MailException exception ) {
			logger.error(exception.getMessage());
		}
	}

	@Override
	public boolean sendHtmlMessage(String senderMailId,String to, String subject, String html, String cc) {
		try {
			String[] recipients=to.split(",");
			String[] ccs=cc.split(",");
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");
			helper.setFrom(senderMailId);
			helper.setTo(recipients);
			helper.setCc(ccs);
			helper.setSubject(subject);
			helper.setText(html,true);
			emailSender.send(message);
			return true;
		} catch ( Exception exception ) {
			logger.error(exception.getMessage(),exception);
			return false;
		}
	}

	@Override
	public boolean sendHtmlMessageWithAttachment(String senderMailId,String to,String subject, String html, String pathToAttachment, String cc,String attachFilename) {
		try {
			String[] recipients=to.split(",");
			String[] ccs=cc.split(",");
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");
			helper.setFrom(senderMailId);
			helper.setTo(recipients);
			helper.setCc(ccs);
			helper.setSubject(subject);
			helper.setText(html,true);
			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
			helper.addAttachment(attachFilename, file);
			emailSender.send(message);
			return true;
		} catch ( MessagingException e ) {
			logger.error(e.getMessage());
			return false;
		}
	}
}

