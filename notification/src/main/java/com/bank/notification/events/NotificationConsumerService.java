package com.bank.notification.events;

import com.bank.notification.enums.ChannelTypeEnum;
import com.bank.notification.service.NotificationService;
import com.finance.common.dto.NotificationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationConsumerService {
	@Autowired
	private NotificationService notificationService;

	@KafkaListener(topics = "sms-notification", groupId = "notification")
	public void listenToSMS(NotificationDTO notificationDTO) {
		try {
			log.info("NotificationConsumerService: Sending SMS notification");
			notificationService.sendNotification(notificationDTO, ChannelTypeEnum.SMS);
		} finally {
			log.info("SMS notification sent");
		}
	}

	@KafkaListener(topics = "mail-notification", groupId = "notification")
	public void listenToMail(NotificationDTO notificationDTO) {
		try {
			log.info("NotificationConsumerService: Sending Mail notification");
			notificationService.sendNotification(notificationDTO, ChannelTypeEnum.EMAIL);
		} finally {
			log.info("Email notification sent");
		}
	}
}
