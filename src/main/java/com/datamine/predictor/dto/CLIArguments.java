package com.datamine.predictor.dto;

public class CLIArguments {
	public String trigger;
	public int forecastLimit;

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public int getForecastLimit() {
		return forecastLimit;
	}

	public void setForecastLimit(int forecastLimit) {
		this.forecastLimit = forecastLimit;
	}

}
