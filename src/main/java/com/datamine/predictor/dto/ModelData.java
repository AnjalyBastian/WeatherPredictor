package com.datamine.predictor.dto;

import java.io.Serializable;

public class ModelData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double[][] independentVariableArry;
	private double[] dependentVariableArry;
	private double[] pressureVariableArry;
	private double[] humidityVariableArry;

	public double[][] getIndependentVariableArry() {
		return independentVariableArry;
	}

	public void setIndependentVariableArry(double[][] independentVariableArry) {
		this.independentVariableArry = independentVariableArry;
	}

	public double[] getDependentVariableArry() {
		return dependentVariableArry;
	}

	public void setDependentVariableArry(double[] dependentVariableArry) {
		this.dependentVariableArry = dependentVariableArry;
	}

	public double[] getPressureVariableArry() {
		return pressureVariableArry;
	}

	public void setPressureVariableArry(double[] pressureVariableArry) {
		this.pressureVariableArry = pressureVariableArry;
	}

	public double[] getHumidityVariableArry() {
		return humidityVariableArry;
	}

	public void setHumidityVariableArry(double[] humidityVariableArry) {
		this.humidityVariableArry = humidityVariableArry;
	}

}
