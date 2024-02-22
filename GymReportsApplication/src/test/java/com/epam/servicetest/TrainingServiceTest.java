package com.epam.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.model.TrainingReport;
import com.epam.repository.TrainingReportRepository;
import com.epam.service.TrainingReportServiceImpl;
import com.epam.utilities.TrainingReportValueMapper;

public class TrainingServiceTest {
	@InjectMocks
	private TrainingReportServiceImpl trainingReportService;

	@Mock
	private TrainingReportRepository reportRepository;

	@Mock
	private TrainingReportValueMapper valueMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testSaveTrainingReport_NewTrainingReport() {
		TrainingReportDto mockTrainingReportDto = new TrainingReportDto();
		mockTrainingReportDto.setTrainerUserName("trainer123");
		mockTrainingReportDto.setDate(LocalDate.of(2023, 8, 10));
		mockTrainingReportDto.setDuration(60l);
		Map<Long, Map<Long, Map<Long, Map<String, Long>>>> map = new HashMap<>();
		TrainingReport mockTrainingReport = TrainingReport.builder().trainingSummary(map).build();

		when(reportRepository.findById(anyString())).thenReturn(Optional.empty());
		when(valueMapper.convertToTrainingReport(any(TrainingReportDto.class))).thenReturn(mockTrainingReport);

		trainingReportService.saveTrainingReport(mockTrainingReportDto);

		verify(reportRepository).findById(mockTrainingReportDto.getTrainerUserName());
		verify(valueMapper).convertToTrainingReport(mockTrainingReportDto);
		verify(reportRepository).save(mockTrainingReport);
	}

	@Test
	public void testGetTrainingReport_Success() throws Exception {
		String username = "trainer123";

		TrainingReportDto mockTrainingReportDto = new TrainingReportDto();
		mockTrainingReportDto.setTrainerUserName("trainer123");
		mockTrainingReportDto.setDate(LocalDate.of(2023, 8, 10));
		mockTrainingReportDto.setDuration(60l);
		Map<Long, Map<Long, Map<Long, Map<String, Long>>>> map = new HashMap<>();
		TrainingReport mockTrainingReport = TrainingReport.builder().trainingSummary(map).build();

		SummaryReportDto mockSummaryReportDto = new SummaryReportDto();

		when(reportRepository.findById("trainer123")).thenReturn(Optional.of(mockTrainingReport));
		when(valueMapper.convertToSummaryReportDto(any(TrainingReport.class), eq(username)))
				.thenReturn(mockSummaryReportDto);

		SummaryReportDto result = trainingReportService.getTrainingReport(username);

		verify(reportRepository).findById(username);
		verify(valueMapper).convertToSummaryReportDto(mockTrainingReport, username);
		assertEquals(mockSummaryReportDto, result);
	}

	@Test
	public void testGetTrainingReport_UserNotFoundException() {
		String username = "nonExistentUser";

		when(reportRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(Exception.class, () -> trainingReportService.getTrainingReport(username));

		verify(reportRepository).findById(username);
	}
}
