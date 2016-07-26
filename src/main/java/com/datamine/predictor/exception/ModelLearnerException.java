package com.datamine.predictor.exception;

public class ModelLearnerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ModelLearnerException(String message){
		this.message = message;
	}
	
	public ModelLearnerException(Throwable cause, String message){
		super(cause);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
