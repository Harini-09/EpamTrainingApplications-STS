package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.model.TrainingReport;
import com.epam.utilities.TrainingReportValueMapper;

public class TrainingReportMapperTest {
	@InjectMocks
    private TrainingReportValueMapper trainingReportValueMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertToTrainingReport() {
        TrainingReportDto reportDto = new TrainingReportDto();
        reportDto.setTrainerUserName("trainer_user");
        reportDto.setTrainerFirstName("Trainer");
        reportDto.setTrainerLastName("User");
        reportDto.setTrainerActive(true);
        reportDto.setTrainerEmail("trainer@example.com");

        TrainingReport trainingReport = trainingReportValueMapper.convertToTrainingReport(reportDto);

        assertEquals("trainer_user", trainingReport.getUserName());
        assertEquals("Trainer", trainingReport.getFirstName());
        assertEquals("User", trainingReport.getLastName());
        assertTrue(trainingReport.isTrainerActive());
        assertEquals("trainer@example.com", trainingReport.getEmail());
        assertNotNull(trainingReport.getTrainingSummary());
        assertTrue(trainingReport.getTrainingSummary().isEmpty());
    }

    @Test
    public void testConvertToSummaryReportDto() {
        TrainingReport trainingReport = TrainingReport.builder()
                .firstName("Trainer")
                .lastName("User")
                .isTrainerActive(true)
                .email("trainer@example.com")
                .trainingSummary(new HashMap<>())
                .build();

        SummaryReportDto summaryReportDto = trainingReportValueMapper.convertToSummaryReportDto(trainingReport, "trainer_user");

        assertEquals("trainer_user", summaryReportDto.getUserName());
        assertEquals("Trainer", summaryReportDto.getFirstName());
        assertEquals("User", summaryReportDto.getLastName());
        assertTrue(summaryReportDto.isTrainerActive());
        assertEquals("trainer@example.com", summaryReportDto.getEmail());
        assertNotNull(summaryReportDto.getTrainingSummary());
        assertTrue(summaryReportDto.getTrainingSummary().isEmpty());
    }


}
