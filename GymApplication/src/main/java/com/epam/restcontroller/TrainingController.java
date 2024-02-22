package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.proxy.TrainingReportProxy;
import com.epam.service.TrainingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/training")
public class TrainingController {

	@Autowired
	TrainingService trainingService;
	
	@Autowired
	TrainingReportProxy trainingReportProxy;
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void saveTraining(@RequestBody @Valid TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException {
		log.info("Received POST request to save a Training");
		TrainingReportDto trainingReportDto = trainingService.saveTraining(trainingDetails);
		trainingReportProxy.saveTrainingReport(trainingReportDto);
	}

	@GetMapping("/trainingreport")
	public ResponseEntity<SummaryReportDto> getTrainingReport(@RequestParam @NotEmpty String username) throws Exception{
		return trainingReportProxy.getTrainingReport(username);
	}

}
