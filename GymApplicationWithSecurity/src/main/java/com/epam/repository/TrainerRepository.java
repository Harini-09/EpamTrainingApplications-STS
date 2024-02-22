package com.epam.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Trainer;
import com.epam.entities.Training;

public interface TrainerRepository extends CrudRepository<Trainer,Integer>{

	@Query("SELECT t FROM Training t " +
            "JOIN t.trainee tr " +
            "JOIN t.trainer tnr " +
            "WHERE tnr.user.userName = :username " +
            "AND (:periodFrom IS NULL OR t.trainingDate >= :periodFrom) " +
            "AND (:periodTo IS NULL OR t.trainingDate <= :periodTo) " +
            "AND (:traineeName IS NULL OR tr.user.userName LIKE %:traineeName%) " )
     List<Training> findTrainingsForTrainer(String username, LocalDate periodFrom, LocalDate periodTo, String traineeName);
}
