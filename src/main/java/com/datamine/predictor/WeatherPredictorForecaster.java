package com.datamine.predictor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.datamine.predictor.dto.ModelCoefficient;
import com.datamine.predictor.dto.StationDetails;
import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.dto.WeatherParameters;
import com.datamine.predictor.exception.WeatherPredictionException;
import com.datamine.predictor.io.WeatherParser;
import com.datamine.predictor.util.Constants;
import com.datamine.predictor.util.DateUtil;
import com.datamine.predictor.util.FileUtil;
import com.datamine.predictor.util.StationDetailsLoader;
import com.datamine.predictor.util.Weather;

/**
 * @author anjalybastian
 * 
 * Forecaster App flows through the following Stages : -
 * 
 * 	1. Reads in the evaluated Model Coefficients. 
 * 	2. Uses the available input dataset for prediction. 
 * 	3. Executes the prediction. 
 * 	4. Persist the forecasted values on to a file. 
 */
public class WeatherPredictorForecaster implements WeatherPredictorApp {

	public final static Logger logger = Logger.getLogger(WeatherPredictorForecaster.class);

	private int forecastLimit;

	/**
	 * If the forecast limit parameter is passed as Command Line argument, that value will be used as the threshold. 
	 * Otherwise sets the default Limit as 10
	 * 
	 * @param forecastLimit - CLI Argument
	 */
	public WeatherPredictorForecaster(int forecastLimit) {
		if (forecastLimit > 0)
			this.forecastLimit = forecastLimit;
		else
			this.forecastLimit = Constants.DEFAULT_FORECAST_DAYS;
	}

	@Override
	public boolean trigger() throws WeatherPredictionException {
		try {
			WeatherParser weatherParser = new WeatherParser();

			logger.debug("Forecaster Begins...");

			/**
			 * Reads the model coefficients provided by the learner - Pluggable component; Manual values are also possible
			 */
			List<ModelCoefficient> modelCoefficients = getModelCoefficients(Constants.MODEL_COEFFICENT_PATH);

			/**
			 * Reading the historical data for prediction
			 */
			WeatherData weatherData = weatherParser.parse(Constants.WEATHER_DATA_PATH);

			String forecastedData = getForecastedData(this.forecastLimit, weatherData, modelCoefficients);

			logger.info("Forecasted data :" + forecastedData);

			FileUtil.writeFile(Constants.FORECASTED_DATA_PATH, forecastedData);

			return true;
		} catch (Exception exception) {
			logger.error("Exception Occured while forecasting weather...");
			throw new WeatherPredictionException(exception, "Exception Occured while forecasting weather...");
		}

	}

	private List<ModelCoefficient> getModelCoefficients(String path) throws Exception {
		List<ModelCoefficient> modelCoefficients = new ArrayList<ModelCoefficient>();
		List<CSVRecord> modelCoefficientRecords = FileUtil.readCSV(path);

		if (null != modelCoefficientRecords && !modelCoefficientRecords.isEmpty()) {
			
			// Removing the header
			modelCoefficientRecords.remove(0);

			for (CSVRecord stationModelCoefficientData : modelCoefficientRecords) {
				ModelCoefficient modelCoefficient = new ModelCoefficient();
				modelCoefficient.setStationId(stationModelCoefficientData.get(0));
				modelCoefficient.setConstant(Double.parseDouble(stationModelCoefficientData.get(1)));
				modelCoefficient.setHumidityCoeffient(Double.parseDouble(stationModelCoefficientData.get(2)));
				modelCoefficient.setPressureCoeffient(Double.parseDouble(stationModelCoefficientData.get(3)));
				modelCoefficient.setPrevDayTempCoefficient(Double.parseDouble(stationModelCoefficientData.get(4)));
				modelCoefficient.setPrevYearTempCoefficient(Double.parseDouble(stationModelCoefficientData.get(5)));
				modelCoefficient.setPrevsDayPrevYearTempCoefficient(Double.parseDouble(stationModelCoefficientData.get(6)));
				modelCoefficient.setPressureStandardDeviation(Double.parseDouble(stationModelCoefficientData.get(7)));
				modelCoefficient.setHumidityStandardDeviation(Double.parseDouble(stationModelCoefficientData.get(8)));

				modelCoefficients.add(modelCoefficient);
			}
		}

		return modelCoefficients;
	}

	private String getForecastedData(int forecastLimit, WeatherData weatherData,
			List<ModelCoefficient> modelCoefficients) throws Exception {

		StringBuffer forecastedData = new StringBuffer();
		forecastedData.append(Constants.FORECAST_FILE_HEADER);
		forecastedData.append(Constants.NEW_LINE_SEPARATOR);

		for (ModelCoefficient modelCoefficient : modelCoefficients) {

			Map<String, WeatherParameters> stationData = weatherData.getStationWiseDailyStats()
					.get(modelCoefficient.getStationId());

			for (int foreastCounter = 0; foreastCounter < forecastLimit; foreastCounter++) {
				String latestDate = Collections.max(stationData.keySet());
				String forecastDate = DateUtil.getDate(latestDate, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_MONTH,
						1);

				WeatherParameters prevDayWeatherParams = stationData.get(latestDate);
				WeatherParameters prevYearWeatherParams = stationData
						.get(DateUtil.getDate(latestDate, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_YEAR, -365));
				WeatherParameters prevDayPrevYearWeatherParams = stationData
						.get(DateUtil.getDate(latestDate, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_YEAR, -366));
				double fittedHumidity = getRandomValue(
						(prevDayWeatherParams.getHumidity() + prevYearWeatherParams.getHumidity()) / 2,
						modelCoefficient.getHumidityStandardDeviation());
				double fittedPressure = getRandomValue(
						(prevDayWeatherParams.getPressure() + prevYearWeatherParams.getPressure()) / 2,
						modelCoefficient.getPressureStandardDeviation());

				/**
				 * Multiple regression
				 * 
				 * temperature = Constant + 
				 * 					Temp-1Coef * Temp-1 + 
				 * 					Temp-365Coef * Temp-365 + 
				 * 					Temp-366Coef * temp-366 +
				 * 					HumidityCoef * Humidity +
				 * 					PressureCoef * Pressure  
				 * 
				 */
				double forecastedTemperature = modelCoefficient.getConstant()
						+ prevDayWeatherParams.getTemperature() * modelCoefficient.prevDayTempCoefficient
						+ prevYearWeatherParams.getTemperature() * modelCoefficient.getPrevYearTempCoefficient()
						+ prevDayPrevYearWeatherParams.getTemperature()
								* modelCoefficient.getPrevsDayPrevYearTempCoefficient()
						+ fittedHumidity * modelCoefficient.getHumidityCoeffient()
						+ fittedPressure * modelCoefficient.getPressureCoeffient();

				WeatherParameters forecastedParameters = new WeatherParameters();

				forecastedParameters.setTemperature(forecastedTemperature);
				forecastedParameters.setHumidity(fittedHumidity);
				forecastedParameters.setPressure(fittedPressure);
				forecastedParameters.setCondition(getCondition(forecastedTemperature, fittedPressure, fittedHumidity));
				
				// Uses this forecasted data for next prediction. An apparent carry over of error is expected. 
				stationData.put(forecastDate, forecastedParameters);

				forecastedData.append(generateForecastedDataContent(modelCoefficient.getStationId(), forecastDate,
						forecastedParameters));
			}
		}

		return forecastedData.toString();
	}

	/**
	 * Simple decision model used for evaluating the Condition. 
	 *  TODO : Could have used a classification algorithm like Logistic Regression. 
	 * 
	 * @param forecastedTemperature 
	 * @param fittedPressure 
	 * @param fittedHumidity
	 * @return
	 */
	private String getCondition(double forecastedTemperature, double fittedPressure, double fittedHumidity) {
		String weather = null;
		if (forecastedTemperature < 0.0) {
			weather = Weather.SNOW.toString();
		} else if (forecastedTemperature > 0.0 && fittedHumidity > 70.0) {
			weather = Weather.RAIN.toString();
		} else {
			weather = Weather.SUNNY.toString();
		}
		return weather;
	}

	private double getRandomValue(double mean, double sd) {
		return (mean - sd) + ((sd * 2) * Math.random());
	}

	private String generateForecastedDataContent(String stationName, String date,
			WeatherParameters forecastedParameters) {
		StringBuffer forecastedData = new StringBuffer();
		
		StationDetails stationData = StationDetailsLoader.getStationData(stationName);

		forecastedData.append(stationData.getIataCode());
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(stationData.getLatitude());
		forecastedData.append(Constants.DELIMITER_COMMA);
		forecastedData.append(stationData.getLongitude());
		forecastedData.append(Constants.DELIMITER_COMMA);
		forecastedData.append(stationData.getAltitude());
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(date);
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(forecastedParameters.getCondition());
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(forecastedParameters.getTemperature());
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(forecastedParameters.getPressure());
		forecastedData.append(Constants.DELIMITER_PIPE);
		forecastedData.append(forecastedParameters.getHumidity());
		forecastedData.append(Constants.NEW_LINE_SEPARATOR);

		return forecastedData.toString();
	}
	
}
