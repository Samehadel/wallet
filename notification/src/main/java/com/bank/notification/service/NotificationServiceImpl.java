package com.bank.notification.service;

import com.bank.notification.Entity.NotificationEntity;
import com.bank.notification.enums.ChannelTypeEnum;
import com.bank.notification.repository.NotificationRepository;
import com.finance.common.dto.NotificationDTO;
import com.finance.common.enums.NotificationTypeEnum;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private SMSService smsService;

	@Autowired
	private MailService emailService;

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public void sendNotification(NotificationDTO notificationDTO, ChannelTypeEnum channelType) {
		try {
			log.info("Sending notification to {} using channel {}", notificationDTO.getReceiverIdentifier(), channelType);
			String message = notificationDTO.getMessage();
			String receiverIdentifier = notificationDTO.getReceiverIdentifier();
			if (channelType == ChannelTypeEnum.SMS) {
				smsService.sendSMS(message, receiverIdentifier);
			} else if (channelType == ChannelTypeEnum.EMAIL) {
				emailService.sendMail(message, receiverIdentifier);
			}
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setCustomerCode(notificationDTO.getCustomerCode());
			notificationEntity.setNotificationType(NotificationTypeEnum.ACCOUNT_REGISTRATION);
			notificationEntity.setMessage(message);
			notificationRepository.save(notificationEntity);
		} finally {
			log.info("Notification sent to {} using channel {}", notificationDTO.getReceiverIdentifier(), channelType);
		}
	}
}