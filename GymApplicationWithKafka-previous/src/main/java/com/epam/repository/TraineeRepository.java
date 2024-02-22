package com.epam.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Trainee;
import com.epam.entities.Training;

public interface TraineeRepository extends CrudRepository<Trainee,Integer>{
	
	@Query("SELECT t FROM Training t " +
            "JOIN t.trainee tr " +
            "JOIN t.trainer tnr " +
            "JOIN t.trainingType tt " +
            "WHERE tr.user.userName = :username " +
            "AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom) " +
            "AND (:periodTo IS NULL OR t.trainingDate <= :periodTo) " +
            "AND (:trainerName IS NULL OR tnr.user.userName LIKE %:trainerName%) " +
            "AND (:trainingType IS NULL OR tt.trainingTypeName LIKE %:trainingType%)")
     List<Training> findTrainingsForTrainee(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType);
	
}
