package com.datamine.predictor.dto;

public class ModelCoefficient {

	public String stationId;

	public double humidityCoeffient;

	public double pressureCoeffient;

	public double prevDayTempCoefficient;

	public double prevYearTempCoefficient;

	public double prevsDayPrevYearTempCoefficient;

	public double constant;

	public double pressureStandardDeviation;

	public double humidityStandardDeviation;

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public double getHumidityCoeffient() {
		return humidityCoeffient;
	}

	public void setHumidityCoeffient(double humidityCoeffient) {
		this.humidityCoeffient = humidityCoeffient;
	}

	public double getPressureCoeffient() {
		return pressureCoeffient;
	}

	public void setPressureCoeffient(double pressureCoeffient) {
		this.pressureCoeffient = pressureCoeffient;
	}

	public double getPrevDayTempCoefficient() {
		return prevDayTempCoefficient;
	}

	public void setPrevDayTempCoefficient(double prevDayTempCoefficient) {
		this.prevDayTempCoefficient = prevDayTempCoefficient;
	}

	public double getPrevYearTempCoefficient() {
		return prevYearTempCoefficient;
	}

	public void setPrevYearTempCoefficient(double prevYearTempCoefficient) {
		this.prevYearTempCoefficient = prevYearTempCoefficient;
	}

	public double getPrevsDayPrevYearTempCoefficient() {
		return prevsDayPrevYearTempCoefficient;
	}

	public void setPrevsDayPrevYearTempCoefficient(double prevsDayPrevYearTempCoefficient) {
		this.prevsDayPrevYearTempCoefficient = prevsDayPrevYearTempCoefficient;
	}

	public double getConstant() {
		return constant;
	}

	public void setConstant(double constant) {
		this.constant = constant;
	}

	public double getPressureStandardDeviation() {
		return pressureStandardDeviation;
	}

	public void setPressureStandardDeviation(double pressureStandardDeviation) {
		this.pressureStandardDeviation = pressureStandardDeviation;
	}

	public double getHumidityStandardDeviation() {
		return humidityStandardDeviation;
	}

	public void setHumidityStandardDeviation(double humidityStandardDeviation) {
		this.humidityStandardDeviation = humidityStandardDeviation;
	}

}
