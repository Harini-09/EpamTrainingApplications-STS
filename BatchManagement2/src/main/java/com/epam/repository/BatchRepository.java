package com.epam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Batch;

public interface BatchRepository extends CrudRepository<Batch,Integer>{

	public Optional<Batch> findByBatchName(String batchName);
}
