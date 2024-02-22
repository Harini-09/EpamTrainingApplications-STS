package com.epam.service;

import java.util.List;

import com.epam.customexceptions.BatchException;
import com.epam.dtos.BatchDto;

public interface BatchService {

	public BatchDto saveBatch(BatchDto batchDto);
	public void deleteBatch(int id) throws BatchException;
	public BatchDto updateBatch(int id,BatchDto batchDto) throws BatchException;
	public List<BatchDto> viewBatches();
	
}
