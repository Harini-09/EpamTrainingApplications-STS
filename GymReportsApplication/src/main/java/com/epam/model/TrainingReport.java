package com.epam.model;

import java.util.Map;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection="trainingreport")
public class TrainingReport {

	@Id
	private String userName;
	private String firstName;
	private String lastName;
	private boolean isTrainerActive;
	private String email;
	private Map<Long,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;
}

/*
 * <2023,<3,<21,<2023-03-21,60>>>>
 * <2023,<6,<21,<2023-06-21,45>>>>
 */
