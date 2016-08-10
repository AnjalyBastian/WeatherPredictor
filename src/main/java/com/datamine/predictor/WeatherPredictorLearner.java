package com.datamine.predictor;

import java.util.Map;

import org.apache.log4j.Logger;

import com.datamine.predictor.dto.ModelData;
import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.exception.WeatherPredictionException;
import com.datamine.predictor.io.WeatherParser;
import com.datamine.predictor.model.Model;
import com.datamine.predictor.model.ModelFactory;
import com.datamine.predictor.util.Constants;
import com.datamine.predictor.util.FileUtil;
import com.datamine.predictor.util.PropertyLoader;

/**
 * 
 * @author anjalybastian
 * 
 * WeatherPredictorLearner
 * 
 * 	This mode of execution will go through the following phases of execution. 
 * 
 * 		1. Parses the Historical Data. 
 * 		2. Gets the appropriate model for learning. 
 * 		3. Pre-processes the data to be fed to learning algorithm. 
 * 		4. Evaluate Model Coefficients. 
 * 		5. Write the model coefficients on to the file for future prediction. 
 *
 */
public class WeatherPredictorLearner implements WeatherPredictorApp {

	public final static Logger logger = Logger.getLogger(WeatherPredictorLearner.class);

	@Override
	public boolean trigger() throws WeatherPredictionException {
		try {
			WeatherParser weatherParser = new WeatherParser();
			ModelFactory modelFactory = new ModelFactory();

			logger.debug("Inside model learner...");

			/**
			 * Parses the historical weather data to initiate the learning.
			 */
			WeatherData weatherData = weatherParser.parse(Constants.WEATHER_DATA_PATH);

			/**
			 * Choose appropriate model
			 */
			Model model = modelFactory.getModel(PropertyLoader.getProperty(Constants.PROPERTY_NAME_MODEL));

			/**
			 * Data Cleansing:-
			 * 
			 * Pre-processes the data - 
			 * 	1. Discards records which doesn't fit the required learning format 
			 *  2. Discards record which doesn't fit the required date format.
			 * 
			 */
			Map<String, ModelData> stationWiseModelData = model.preprocessWeatherData(weatherData);

			/**
			 * Triggers the Statistical models and evaluates the model coefficients required for the forecaster
			 */
			String modelCoefficients = model.evaluateModelCoeffients(stationWiseModelData);

			/**
			 * Writes the learned model coefficients
			 */
			boolean status = FileUtil.writeFile(Constants.MODEL_COEFFICENT_PATH, modelCoefficients);
			
			logger.debug("Completed model learning");

			return status;
		} catch (Exception exception) {
			logger.error("Exception Occured while model learning...");
			throw new WeatherPredictionException(exception, "Exception Occured while model learning...");
		}
	}
}
