package com.epam.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.AssociateException;
import com.epam.dtos.AssociateDto;
import com.epam.service.AssociateService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rd/associates")
@Slf4j
public class AssociateRestController {

	@Autowired
	AssociateService associateService;
	
	@PostMapping
	public ResponseEntity<AssociateDto> saveAssociate(@RequestBody @Valid AssociateDto associateDto){
		log.info("Received POST request to save the associate");
		return new ResponseEntity<>(associateService.saveAssociate(associateDto),HttpStatus.CREATED);
	} 
	
	@GetMapping("/{gender}")
	public ResponseEntity<List<AssociateDto>> getAssociatesByGender(@PathVariable("gender") String gender){
		log.info("Received GET request to retrieve all the associates by gender");
		return new ResponseEntity<>(associateService.getAssociateByGender(gender),HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<AssociateDto> updateAssociate(@RequestParam int id,@RequestBody @Valid AssociateDto associateDto) throws AssociateException{
		log.info("Received PUT request to update an associate with associate id - {}, with the updated details - {}",id,associateDto);
		return new ResponseEntity<>(associateService.updateAssociate(id, associateDto),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void deleteAssociate(@PathVariable("id") int id) {
		log.info("Received DELETE request to delete an associate with associate id - {}",id);
		associateService.deleteAssociate(id);
	}
}
