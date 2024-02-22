package com.epam.utilities;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.model.TrainingReport;

@Service
public class TrainingReportValueMapper {

	public TrainingReport convertToTrainingReport(TrainingReportDto trainingReportDto) {
		return TrainingReport.builder()
				 .userName(trainingReportDto.getTrainerUserName())
				 .firstName(trainingReportDto.getTrainerFirstName())
				 .lastName(trainingReportDto.getTrainerLastName())
				 .isTrainerActive(trainingReportDto.isTrainerActive())
				 .email(trainingReportDto.getTrainerEmail())
				 .trainingSummary(new HashMap<>())
				 .build();
	} 
	
	public SummaryReportDto convertToSummaryReportDto(TrainingReport trainingReport,String username) {
		return SummaryReportDto.builder()
				   .userName(username)
				   .firstName(trainingReport.getFirstName())
				   .lastName(trainingReport.getLastName())
				   .isTrainerActive(trainingReport.isTrainerActive())
				   .email(trainingReport.getEmail())
				   .trainingSummary(trainingReport.getTrainingSummary())
				   .build();
	}
} 
