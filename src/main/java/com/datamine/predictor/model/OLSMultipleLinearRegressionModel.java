package com.datamine.predictor.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.log4j.Logger;

import com.datamine.predictor.dto.ModelData;
import com.datamine.predictor.dto.WeatherData;
import com.datamine.predictor.dto.WeatherParameters;
import com.datamine.predictor.exception.ModelLearnerException;
import com.datamine.predictor.util.Constants;
import com.datamine.predictor.util.DateUtil;

public class OLSMultipleLinearRegressionModel implements Model {

	public final static Logger logger = Logger.getLogger(OLSMultipleLinearRegressionModel.class);

	@Override
	public Map<String, ModelData> preprocessWeatherData(WeatherData weatherData) throws ModelLearnerException {
		Map<String, ModelData> stationWiseModelData = new HashMap<String, ModelData>();
		try {
			for (String stationId : weatherData.getStationWiseDailyStats().keySet()) {
				Map<String, WeatherParameters> dailyWeatherStats = weatherData.getStationWiseDailyStats()
						.get(stationId);
				stationWiseModelData.put(stationId, createModelData(dailyWeatherStats));
			}
		} catch (Exception exception) {
			logger.error("Exception while  preprocessing data for model learning");
			throw new ModelLearnerException("Exception while  preprocessing data for model learning");
		}
		return stationWiseModelData;
	}

	/**
	 * Triggers the Linear Regression Model with the Station wise model Data. 
	 */
	@Override
	public String evaluateModelCoeffients(Map<String, ModelData> stationLevelModelData) throws ModelLearnerException {
		StringBuffer modelCoefficents = new StringBuffer();
		modelCoefficents.append(Constants.FILE_HEADER.toString());
		modelCoefficents.append(Constants.NEW_LINE_SEPARATOR);
		if (null != stationLevelModelData) {
			for (Entry<String, ModelData> stationLevelModelDataEntry : stationLevelModelData.entrySet()) {
				modelCoefficents.append(stationLevelModelDataEntry.getKey());
				modelCoefficents.append(Constants.DELIMITER_COMMA);
				constructModelCoefficientAtStationLevel(stationLevelModelDataEntry.getValue(), modelCoefficents);
			}
		}
		return modelCoefficents.toString();
	}

	/**
	 *  The Linear Model is defined as follows 
	 *  
	 *  the given y (temperature) has a linear relationship with 
	 *  	temp-1   : Last days temperature
	 *  	temp-365 : Last years temperature
	 *  	temp-366 : Day before the last year' temperature
	 *  	pressure : Pressure at the given point
	 *  	humidity : humidity at the given point
	 * 
	 * @param dailyWeatherStats - Daily weather statistics for a station. 
	 * @return ModelData - The final model data format required for initiating the learning. 
	 * @throws ParseException - If any error is occurred. 
	 */
	private ModelData createModelData(Map<String, WeatherParameters> dailyWeatherStats) throws ParseException {
		ModelData modelData = new ModelData();
		double[][] dependentVariableArry = new double[dailyWeatherStats.size()][5];
		double[] independentVariableArry = new double[dailyWeatherStats.size()];
		double[] pressureVariableArry = new double[dailyWeatherStats.size()];
		double[] humidityVariableArry = new double[dailyWeatherStats.size()];
		int index = 0;
		for (Entry<String, WeatherParameters> dailyWeatherStatsEntry : dailyWeatherStats.entrySet()) {
			String date = dailyWeatherStatsEntry.getKey();
			String previousDate = DateUtil.getDate(date, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_YEAR, -1);
			String lastYearDate = DateUtil.getDate(date, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_YEAR, -365);
			String lastYearPreviousDate = DateUtil.getDate(date, Constants.DEFAULT_DATE_FORMAT, Calendar.DAY_OF_YEAR,
					-366);

			if (isRecordMissing(dailyWeatherStats, previousDate, lastYearDate, lastYearPreviousDate))
				continue;

			independentVariableArry[index] = dailyWeatherStatsEntry.getValue().getTemperature();
			pressureVariableArry[index] = dailyWeatherStatsEntry.getValue().getHumidity();
			humidityVariableArry[index] = dailyWeatherStatsEntry.getValue().getPressure();

			dependentVariableArry[index][0] = dailyWeatherStatsEntry.getValue().getHumidity();
			dependentVariableArry[index][1] = dailyWeatherStatsEntry.getValue().getPressure();
			dependentVariableArry[index][2] = dailyWeatherStats.get(previousDate).getTemperature();
			dependentVariableArry[index][3] = dailyWeatherStats.get(lastYearDate).getTemperature();
			dependentVariableArry[index][4] = dailyWeatherStats.get(lastYearPreviousDate).getTemperature();
			index++;
		}

		modelData.setDependentVariableArry(dependentVariableArry);
		modelData.setIndependentVariableArry(independentVariableArry);
		modelData.setPressureVariableArry(pressureVariableArry);
		modelData.setHumidityVariableArry(humidityVariableArry);
		logger.info("Model data created successfully...");
		return modelData;
	}

	private boolean isRecordMissing(Map<String, WeatherParameters> dailyWeatherStats, String previousDate,
			String lastYearDate, String lastYearPreviousDate) {
		return null == dailyWeatherStats.get(previousDate) || null == dailyWeatherStats.get(lastYearDate)
				|| null == dailyWeatherStats.get(lastYearPreviousDate);
	}

	private void constructModelCoefficientAtStationLevel(ModelData stationModelData, StringBuffer modelCoefficents)
			throws ModelLearnerException {

		List<Double> stationLevelModelCoefficients = evaluateStationLevelModelCoefficient(stationModelData);

		for (Double stationLevelModelCoefficient : stationLevelModelCoefficients) {
			modelCoefficents.append(String.valueOf(stationLevelModelCoefficient));
			modelCoefficents.append(Constants.DELIMITER_COMMA);
		}
		if (modelCoefficents.length() > 0)
			modelCoefficents.setLength(modelCoefficents.length() - 1);

		modelCoefficents.append(Constants.NEW_LINE_SEPARATOR);
	}

	private List<Double> evaluateStationLevelModelCoefficient(ModelData stationModelData) throws ModelLearnerException {
		List<Double> modelCoefficents = new ArrayList<Double>();
		OLSMultipleLinearRegression olsMultipleLinearRegression = new OLSMultipleLinearRegression();
		olsMultipleLinearRegression.newSampleData(stationModelData.getIndependentVariableArry(),
				stationModelData.getDependentVariableArry());
		for (double modelCoefficent : olsMultipleLinearRegression.estimateRegressionParameters()) {
			modelCoefficents.add(modelCoefficent);
		}
		modelCoefficents.add(Statistics.getStandardDeviation(stationModelData.getHumidityVariableArry()));
		modelCoefficents.add(Statistics.getStandardDeviation(stationModelData.getPressureVariableArry()));
		logger.info("Successfully calculated the coefficients for OLSMultipleLinearRegression");
		return modelCoefficents;
	}

}
