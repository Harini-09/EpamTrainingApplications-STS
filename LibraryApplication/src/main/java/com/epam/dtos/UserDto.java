package com.epam.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDto {
	
	@Size(min=5,max=20,message="User Name length should be between 5 and 20")
	private String username;
	@Email(message="The email format is not matched. Please enter a valid email")
	private String email;
	@Size(min=3,max=20,message="The name length should be between 3 and 20")
	private String name;

}
