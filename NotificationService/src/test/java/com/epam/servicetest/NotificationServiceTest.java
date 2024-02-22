package com.epam.servicetest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.epam.dtos.response.NotificationDto;
import com.epam.model.Notification;
import com.epam.repository.EmailRepository;
import com.epam.service.NotificationServiceImpl;

public class NotificationServiceTest {
	@InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailRepository emailRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testAddNewNotificationLog_Success() {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        NotificationDto notificationDto = NotificationDto.builder()
        												 .toEmails(List.of("abc@gmail.com"))
        												 .body("hello")
        												 .ccEmails(List.of("aga@gmail.com"))
        												 .subject("subject").build();
        												 

        doNothing().when(javaMailSender).send(mailMessage);
        Notification n=null;
        when(emailRepository.save(n)).thenReturn(n);
        notificationService.addNewNotificationLog(notificationDto);

        // Verify interactions and assertions as needed
    }

}
