package com.epam.proxy;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.dtos.response.SummaryReportDto;
import com.epam.dtos.response.TrainingReportDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@FeignClient(name="gymreports-microservice")
@LoadBalancerClient(name="gymreports-microservice")
public interface TrainingReportProxy {
	
	@PostMapping("/trainingreport")
	@ResponseStatus(HttpStatus.CREATED)
	public void saveTrainingReport(@RequestBody @Valid TrainingReportDto trainingReportDto);
	
	@GetMapping("/trainingreport")
	public ResponseEntity<SummaryReportDto> getTrainingReport(@RequestParam @NotEmpty String username) throws Exception;

}
