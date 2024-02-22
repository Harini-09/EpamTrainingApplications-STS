package com.epam.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerDto {

	private String userName;
	private String firstName;
	private String lastName;
	private String trainingType;

}
