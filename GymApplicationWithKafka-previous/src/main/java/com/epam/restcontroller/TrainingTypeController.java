package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.service.TrainingTypeService;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/trainingType")
public class TrainingTypeController {

	@Autowired
	TrainingTypeService trainingTypeService;
	
	@PostMapping("/{trainingTypeName}")
	@ResponseStatus(HttpStatus.OK)
	public void saveTrainingType(@PathVariable @NotEmpty @Pattern(regexp="fitness|yoga|Zumba|stretching|resistance",message="Invalid Training Type") String trainingTypeName) {
		log.info("Received POST request to save a Training Type");
		trainingTypeService.saveTrainingType(trainingTypeName);
	}
}
