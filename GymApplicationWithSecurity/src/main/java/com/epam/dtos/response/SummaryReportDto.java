package com.epam.dtos.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryReportDto {

	private String userName;
	private String firstName;
	private String lastName;
	private boolean isTrainerActive;
	private String email;
	private Map<Long,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;
	
}
