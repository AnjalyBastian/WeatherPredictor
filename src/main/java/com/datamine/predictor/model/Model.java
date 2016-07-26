package com.datamine.predictor.model;

import java.util.Map;

import com.datamine.predictor.dto.ModelData;
import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.exception.ModelLearnerException;

public interface Model {
	Map<String, ModelData> preprocessWeatherData(WeatherData weatherData) throws ModelLearnerException;
	
	String evaluateModelCoeffients(Map<String, ModelData> stationLevelModelData) throws ModelLearnerException;
}
