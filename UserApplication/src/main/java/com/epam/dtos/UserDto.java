package com.epam.dtos;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class UserDto {
	
	@Size(min=5,max=20,message="User Name length should be between 5 and 20")
	private String username;
	@Email(message="The email format is not matched. Please enter a valid email")
	private String email;
	@Size(min=3,max=20,message="The name length should be between 3 and 20")
	private String name;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
}
