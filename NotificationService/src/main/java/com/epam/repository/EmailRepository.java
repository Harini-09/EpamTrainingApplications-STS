package com.epam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.epam.model.Notification;

public interface EmailRepository extends MongoRepository<Notification,Integer>{

}
