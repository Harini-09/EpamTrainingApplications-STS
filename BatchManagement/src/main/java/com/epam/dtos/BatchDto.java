package com.epam.dtos;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.epam.entities.Associate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class BatchDto {
	
	@NotBlank(message = "The batch name should not be blank.Please enter a valid branch name.")
	String batchName;
	@NotBlank(message = "The practise should not be blank.Please enter a valid practise.")
	String practise;
	@DateTimeFormat
	Date startDate;
	@DateTimeFormat
	Date endDate;	
	@JsonIgnore
	List<Associate> associates;
}
