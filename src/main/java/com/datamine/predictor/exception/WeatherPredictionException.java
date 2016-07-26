package com.datamine.predictor.exception;

public class WeatherPredictionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public WeatherPredictionException(String message){
		this.message = message;
	}
	
	public WeatherPredictionException(Throwable cause, String message){
		super(cause);
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
