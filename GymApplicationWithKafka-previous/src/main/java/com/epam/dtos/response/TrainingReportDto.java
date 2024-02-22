package com.epam.dtos.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingReportDto {

	String trainerUserName;
	String trainerFirstName;
	String trainerLastName;
	boolean isTrainerActive;
	String trainerEmail;
	LocalDate date;
	Long duration;

}
