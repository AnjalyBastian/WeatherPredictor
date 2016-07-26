package com.datamine.predictor;

import org.apache.log4j.Logger;

import com.datamine.predictor.dto.CLIArguments;
import com.datamine.predictor.exception.WeatherPredictionException;
import com.datamine.predictor.util.Constants;

public class WeatherPredictorAppFactory {

	public final static Logger logger = Logger.getLogger(WeatherPredictorAppFactory.class);

	/**
	 * Factory Design to decide the App on which the execution flow should be triggered. 
	 * 
	 * 
	 * @param cliArguments - Program arguments
	 * @return App (Learner / Forecaster / Combined)
	 * @throws Exception
	 */
	public WeatherPredictorApp getWeatherPredictorApp(CLIArguments cliArguments) throws WeatherPredictionException {

		logger.info("triggerName : " + cliArguments.getTrigger());

		switch (cliArguments.getTrigger()) {
		case Constants.APP_TRIGGER_MODEL:
			return new WeatherPredictorLearner();
		case Constants.APP_TRIGGER_FORECAST:
			return new WeatherPredictorForecaster(cliArguments.getForecastLimit());
		case Constants.APP_TRIGGER_ALL:
			return new WeatherPredictorCombined(cliArguments.getForecastLimit());
		default:
			logger.error("invalid program triger .... please use the format");
			throw new WeatherPredictionException("invalid program triger .... please use the format");
		}
	}
}
