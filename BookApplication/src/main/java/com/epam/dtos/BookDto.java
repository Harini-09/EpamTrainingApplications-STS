package com.epam.dtos;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class BookDto {
	@NotBlank(message="Book name shouldn't be blank")
	private String bookname;
	@NotBlank(message="Publisher shouldn't be blank")
	private String publisher;
	@NotBlank(message="Author name shouldn't be blank")
	private String author;
	@Positive(message="The quantity of the no of books should be a positive number greater than zero")
	@NotNull(message="Null is an invalid entry.Please enter a valid positive number greater than zero")
	private int quantity;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
	
}
