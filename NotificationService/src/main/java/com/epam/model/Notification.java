package com.epam.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection="notification")
public class Notification {

	String fromEmail;
	List<String> toEmails;
	List<String> ccEmails;
	String body;
	String status;
	String remarks;
	
	
}

