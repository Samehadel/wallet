package com.bank.notification.service;

import com.bank.notification.enums.ChannelTypeEnum;
import com.finance.common.dto.NotificationDTO;

public interface NotificationService {
	void sendNotification(NotificationDTO notificationDTO, ChannelTypeEnum channelType);
}
