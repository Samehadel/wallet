package com.bank.notification.service;

public interface SMSService {
	void sendSMS(String message, String phoneNumber);
}
