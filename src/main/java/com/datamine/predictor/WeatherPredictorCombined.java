package com.datamine.predictor;

import org.apache.log4j.Logger;

import com.datamine.predictor.exception.WeatherPredictionException;
import com.datamine.predictor.util.Constants;

/**
 * 
 * Combined App flows through the following Phases. 
 * 
 * 	1. Triggers the Learner App
 * 	2. Triggers the Forecaster App. 
 * 
 * @author anjalybastian
 *
 */
public class WeatherPredictorCombined implements WeatherPredictorApp {
	
	public final static Logger logger = Logger.getLogger(WeatherPredictorCombined.class);
	
	private int forecastLimit;
	
	public  WeatherPredictorCombined(int forecastLimit) {
		if (forecastLimit > 0)
			this.forecastLimit = forecastLimit;
		else
			this.forecastLimit = Constants.DEFAULT_FORECAST_DAYS;
	}

	@Override
	public boolean trigger() throws WeatherPredictionException {
		WeatherPredictorLearner weatherPredictorLearner = new WeatherPredictorLearner();
		weatherPredictorLearner.trigger();
		logger.debug("Model learner completed successfully..");
		
		WeatherPredictorForecaster weatherPredictorForecaster = new WeatherPredictorForecaster(forecastLimit);
		weatherPredictorForecaster.trigger();
		logger.debug("Forecaster completed successfully..");
		
		return true;
	}
}
