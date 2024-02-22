package com.epam.customexceptions;

@SuppressWarnings("serial")
public class TrainingReportException extends Exception{

	public TrainingReportException() {
	
	}
	
	public TrainingReportException(String message) {
		super(message);
	}


}
