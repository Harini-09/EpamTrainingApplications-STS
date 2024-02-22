package com.epam.dtos.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingDto {
	
	@NotEmpty(message="The username should not be empty or null")
	private String username;
	@Nullable
	private LocalDate periodFrom;
	@Nullable
	private LocalDate periodTo;
	private String traineeName;
}
