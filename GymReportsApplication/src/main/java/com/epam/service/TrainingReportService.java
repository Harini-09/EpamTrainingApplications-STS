package com.epam.service;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;

public interface TrainingReportService {

	public void saveTrainingReport(TrainingReportDto trainingReportDto);
	public SummaryReportDto getTrainingReport(String username) throws Exception;
}
