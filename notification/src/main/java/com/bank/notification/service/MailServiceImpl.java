package com.bank.notification.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MailServiceImpl implements MailService {
	@Override
	public void sendMail(String message, String email) {
		log.info("Sending mail to {} with message {}", email, message);
	}
}
