package com.epam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.epam.model.TrainingReport;

public interface TrainingReportRepository extends MongoRepository<TrainingReport,String>{

}
