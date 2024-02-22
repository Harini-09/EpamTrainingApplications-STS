package com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.epam.dtos.response.NotificationDto;
import com.epam.model.Notification;
import com.epam.repository.EmailRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	EmailRepository emailRepository;
	
	@Value("${from.mail}")
	private String fromMail;
	
	@KafkaListener(topics="notification_topic",groupId="myGroup")
	@Override
	public void addNewNotificationLog(NotificationDto notificationDto) {
		
		log.info("Entered into addNewNotificationLog() method to save a new notification details and send to the respective user.");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromMail);
        mailMessage.setTo(notificationDto.getToEmails().toArray(new String[0]));
        mailMessage.setCc(notificationDto.getCcEmails().toArray(new String[0]));
        mailMessage.setSubject(notificationDto.getSubject());
        mailMessage.setText(notificationDto.getBody());

        Notification notification = Notification.builder()
        										.fromEmail(fromMail)
        										.toEmails(notificationDto.getToEmails())
        										.ccEmails(notificationDto.getCcEmails()).body(notificationDto.getBody())
        				 						.build();
        try {
            javaMailSender.send(mailMessage);
            notification.setStatus("Mail Sent");
            notification.setRemarks("Mail sent successfully!!!");
        } catch (MailException e) {
            notification.setStatus("Mail failed");
            notification.setRemarks("Failed to send a mail " + e.getMessage());
        }
        emailRepository.save(notification);
		
	}
	

}
