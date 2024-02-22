package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dtos.response.NotificationDto;
import com.epam.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notification")
@Slf4j
public class NotificationController {

	@Autowired
	NotificationService notificationService;
	
	@PostMapping
	public void addNewNotificationLog(@RequestBody NotificationDto notificationDto) {
		log.info("Received Post request to add a new notification");
		notificationService.addNewNotificationLog(notificationDto);
	}
	
	
}





