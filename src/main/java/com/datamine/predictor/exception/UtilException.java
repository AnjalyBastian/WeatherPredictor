package com.datamine.predictor.exception;

public class UtilException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public UtilException(String message){
		this.message = message;
	}
	
	public UtilException(Throwable cause, String message){
		super(cause);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
