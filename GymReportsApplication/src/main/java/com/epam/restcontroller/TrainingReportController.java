package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.service.TrainingReportServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TrainingReportController {

	@Autowired
	TrainingReportServiceImpl trainingReportService;
	
	@PostMapping("/trainingreport")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveTrainingReport(@RequestBody @Valid TrainingReportDto trainingReportDto) {
		log.info("Received a Post Request to save a Training Report");
		trainingReportService.saveTrainingReport(trainingReportDto);
	}
	
	@GetMapping("/trainingreport")
	public ResponseEntity<SummaryReportDto> getTrainingReport(@RequestParam @NotEmpty String username) throws Exception {
		log.info("Received a Get Request to get a Training Report");
		return new ResponseEntity<>(trainingReportService.getTrainingReport(username),HttpStatus.OK);
	}
}
 