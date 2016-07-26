package com.datamine.predictor.exception;

public class WeatherParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public WeatherParseException(String message){
		this.message = message;
	}
	
	public WeatherParseException(Throwable cause, String message){
		super(cause);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
