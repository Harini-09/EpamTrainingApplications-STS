package com.epam.dtos.response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryReportDto {

	private String userName;
	private String firstName;
	private String lastName;
	private boolean isTrainerActive;
	private String email;
	private Map<Long,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;
	
}
