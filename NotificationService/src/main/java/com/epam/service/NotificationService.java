package com.epam.service;

import com.epam.dtos.response.NotificationDto;

public interface NotificationService {

	public void addNewNotificationLog(NotificationDto notificationDto);

}
