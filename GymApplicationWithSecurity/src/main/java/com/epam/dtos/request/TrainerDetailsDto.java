package com.epam.dtos.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDetailsDto {

	@NotEmpty(message="The first Name should not be null or empty")
	@Size(min=5,max=50,message="The first Name should have a minimum of 5 characters")
	private String firstName;
	@NotEmpty(message="The last Name should not be null or empty")
	@Size(min=5,max=50,message="The last Name should have a minimum of 5 characters")
	private String lastName;
	@Nullable
	@Email
	private String email;
	@NotEmpty(message="The Training Type Name should not be null or empty")
	private String trainingTypeName;
}
