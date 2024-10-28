package com.bank.notification.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SMSServiceImpl implements SMSService {
	@Override
	public void sendSMS(String message, String phoneNumber) {
		log.info("Sending SMS to {} with message {}", phoneNumber, message);
	}
}
