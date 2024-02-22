package com.epam.dtos.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileUpdate {

	@NotEmpty(message="The username should not be empty")
	private String username;
	@NotEmpty(message="The first Name should not be null or empty")
	@Size(min=5,max=50,message="The first Name should have a minimum of 5 characters")
	private String firstName;
	@NotEmpty(message="The last Name should not be null or empty")
	@Size(min=5,max=50,message="The last Name should have a minimum of 5 characters")
	private String lastName;
	private LocalDate dateOfBirth;
	@NotEmpty(message="The address should not be empty")
	private String address;
	private boolean isActive;

}
