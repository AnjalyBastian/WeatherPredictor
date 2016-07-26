package com.datamine.predictor.exception;

public class PropertyLoaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public PropertyLoaderException(String message){
		this.message = message;
	}
	
	public PropertyLoaderException(Throwable cause, String message){
		super(cause);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
