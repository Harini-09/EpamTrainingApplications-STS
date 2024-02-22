package com.epam.controllertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.restcontroller.TrainingReportController;
import com.epam.service.TrainingReportServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainingReportController.class)
class TrainingReportControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingReportServiceImpl trainingReportService;

    private TrainingReportDto trainingReportDto;
    private SummaryReportDto summaryReportDto;

    @BeforeEach
    void setUp() {
        trainingReportDto = new TrainingReportDto();
        summaryReportDto = new SummaryReportDto();
    }

    @Test
    void saveTrainingReportTest() throws Exception {
        mockMvc.perform(post("/trainingreport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainingReportDto)))
                .andExpect(status().isCreated());
        Mockito.verify(trainingReportService).saveTrainingReport(trainingReportDto);
    }

    @Test
    void getTrainingReportTest() throws Exception {
        String username = "testUsername";
        Mockito.when(trainingReportService.getTrainingReport(username))
                .thenReturn(summaryReportDto);
       mockMvc.perform(get("/trainingreport")
                .param("username", username))
                .andExpect(status().isOk());
    }
}

 


