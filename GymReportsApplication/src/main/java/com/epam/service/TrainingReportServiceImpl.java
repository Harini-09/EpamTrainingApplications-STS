package com.epam.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.model.TrainingReport;
import com.epam.repository.TrainingReportRepository;
import com.epam.utilities.TrainingReportValueMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainingReportServiceImpl implements TrainingReportService{
	
	@Autowired
	TrainingReportRepository reportRepo;
	
	@Autowired
	TrainingReportValueMapper mapper;

	@KafkaListener(topics="report_topic",groupId="myGroup")
	@Transactional
	@Override
	public void saveTrainingReport(TrainingReportDto trainingReportDto) {
		log.info("Entered into saveTrainingReport() method to save a Training Report");
		TrainingReport trainingReport = reportRepo.findById(trainingReportDto.getTrainerUserName()).orElseGet(()->{
			return mapper.convertToTrainingReport(trainingReportDto);
		});
		
		long year = trainingReportDto.getDate().getYear();
		long month = trainingReportDto.getDate().getMonthValue();
		long day = trainingReportDto.getDate().getDayOfMonth();
		long duration = trainingReportDto.getDuration();
		
		trainingReport.getTrainingSummary()
					  .computeIfAbsent(year, k -> new HashMap<>())
					  .computeIfAbsent(month, k -> new HashMap<>())
					  .computeIfAbsent(day, k -> new HashMap<>())
					  .put(trainingReportDto.getDate().toString(), duration);

		reportRepo.save(trainingReport);
	} 

	@Override
	public SummaryReportDto getTrainingReport(String username) throws Exception{
		log.info("Entered into getTrainingReport() method to get a Training Report");
		TrainingReport trainingReport = reportRepo.findById(username).orElseThrow(()-> new Exception("The user with this username doesn't have a record!!"));
		return mapper.convertToSummaryReportDto(trainingReport, username);
	}
 
	
}
