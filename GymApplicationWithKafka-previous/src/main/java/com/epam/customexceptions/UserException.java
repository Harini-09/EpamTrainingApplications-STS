package com.epam.customexceptions;

@SuppressWarnings("serial")
public class UserException extends Exception{

	public UserException(String message) {
		super(message);
	}

}
