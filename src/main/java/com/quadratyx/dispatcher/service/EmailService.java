package com.quadratyx.dispatcher.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

	void sendSimpleMessage(String to, String subject, String text);
	/**
	 *
	 * @param to :
	 * @param subject :
	 * @param html :
	 */
	boolean sendHtmlMessage(String senderEmailId ,String to, String subject, String html, String cc);

	/**
	 * @param to :
	 * @param subject :
	 * @param text :
	 * @param pathToAttachment :
	 */
	boolean sendHtmlMessageWithAttachment(String senderMailId,String to,String subject, String text, String pathToAttachment, String cc, String attachFilename);
}
