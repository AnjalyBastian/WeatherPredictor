package com.datamine.predictor;

import com.datamine.predictor.exception.WeatherPredictionException;

public interface WeatherPredictorApp {
	boolean trigger() throws WeatherPredictionException;
}
