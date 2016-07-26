package com.datamine;

import org.apache.log4j.Logger;

import com.datamine.predictor.WeatherPredictorApp;
import com.datamine.predictor.WeatherPredictorAppFactory;
import com.datamine.predictor.util.CLIParser;

public class WeatherPredictor {

	public final static Logger logger = Logger.getLogger(WeatherPredictor.class);

	/**
	 *  The Weather Prediction Solution can be Used in 3 Ways. 
	 * 
	 * 
	 * 1. model 	:- Triggers the model alone to generate the model coefficients for doing the further prediction. 
	 * 2. forecast <forecastLimit> :- Triggers the forecast App. Uses the available model coefficients for the prediction.
	 * 		forecastLimit specifies the number of predictions to be made.    
	 * 3. all <forecastLimit>	:- Triggers both model Learner and Forecaster. 
	 *
	 * 
	 * @param args - Program arguments
	 */
	public static void main(String[] args) {
		try {
			WeatherPredictorApp weatherPredictorApp = new WeatherPredictorAppFactory()
					.getWeatherPredictorApp(CLIParser.getCLIArguments(args));
			weatherPredictorApp.trigger();
		} catch (Exception exception) {
			logger.error("Error occured in  weather prediction App : " + exception);
		}
	}
}