package com.epam.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

	@NotEmpty(message="The username should not be empty or null")
	private String username;
	@NotEmpty(message="The password should not be empty or null")
	private String password;

}
