package com.epam.controllertest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.response.NotificationDto;
import com.epam.restcontroller.NotificationController;
import com.epam.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testAddNewNotificationLog() throws Exception {
        NotificationDto notificationDto = new NotificationDto("New Notification", "Details",List.of(),List.of());
                mockMvc.perform(post("/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(notificationDto)))
                .andExpect(status().isOk());

        verify(notificationService, times(1)).addNewNotificationLog(notificationDto);
    }
} 
