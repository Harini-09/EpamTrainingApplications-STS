package com.epam.dtos;

import com.epam.entities.Batch;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor 
@EqualsAndHashCode
@ToString
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class AssociateDto {
	
	@NotBlank(message = "The associate name should not be blank.Please enter a valid associate name.")
	String associateName;
	@Email
	String email;
	@Size(min=1,max=1,message = "The gender should be a single character (M/F). Please enter a valid gender.")
	String gender;
	@Size(min=3,max=20,message = "The college name should be minimum of 3 characters and a maximum of 20 characters.")
	String college;
	@NotBlank(message = "The status name should not be blank.Please enter a valid status name.")
	String status;
	Batch batch;
	

}
