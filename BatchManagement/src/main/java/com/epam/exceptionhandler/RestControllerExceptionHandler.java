package com.epam.exceptionhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.epam.customexceptions.AssociateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex,WebRequest req) {
		List<String> inputErrors = new ArrayList<>();
		ex.getAllErrors().forEach(err->inputErrors.add(err.getDefaultMessage()));
		log.error("MethodArgumentNotValidException occured = {}",inputErrors);
		return new ExceptionResponse(new Date().toString(),HttpStatus.BAD_REQUEST.name(),inputErrors.toString(),req.getDescription(false));
	}
	
	@ExceptionHandler(AssociateException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleAssociateException(AssociateException ex,WebRequest req) {
		log.error("HttpMessageNotReadableException occured = {}",ex.getMessage());
		return new ExceptionResponse(new Date().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),req.getDescription(false));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,WebRequest req) {
		log.error("HttpMessageNotReadableException occured = {}",ex.getMessage());
		return new ExceptionResponse(new Date().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),req.getDescription(false));
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,WebRequest req) {
		log.error("MethodArgumentTypeMismatchException occured = {}",ex.getMessage());
		return new ExceptionResponse(new Date().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),req.getDescription(false));
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT)
	public ExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex,WebRequest req) {
		log.error("DataIntegrityViolationException occured = {}",ex.getMessage());
		return new ExceptionResponse(new Date().toString(),HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),req.getDescription(false));
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse handleRuntimeException(RuntimeException ex,WebRequest req) {
		log.error("RuntimeException occured = {}",ex.getMessage());
		return new ExceptionResponse(new Date().toString(),HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage(),req.getDescription(false));
	}
}


