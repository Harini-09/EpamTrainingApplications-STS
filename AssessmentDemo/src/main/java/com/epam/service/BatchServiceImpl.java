package com.epam.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.BatchException;
import com.epam.dtos.BatchDto;
import com.epam.model.Batch;

import repository.BatchRepo;

@Service
public class BatchServiceImpl implements BatchService{

	@Autowired
	BatchRepo batchRepo;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public BatchDto saveBatch(BatchDto batchDto) {
		Batch batch = modelMapper.map(batchDto,Batch.class);
		batch = batchRepo.save(batch);
		return modelMapper.map(batch, BatchDto.class);
	}

	@Override
	public void deleteBatch(int id) {
		batchRepo.deleteById(id);
	}

	@Override
	public BatchDto updateBatch(int id, BatchDto batchDto) throws BatchException {
		batchRepo.findById(id).isPresent() {
			throw new BatchException("Batch Not Found!!!");
		}
		Batch batch = modelMapper.map(batchDto,Batch.class);
		batch = batchRepo.save(batch);
		return modelMapper.map(batch, BatchDto.class);
	}

	@Override
	public List<Batch> viewBatches() {
		return (List<Batch>) batchRepo.findAll();
	}
	
	
}
