package com.epam.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.AssociateException;
import com.epam.dtos.AssociateDto;
import com.epam.entities.Associate;
import com.epam.entities.Batch;
import com.epam.repository.AssociateRepository;
import com.epam.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssociateServiceImpl implements AssociateService{

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AssociateRepository associateRepo;
	
	@Autowired
	BatchRepository batchRepo;
	
	public AssociateDto saveAssociate(AssociateDto associateDto) {
		log.info("Entered into the Associate Service - saveAssociate() method to save an associate - {}",associateDto);
		Associate associate = modelMapper.map(associateDto, Associate.class);
		Optional<Batch> batch = batchRepo.findByBatchName(associateDto.getBatch().getBatchName());
		if(batch.isPresent()){
			associate.setBatch(batch.get());
		}
		associate = associateRepo.save(associate);
		return modelMapper.map(associate, AssociateDto.class);
	} 
	 
	public List<AssociateDto> getAssociateByGender(String gender) {
		log.info("Entered into the Associate Service - getAssociateByGender() method to get all the books from the library");
		return ((List<Associate>)associateRepo.findAllByGender(gender)).stream().map(associate->modelMapper.map(associate, AssociateDto.class)).toList();
	}
	 
	public AssociateDto updateAssociate(int id,AssociateDto associateDto) throws AssociateException {
		log.info("Entered into the Associate Service - updateAssociate() method to update an associate with id : {} with the updated details - {}\",id, associateDto");
		if(!associateRepo.existsById(id)) {
			throw new AssociateException("The Associate with the given id doesn't exist.Please enter a valid id!!!");
		} 
		Associate associate = modelMapper.map(associateDto, Associate.class);
		Optional<Batch> batch = batchRepo.findByBatchName(associateDto.getBatch().getBatchName());
		if(batch.isPresent()){
			associate.setBatch(batch.get());
		}
		associate.setId(id); 
		associateRepo.save(associate);
		return modelMapper.map(associate, AssociateDto.class);
	}
	 
	public void deleteAssociate(int id) {
		log.info("Entered into the Associate Service - deleteAssociate() method to delete an associate from the library");
		associateRepo.deleteById(id);
	}
	
}
