package com.epam.restcontroller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dtos.BatchDto;
import com.epam.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rd/batch/")
@Slf4j
public class BatchRestController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	BatchRepository batchRepo;
	
	@GetMapping("{id}")
	public BatchDto getBatchById(@PathVariable int id){
		return modelMapper.map(batchRepo.findById(id),BatchDto.class);
	}
}
