package com.bank.notification.Entity;

import com.bank.notification.enums.NotificationStatusEnum;
import com.finance.common.entity.BaseEntity;
import com.finance.common.enums.NotificationTypeEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "NOTIFICATION")
public class NotificationEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_ID_SEQ")
	@SequenceGenerator(name = "NOTIFICATION_ID_SEQ", sequenceName = "NOTIFICATION_ID_SEQ", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID")
	private Long id;

	@Column(name = "CUSTOMER_CODE", nullable = false)
	private String customerCode;

	@Column(name = "MESSAGE", nullable = false)
	private String message;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private NotificationStatusEnum status;

	@Column(name = "STATUS_MESSAGE")
	private String statusMessage;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "NOTIFICATION_TYPE", nullable = false)
	private NotificationTypeEnum notificationType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(NotificationStatusEnum status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public NotificationTypeEnum getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationTypeEnum notificationType) {
		this.notificationType = notificationType;
	}
}
