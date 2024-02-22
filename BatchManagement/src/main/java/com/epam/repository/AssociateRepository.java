package com.epam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Associate;

public interface AssociateRepository extends CrudRepository<Associate, Integer>{

	public Iterable<Associate> findAllByGender(String gender);
}
